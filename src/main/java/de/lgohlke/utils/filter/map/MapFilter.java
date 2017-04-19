package de.lgohlke.utils.filter.map;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface MapFilter {
    Map<Long, Set<Path>> filter(Map<Long, Set<Path>> sizeToFileMap);
}
