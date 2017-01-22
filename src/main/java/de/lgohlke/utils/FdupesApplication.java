package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.MapFilter;
import de.lgohlke.utils.filter.NotSameFilesystemFilter;
import de.lgohlke.utils.filter.SingleSizeFilter;
import de.lgohlke.utils.filter.TooSmallFilter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
public class FdupesApplication {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("/backup/backintime/lars-MS-7930/lars/4");
        log.info("scan {}", path);
        Map<Long, List<Path>> sizeToFileMap = DirWalker.walk(path);

        List<MapFilter> filters = Lists.newArrayList(new SingleSizeFilter(), new TooSmallFilter(), new NotSameFilesystemFilter(path));

        for (MapFilter filter : filters) {
            int oldSize = sizeToFileMap.size();
            log.info("running {}", filter.getClass().getSimpleName());
            sizeToFileMap = filter.filter(sizeToFileMap);
            int newSize = sizeToFileMap.size();
            log.info(" filtered from {} -> {}", oldSize, newSize);
        }
    }
}
