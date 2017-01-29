package de.lgohlke.utils.filter.pair;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;

import static org.assertj.core.api.Assertions.assertThat;

public class SamePermissionsFilterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private final PairFilter filter = new SamePermissionsFilter();

    @Test
    public void samePermissionsShouldBeSelected() throws IOException {
        File file = temporaryFolder.newFile();

        Pair pair = new Pair(file.toPath(), file.toPath());
        assertThat(filter.select(pair)).isTrue();
    }

    @Test
    public void differentPermissionsShouldBeSelected() throws IOException {
        File fileA = temporaryFolder.newFile();
        File fileB = temporaryFolder.newFile();

        Files.setPosixFilePermissions(fileA.toPath(), PosixFilePermissions.fromString("--x--x---"));

        Pair pair = new Pair(fileA.toPath(), fileB.toPath());
        assertThat(filter.select(pair)).isFalse();
    }
}
