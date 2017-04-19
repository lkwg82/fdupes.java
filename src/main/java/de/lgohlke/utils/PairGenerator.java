package de.lgohlke.utils;

import de.lgohlke.utils.filter.pair.Pair;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class PairGenerator {
    Set<Pair> generate(Set<Path> list) {
        Set<Pair> pairs = new HashSet<>();

        // cartesian
        list.forEach(p1 -> list.forEach(p2 -> {
                         if (!p1.equals(p2)) {
                             pairs.add(new Pair(p1, p2));
                         }
                     })
        );

        return pairs;
    }

}
