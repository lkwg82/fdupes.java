package de.lgohlke.utils.filter.pair;

import com.google.common.annotations.VisibleForTesting;
import de.lgohlke.utils.FileInfo;

import java.io.IOException;
import java.nio.file.Path;

public class NotSameOwnerFilter implements PairFilter {
    @Override
    public boolean select(Pair pair) {
        Path p1 = pair.getP1();
        Path p2 = pair.getP2();

        return getUid(p1) == getUid(p2) && getGid(p1) == getGid(p2);
    }

    @VisibleForTesting
    int getUid(Path path) {
        FileInfo fileInfo = new FileInfo(path);
        try {
            return fileInfo.getUid();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @VisibleForTesting
    int getGid(Path path) {
        FileInfo fileInfo = new FileInfo(path);
        try {
            return fileInfo.getGid();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
