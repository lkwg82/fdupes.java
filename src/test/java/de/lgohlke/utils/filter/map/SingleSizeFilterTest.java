package de.lgohlke.utils.filter.map;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleSizeFilterTest {

    @Test
    public void test() {
        Map<Long, List<Path>> map = new HashMap<>();

        map.put(0L, Lists.newArrayList(Paths.get("x")));
        map.put(1L, Lists.newArrayList(Paths.get("a"), Paths.get("b")));

        SingleSizeFilter filter = new SingleSizeFilter();
        Map<Long, List<Path>> filteredMap = filter.filter(map);

        assertThat(filteredMap).hasSize(1);
    }
}