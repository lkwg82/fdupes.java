package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.pair.Pair;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PairGeneratorTest {

    private PairGenerator pairGenerator = new PairGenerator();

    @Test
    public void singlePair() {
        List<Path> list = Lists.newArrayList(Paths.get("a"), Paths.get("b"));
        List<Pair> pairs = pairGenerator.generate(list);

        assertThat(pairs).hasSize(1);
    }

    @Test
    public void tripplePair() {
        List<Path> list = Lists.newArrayList(Paths.get("a"), Paths.get("b"), Paths.get("c"));
        List<Pair> pairs = pairGenerator.generate(list);

        assertThat(pairs).hasSize(3);
    }
}
