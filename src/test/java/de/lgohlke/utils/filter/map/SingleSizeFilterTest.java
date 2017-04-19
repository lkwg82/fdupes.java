package de.lgohlke.utils.filter.map;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleSizeFilterTest {

    @Test
    public void test() {
        Map<Long, Set<Path>> map = new HashMap<>();

        map.put(0L, Sets.newHashSet(Paths.get("x")));
        map.put(1L, Sets.newHashSet(Paths.get("a"), Paths.get("b")));

        SingleSizeFilter filter = new SingleSizeFilter();
        Map<Long, Set<Path>> filteredMap = filter.filter(map);

        assertThat(filteredMap).hasSize(1);
    }
}