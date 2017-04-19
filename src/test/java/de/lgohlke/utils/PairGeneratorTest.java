package de.lgohlke.utils;

import com.google.common.collect.Sets;
import de.lgohlke.utils.filter.pair.Pair;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PairGeneratorTest {

    private PairGenerator pairGenerator = new PairGenerator();

    @Test
    public void singlePair() {
        Set<Path> list = Sets.newHashSet(Paths.get("a"), Paths.get("b"));
        Set<Pair> pairs = pairGenerator.generate(list);

        assertThat(pairs).hasSize(1);
    }

    @Test
    public void tripplePair() {
        Set<Path> list = Sets.newHashSet(Paths.get("a"), Paths.get("b"), Paths.get("c"));
        Set<Pair> pairs = pairGenerator.generate(list);

        assertThat(pairs).hasSize(3);
    }
}
