package de.lgohlke.utils.filter.pair;

import org.junit.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class PairTest {
    @Test
    public void testEquals() {
        Pair p1 = new Pair(Paths.get("a"), Paths.get("b"));
        Pair p2 = new Pair(Paths.get("b"), Paths.get("a"));

        assertThat(p1.same(p2)).isTrue();
    }

    @Test
    public void testEquals2() {
        Pair p1 = new Pair(Paths.get("a"), Paths.get("b"));
        Pair p2 = new Pair(Paths.get("a"), Paths.get("c"));

        assertThat(p1.same(p2)).isFalse();
    }
}