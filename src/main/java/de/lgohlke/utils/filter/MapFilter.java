package de.lgohlke.utils.filter;

import java.util.List;
import java.util.Map;

public interface MapFilter {
    Map<Long, List<String>> filter(Map<Long, List<String>> sizeToFileMap);
}
