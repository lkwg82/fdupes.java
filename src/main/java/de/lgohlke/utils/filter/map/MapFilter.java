package de.lgohlke.utils.filter.map;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface MapFilter {
    Map<Long, List<Path>> filter(Map<Long, List<Path>> sizeToFileMap);
}
