package de.lgohlke.utils;

import java.util.List;
import java.util.Map;

interface MapFilter {
    Map<Long, List<String>> filter(Map<Long, List<String>> sizeToFileMap);
}
