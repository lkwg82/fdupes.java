package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.MapFilter;
import de.lgohlke.utils.filter.SingleSizeFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class FdupesApplication {

    public static void main(String[] args) throws Exception {
        String path = "/backup/backintime/lars-MS-7930/lars/4";
        log.info("scan {}", path);
        Map<Long, List<String>> sizeToFileMap = DirWalker.walk(path);

        List<MapFilter> filters = Lists.newArrayList(new SingleSizeFilter());

        for (MapFilter filter : filters) {
            int oldSize = sizeToFileMap.size();
            log.info("running {}", filter.getClass().getSimpleName());
            sizeToFileMap = filter.filter(sizeToFileMap);
            int newSize = sizeToFileMap.size();
            log.info(" filtered from {} -> {}", oldSize, newSize);
        }

    }

}
