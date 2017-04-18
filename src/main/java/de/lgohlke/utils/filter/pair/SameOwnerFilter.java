package de.lgohlke.utils.filter.pair;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.lgohlke.utils.FileInfo;

import java.io.IOException;
import java.nio.file.Path;

public class SameOwnerFilter implements PairFilter {
    private final static Cache<Path, Integer> CACHE_UID = CacheBuilder.newBuilder().maximumSize(10000).build();
    private final static Cache<Path, Integer> CACHE_GID = CacheBuilder.newBuilder().maximumSize(10000).build();

    @Override
    public boolean select(Pair pair) {
        Path p1 = pair.getP1();
        Path p2 = pair.getP2();

        return getUid(p1) == getUid(p2) && getGid(p1) == getGid(p2);
    }

    @VisibleForTesting
    int getUid(Path path) {
        Integer uid = CACHE_UID.getIfPresent(path);
        if (uid != null) {
            return uid;
        }

        FileInfo fileInfo = new FileInfo(path);
        try {
            int uidFresh = fileInfo.getUid();
            CACHE_UID.put(path, uidFresh);
            return uidFresh;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @VisibleForTesting
    int getGid(Path path) {
        Integer gid = CACHE_GID.getIfPresent(path);
        if (gid != null) {
            return gid;
        }

        FileInfo fileInfo = new FileInfo(path);
        try {
            int gidFresh = fileInfo.getGid();
            CACHE_GID.put(path, gidFresh);
            return gidFresh;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
