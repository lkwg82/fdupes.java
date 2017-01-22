package de.lgohlke.utils.filter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TooSmallFilter implements MapFilter {
    @Override
    public Map<Long, List<String>> filter(Map<Long, List<String>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getKey() > 4 * 1024)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
