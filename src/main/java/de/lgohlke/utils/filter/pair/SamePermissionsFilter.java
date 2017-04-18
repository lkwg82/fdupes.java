package de.lgohlke.utils.filter.pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SamePermissionsFilter implements PairFilter {
    private final static Cache<Path, Set<PosixFilePermission>>
            CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    @Override
    public boolean select(Pair pair) {
        try {
            Set<PosixFilePermission> permissionsA = retrievePerms(pair.getP1());
            Set<PosixFilePermission> permissionsB = retrievePerms(pair.getP2());

            return permissionsA.equals(permissionsB);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Set<PosixFilePermission> retrievePerms(Path path) throws ExecutionException {
        return CACHE.get(path, () -> Files.getPosixFilePermissions(path, LinkOption.NOFOLLOW_LINKS));
    }
}
