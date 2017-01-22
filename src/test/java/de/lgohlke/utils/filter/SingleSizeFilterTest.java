package de.lgohlke.utils.filter;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleSizeFilterTest {

    @Test
    public void test() {
        Map<Long, List<String>> map = new HashMap<>();

        map.put(0L, Lists.newArrayList("x"));
        map.put(1L, Lists.newArrayList("a", "b"));

        SingleSizeFilter filter = new SingleSizeFilter();
        Map<Long, List<String>> filteredMap = filter.filter(map);

        assertThat(filteredMap).hasSize(1);
    }
}