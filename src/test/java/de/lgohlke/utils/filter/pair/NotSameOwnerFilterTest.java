package de.lgohlke.utils.filter.pair;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NotSameOwnerFilterTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Test
    public void sameOwner() throws IOException {
        Pair pair = new Pair(Paths.get("a"), Paths.get("b"));

        PairFilter filter = new MyFilter(new HashMap<>(), new HashMap<>());

        assertThat(filter.select(pair)).isTrue();
    }

    @Test
    public void notSameUID() throws IOException {
        Pair pair = new Pair(Paths.get("a"), Paths.get("b"));

        Map<String, Integer> uidMap = ImmutableMap.of("a", 1, "b", 2);
        PairFilter filter = new MyFilter(uidMap, new HashMap<>());

        assertThat(filter.select(pair)).isFalse();
    }

    @Test
    public void notSameGID() throws IOException {
        Pair pair = new Pair(Paths.get("a"), Paths.get("b"));

        Map<String, Integer> gidMap = ImmutableMap.of("a", 1, "b", 2);
        PairFilter filter = new MyFilter(new HashMap<>(), gidMap);

        assertThat(filter.select(pair)).isFalse();
    }

    @RequiredArgsConstructor
    private static class MyFilter extends NotSameOwnerFilter {
        private final Map<String, Integer> pathUidMap;
        private final Map<String, Integer> pathGidMap;

        @Override
        int getUid(Path path) {
            return pathUidMap.getOrDefault(path.toString(), 0);
        }

        @Override
        int getGid(Path path) {
            return pathGidMap.getOrDefault(path.toString(), 0);
        }
    }

}