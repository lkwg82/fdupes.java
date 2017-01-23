package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.map.MapFilter;
import de.lgohlke.utils.filter.map.NotSameFilesystemFilter;
import de.lgohlke.utils.filter.map.SingleSizeFilter;
import de.lgohlke.utils.filter.map.TooSmallFilter;
import de.lgohlke.utils.filter.pair.NotSameOwnerFilter;
import de.lgohlke.utils.filter.pair.Pair;
import de.lgohlke.utils.filter.pair.PairFilter;
import de.lgohlke.utils.filter.pair.SameFileFilter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class FdupesApplication {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("/backup/backintime/lars-MS-7930/lars/4");
        log.info("scan {}", path);

        // only for profiling
        TimeUnit.SECONDS.sleep(10);

        Map<Long, List<Path>> sizeToFileMap = DirWalker.walk(path);
        log.info("scanned");
        List<MapFilter> filters = Lists.newArrayList(new SingleSizeFilter(), new TooSmallFilter(), new NotSameFilesystemFilter(path));

        for (MapFilter filter : filters) {
            int oldSize = sizeToFileMap.size();
            log.info("running {}", filter.getClass().getSimpleName());
            sizeToFileMap = filter.filter(sizeToFileMap);
            int newSize = sizeToFileMap.size();
            log.info(" filtered from {} -> {}", oldSize, newSize);
        }

        List<PairFilter> pairFilters = Lists.newArrayList(new NotSameOwnerFilter(), new SameFileFilter());

        log.info("start pairfiltering");
        sizeToFileMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(entry -> {
            List<Pair> pairs = new PairGenerator().generate(entry.getValue());
            log.info(" pairfiltering size ({}) #{}", entry.getKey(), pairs.size());

            for (PairFilter pairFilter : pairFilters) {
                int oldSize = pairs.size();
                pairs = pairs.stream().filter(pairFilter::select).collect(Collectors.toList());
                log.info("  filtered {} -> {} with {}", oldSize, pairs.size(), pairFilter.getClass().getSimpleName());
            }
        });
        log.info("finish");
    }
}
