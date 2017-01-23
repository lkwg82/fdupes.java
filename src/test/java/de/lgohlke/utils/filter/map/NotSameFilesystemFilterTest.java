package de.lgohlke.utils.filter.map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NotSameFilesystemFilterTest {

    private Map<Long, List<Path>> map = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        map.put(1L, Lists.newArrayList(Paths.get("a"), Paths.get("b")));
    }

    @Test
    public void onSameDevice() {
        MyFilter filter = new MyFilter("root", new HashMap<>());
        Map<Long, List<Path>> filteredMap = filter.filter(map);

        assertThat(filteredMap).hasSize(1);
    }

    @Test
    public void notOnSameDevice() {
        Map<String, Integer> pathDeviceMap = ImmutableMap.of("a", 1, "b", 1);
        MyFilter filter = new MyFilter("root", pathDeviceMap);
        Map<Long, List<Path>> filteredMap = filter.filter(map);

        assertThat(filteredMap).isEmpty();
    }

    @Test
    public void mixedDevice() {
        Map<String, Integer> pathDeviceMap = ImmutableMap.of("a", 1);
        MyFilter filter = new MyFilter("root", pathDeviceMap);
        Map<Long, List<Path>> filteredMap = filter.filter(map);

        assertThat(filteredMap).isEmpty();
    }

    private static class MyFilter extends NotSameFilesystemFilter {
        private final Map<String, Integer> pathDeviceMap;

        public MyFilter(String rootPath, Map<String, Integer> pathDeviceMap) {
            super(Paths.get(rootPath));
            this.pathDeviceMap = pathDeviceMap;
        }

        @Override
        int getDeviceNo(Path path) {
            return null == pathDeviceMap ? 0 : pathDeviceMap.getOrDefault(path.toString(), 0);
        }
    }
}