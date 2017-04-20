package de.lgohlke.utils.filter.map;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ZeroSizeFilterTest {
    @Test
    public void testEmpty() {
        Map<Long, Set<Path>> map = new HashMap<>();

        map.put(0L, Sets.newHashSet(Paths.get("x")));

        MapFilter filter = new ZeroSizeFilter();

        assertThat(filter.filter(map)).isEmpty();
    }

    @Test
    public void shouldBeNotEmpty() {
        Map<Long, Set<Path>> map = new HashMap<>();

        map.put(0L, Sets.newHashSet(Paths.get("x")));
        map.put(1L, Sets.newHashSet(Paths.get("x2")));

        MapFilter filter = new ZeroSizeFilter();

        assertThat(filter.filter(map)).hasSize(1);
    }

}
