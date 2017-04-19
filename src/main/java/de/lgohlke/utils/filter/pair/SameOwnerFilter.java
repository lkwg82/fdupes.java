package de.lgohlke.utils.filter.pair;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.lgohlke.utils.FileInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class SameOwnerFilter implements PairFilter {
    private final static Cache<Path, FileInfo> CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    @Override
    public boolean select(Pair pair) {
        Path p1 = pair.getP1();
        Path p2 = pair.getP2();

        return getUid(p1) == getUid(p2) && getGid(p1) == getGid(p2);
    }

    @VisibleForTesting
    int getUid(Path path) {
        try {
            return getFileInfo(path).getUid();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @VisibleForTesting
    int getGid(Path path) {
        try {
            return getFileInfo(path).getGid();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private FileInfo getFileInfo(Path path) {
        try {
            return CACHE.get(path, () -> new FileInfo(path));
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    ;
}
