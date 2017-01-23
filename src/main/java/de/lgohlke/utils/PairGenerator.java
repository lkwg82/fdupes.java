package de.lgohlke.utils;

import de.lgohlke.utils.filter.pair.Pair;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PairGenerator {
    public List<Pair> generate(List<Path> list) {
        List<Pair> pairs = new ArrayList<>();
        // cartesian
        list.forEach(p1 -> list.forEach(p2 -> {
            if (!p1.equals(p2)) {
                pairs.add(new Pair(p1, p2));
            }
        }));

        List<Pair> deduplicated = new ArrayList<>();
        for (Pair p : pairs) {
            boolean canBeAdded = true;
            for (Pair deduplicatedPair : deduplicated) {
                if (p.same(deduplicatedPair)) {
                    canBeAdded = false;
                    break;
                }
            }
            if (canBeAdded) {
                deduplicated.add(p);
            }
        }

        return deduplicated;
    }
}
