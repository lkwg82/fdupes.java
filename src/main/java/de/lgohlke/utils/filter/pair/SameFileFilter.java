package de.lgohlke.utils.filter.pair;

import java.io.IOException;
import java.nio.file.Files;

public class SameFileFilter implements PairFilter {
    @Override
    public boolean select(Pair pair) {
        try {
            return !Files.isSameFile(pair.getP1(), pair.getP2());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
