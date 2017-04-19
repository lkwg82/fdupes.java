package de.lgohlke.utils.filter.pair;

import com.google.common.annotations.VisibleForTesting;
import de.lgohlke.utils.FileInfo;

import java.nio.file.Path;

public class SameOwnerFilter implements PairFilter {

    @Override
    public boolean select(Pair pair) {
        Path p1 = pair.getP1();
        Path p2 = pair.getP2();

        return getUid(p1) == getUid(p2) && getGid(p1) == getGid(p2);
    }

    @VisibleForTesting
    int getUid(Path path) {
        return new FileInfo(path).getUid();
    }

    @VisibleForTesting
    int getGid(Path path) {
        return new FileInfo(path).getGid();
    }
}
