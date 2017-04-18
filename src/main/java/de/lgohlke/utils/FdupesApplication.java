package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.map.MapFilter;
import de.lgohlke.utils.filter.map.NotSameFilesystemFilter;
import de.lgohlke.utils.filter.map.SingleSizeFilter;
import de.lgohlke.utils.filter.pair.*;
import de.lgohlke.utils.status.Progress;
import de.lgohlke.utils.status.SizeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class FdupesApplication {
    private final String _path;

    private Path path;
    private Progress progress;
    private Map<Long, List<Path>> sizeToFileMap;

    private FdupesApplication init(long minimumSize, long maximumSize) throws IOException {
        path = Paths.get(_path);
        log.info("scanning {}", path);
        sizeToFileMap = DirWalker.walk(path, minimumSize, maximumSize);
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
//                                                     new TooSmallFilter(),
                                                     new NotSameFilesystemFilter(path));

        for (MapFilter filter : filters) {
            long oldSize = sumFilesize(sizeToFileMap);
            if (oldSize > 0) {
                log.info("running {}", filter.getClass().getSimpleName());
                sizeToFileMap = filter.filter(sizeToFileMap);
                long newSize = sumFilesize(sizeToFileMap);
                log.info(" filtered from {} -> {}", size(oldSize), size(newSize));
                progress.reduce(oldSize - newSize);
            }
            log.info(progress.status());
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
        Stream<Map.Entry<Long, List<Path>>> reverseSortedBySize = sizeToFileMap.entrySet()
                                                                               .stream()
                                                                               .sorted((o1, o2) -> Long.compare(o2.getKey(),
                                                                                                                o1.getKey()));
        reverseSortedBySize.forEach(entry -> {
            List<Pair> pairs = new PairGenerator().generate(entry.getValue());
            log.info(" pairfiltering size ({}) #{}", size(entry.getKey()), pairs.size());

            for (PairFilter pairFilter : pairFilters) {
                int oldSize = pairs.size();
                if (oldSize > 0) {
                    pairs = pairs.stream()
                                 .filter(pairFilter::select)
                                 .collect(Collectors.toList());
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
            log.info(progress.status());
        });
        return this;
    }

    private static String size(long oldSize) {
        return SizeFormatter.readableFileSize(oldSize);
    }

    private static long sumFilesize(Map<Long, List<Path>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .map(entry -> entry.getKey() * entry.getValue().size())
                            .mapToLong(Long::longValue)
                            .sum();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("need a path to scan");
            System.exit(1);
        }
        String pathToScan = args[0];

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

        // only for profiling
//        TimeUnit.SECONDS.sleep(10);

        Saved saved = new Saved();
        Deduplicator eleminateDuplicateOperation = new Deduplicator(saved);
        new FdupesApplication(pathToScan).init(minimumSize, maximumSize)
                                         .filterGlobal()
                                         .filterCandidatePairs(eleminateDuplicateOperation);

        log.info("finish: " + saved);
    }
}
