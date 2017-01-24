package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.map.MapFilter;
import de.lgohlke.utils.filter.map.NotSameFilesystemFilter;
import de.lgohlke.utils.filter.map.SingleSizeFilter;
import de.lgohlke.utils.filter.map.TooSmallFilter;
import de.lgohlke.utils.filter.pair.*;
import de.lgohlke.utils.status.Progress;
import de.lgohlke.utils.status.SizeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class FdupesApplication {
    private final String _path;

    private Path path;
    private Progress progress;
    private Map<Long, List<Path>> sizeToFileMap;

    FdupesApplication init() throws IOException {
        path = Paths.get(_path);
        log.info("scanning {}", path);
        sizeToFileMap = DirWalker.walk(path);
        log.info("scanned");
        progress = new Progress(sumFilesize(sizeToFileMap));
        return this;
    }

    FdupesApplication filterGlobal() {
        MapFilter tooBigFilter = sizeToFileMap -> sizeToFileMap.entrySet()
                                                               .stream()
                                                               .filter(entry -> entry.getKey() < 4 * 1024 * 1024)
                                                               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<MapFilter> filters = Lists.newArrayList(new SingleSizeFilter(), new TooSmallFilter(), new NotSameFilesystemFilter(path), tooBigFilter);

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

    FdupesApplication filterCandidatePairs(Function<Pair, Void> eleminateDuplicateOperation) {
        List<PairFilter> pairFilters = Lists.newArrayList(new SameFileFilter(), new NotSameOwnerFilter(), new Same4KHashFilter(), new SameHashFilter());

        log.info("start pairfiltering");
        Stream<Map.Entry<Long, List<Path>>> reverseSortedBySize = sizeToFileMap.entrySet()
                                                                               .stream()
                                                                               .sorted((o1, o2) -> Long.compare(o2.getKey(), o1
                                                                                       .getKey()));
        reverseSortedBySize.forEach(entry -> {
            List<Pair> pairs = new PairGenerator().generate(entry.getValue());
            log.info(" pairfiltering size ({}) #{}", entry.getKey(), pairs.size());

            for (PairFilter pairFilter : pairFilters) {
                int oldSize = pairs.size();
                if (oldSize > 0) {
                    pairs = pairs.stream().filter(pairFilter::select).collect(Collectors.toList());
                    log.info("  filtered {} -> {} with {}", oldSize, pairs.size(), pairFilter.getClass()
                                                                                             .getSimpleName());
                }
            }

            if (!pairs.isEmpty()) {
                pairs.stream().map(eleminateDuplicateOperation);
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

    private static String size(int oldSize) {
        return size((long) oldSize);
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
        String pathToScan = "/backup/backintime/lars-MS-7930/lars/4";

        // only for profiling
        TimeUnit.SECONDS.sleep(10);

        new FdupesApplication(pathToScan).init().filterGlobal().filterCandidatePairs(pair -> {
            log.info("duplicates: " + pair.getP1() + " & " + pair.getP2());
            return null;
        });


        log.info("finish");
    }

}
