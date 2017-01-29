package de.lgohlke.utils.filter.pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class SamePermissionsFilter implements PairFilter {
    @Override
    public boolean select(Pair pair) {
        try {
            Set<PosixFilePermission> filePermissionsA = Files.getPosixFilePermissions(pair.getP1(), LinkOption.NOFOLLOW_LINKS);
            Set<PosixFilePermission> permissionsB = Files.getPosixFilePermissions(pair.getP2(), LinkOption.NOFOLLOW_LINKS);

            return filePermissionsA.equals(permissionsB);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
