package de.lgohlke.utils.filter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NotSameFilesystemFilterTest {

    @Test
    public void onSameDevice() {
        Map<Long, List<String>> map = new HashMap<>();
        map.put(1L, Lists.newArrayList("a", "b"));

        MyFilter filter = new MyFilter("root", new HashMap<>());
        Map<Long, List<String>> filteredMap = filter.filter(map);

        assertThat(filteredMap).hasSize(1);
    }

    @Test
    public void notOnSameDevice() {
        Map<Long, List<String>> map = new HashMap<>();
        map.put(1L, Lists.newArrayList("a", "b"));

        Map<String, Integer> pathDeviceMap = ImmutableMap.of("a", 1, "b", 1);
        MyFilter filter = new MyFilter("root", pathDeviceMap);
        Map<Long, List<String>> filteredMap = filter.filter(map);

        assertThat(filteredMap).isEmpty();
    }

    @Test
    public void mixedDevice() {
        Map<Long, List<String>> map = new HashMap<>();
        map.put(1L, Lists.newArrayList("a", "b"));

        Map<String, Integer> pathDeviceMap = ImmutableMap.of("a", 1);
        MyFilter filter = new MyFilter("root", pathDeviceMap);
        Map<Long, List<String>> filteredMap = filter.filter(map);

        assertThat(filteredMap).isEmpty();
    }

    private static class MyFilter extends NotSameFilesystemFilter {
        private final Map<String, Integer> pathDeviceMap;

        public MyFilter(String rootPath, Map<String, Integer> pathDeviceMap) {
            super(rootPath);
            this.pathDeviceMap = pathDeviceMap;
        }

        @Override
        int getDeviceNo(String path) {
            return null == pathDeviceMap ? 0 : pathDeviceMap.getOrDefault(path, 0);
        }
    }
}