package de.lgohlke.utils.filter;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SingleSizeFilter implements MapFilter {
    @Override
    public Map<Long, List<Path>> filter(Map<Long, List<Path>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().size() > 1)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}