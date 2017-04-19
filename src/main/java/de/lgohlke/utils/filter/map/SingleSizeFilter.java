package de.lgohlke.utils.filter.map;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SingleSizeFilter implements MapFilter {
    @Override
    public Map<Long, Set<Path>> filter(Map<Long, Set<Path>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().size() > 1)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}