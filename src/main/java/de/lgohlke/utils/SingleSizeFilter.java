package de.lgohlke.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class SingleSizeFilter implements MapFilter {
    @Override
    public Map<Long, List<String>> filter(Map<Long, List<String>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().size() > 1)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}