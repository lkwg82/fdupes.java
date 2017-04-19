package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.map.MapFilter;
import de.lgohlke.utils.filter.map.NotSameFilesystemFilter;
import de.lgohlke.utils.filter.map.SingleSizeFilter;
import de.lgohlke.utils.filter.pair.*;
import de.lgohlke.utils.status.Progress;
import de.lgohlke.utils.status.SizeFormatter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class FdupesApplication {
    private final RunConfig runConfig;

    private Progress progress;
    private Map<Long, Set<Path>> sizeToFileMap;

    private FdupesApplication init() throws IOException {
        log.info("scanning {}", runConfig.getPathToScan());
        sizeToFileMap = DirWalker.walk(runConfig.getPathToScan(),
                                       runConfig.getMinimumSize(),
                                       runConfig.getMaximumSize());
        log.info("scanned");
        progress = new Progress(sumFilesize(sizeToFileMap));
        return this;
    }

    private FdupesApplication filterGlobal() {
        MapFilter zeroSizeFiles = sizeToFileMap -> sizeToFileMap.entrySet()
                                                                .stream()
                                                                .filter(entry -> entry.getKey() > 0L)
                                                                .collect(Collectors.toMap(Map.Entry::getKey,
                                                                                          Map.Entry::getValue));

        List<MapFilter> filters = Lists.newArrayList(zeroSizeFiles,
                                                     new SingleSizeFilter(),
                                                     new NotSameFilesystemFilter(runConfig.getPathToScan()));

        for (MapFilter filter : filters) {
            long oldSize = sumFilesize(sizeToFileMap);
            if (oldSize > 0) {
                log.info("running {}", filter.getClass().getSimpleName());
                sizeToFileMap = filter.filter(sizeToFileMap);
                long newSize = sumFilesize(sizeToFileMap);
                log.info(" filtered from {} -> {}", size(oldSize), size(newSize));
                progress.reduce(oldSize - newSize);
            }
//            log.info(progress.status());
        }
        return this;
    }

    private FdupesApplication filterCandidatePairs(Function<Pair, Void> eleminateDuplicateOperation) {
        List<PairFilter> pairFilters = Lists.newArrayList(new SameFileFilter(),
                                                          new SameOwnerFilter(),
                                                          new SamePermissionsFilter(),
                                                          new Same4KHashFilter(),
                                                          new SameHashFilter());

        log.info("start pairfiltering");
        Stream<Map.Entry<Long, Set<Path>>> reverseSortedBySize = sizeToFileMap.entrySet()
                                                                              .stream()
                                                                              .sorted((o1, o2) -> Long.compare(o2.getKey(),
                                                                                                               o1.getKey()));
        reverseSortedBySize.forEach(entry -> {
            Set<Pair> pairs = new PairGenerator().generate(entry.getValue());
            log.info(" pairfiltering size ({}) #{}", size(entry.getKey()), pairs.size());

            for (PairFilter pairFilter : pairFilters) {
                int oldSize = pairs.size();
                if (oldSize > 0) {
                    pairs = pairs.stream()
                                 .filter(pairFilter::select)
                                 .collect(Collectors.toSet());
                    log.info("  filtered {} -> {} with {}", oldSize, pairs.size(), pairFilter.getClass()
                                                                                             .getSimpleName());
                }
            }

            if (!pairs.isEmpty()) {
                pairs.forEach(eleminateDuplicateOperation::apply);
            }

            int minus = pairs.stream()
                             .flatMap(pair -> Stream.of(pair.getP1(), pair.getP2()))
                             .collect(Collectors.toSet())
                             .size();

            progress.reduce(entry.getValue().size() - minus);
//            log.info(progress.status());
        });
        return this;
    }

    private static String size(long oldSize) {
        return SizeFormatter.readableFileSize(oldSize);
    }

    private static long sumFilesize(Map<Long, Set<Path>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .map(entry -> entry.getKey() * entry.getValue().size())
                            .mapToLong(Long::longValue)
                            .sum();
    }

    public static void main(String... args) throws Exception {

        RunConfig runConfig = buildRunConfig("/backup/wirt.lgohlke.de", "1", "3");

        Saved saved = new Saved();
        new FdupesApplication(runConfig).init()
                                        .filterGlobal()
                                        .filterCandidatePairs(new Deduplicator(saved));

        log.info("finish: " + saved);
    }

    private static RunConfig buildRunConfig(String... args) {
        if (args.length == 0) {
            System.err.println("need a path to scan");
            System.exit(1);
        }
        Path pathToScan = Paths.get(args[0]);

        int MEGABYTE = 1024 * 1024;
        long minimumSize = 0L;
        long maximumSize = Long.MAX_VALUE;
        if (args.length > 1) {
            try {
                minimumSize = Long.parseLong(args[1]) * MEGABYTE;
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (args.length > 2) {
            try {
                maximumSize = Long.parseLong(args[2]) * MEGABYTE;
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }
        }

        return new RunConfig(pathToScan, minimumSize, maximumSize);
    }

    @RequiredArgsConstructor
    @Getter
    private static class RunConfig {
        private final Path pathToScan;
        private final long minimumSize;
        private final long maximumSize;
    }
}
