package de.lgohlke.utils.filter;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TooSmallFilterTest {

    @Test
    public void test() {
        Map<Long, List<String>> map = new HashMap<>();

        map.put((long) 4 * 1024, Lists.newArrayList("x", "y"));
        map.put((long) 4 * 1024 + 1, Lists.newArrayList("a", "b"));

        MapFilter filter = new TooSmallFilter();
        Map<Long, List<String>> filteredMap = filter.filter(map);

        assertThat(filteredMap).hasSize(1);
        assertThat(filteredMap.get((long) (4 * 1024 + 1))).contains("a", "b");
    }

}