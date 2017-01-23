package de.lgohlke.utils;

import com.google.common.collect.Lists;
import de.lgohlke.utils.filter.pair.Pair;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PairGenerator {
    public List<Pair> generate(List<Path> list) {
        Set<Pair> pairs = new HashSet<>();
        // cartesian
        list.forEach(p1 -> list.forEach(p2 -> pairs.add(new Pair(p1, p2))));
        return Lists.newArrayList(pairs);
    }

}
