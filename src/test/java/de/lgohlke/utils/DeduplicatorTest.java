package de.lgohlke.utils;

import de.lgohlke.utils.filter.pair.Pair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DeduplicatorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Saved saved = new Saved();
    private Deduplicator deduplicator = new Deduplicator(saved);

    @Test
    public void shouldHaveSamePermissions() throws Exception {
        Pair pair = createPair();

        Set<PosixFilePermission> permissionsA = PosixFilePermissions.fromString("---------");
        fileAttributeView(pair.getP1()).setPermissions(permissionsA);

        boolean eleminated = deduplicator.eleminate(pair);

        assertThat(eleminated).isTrue();
        Set<PosixFilePermission> permissionsB = fileAttributes(pair.getP2()).permissions();
        assertThat(permissionsA).isEqualTo(permissionsB);
    }

    @Test
    public void shouldBeEleminatedAndCounted() throws Exception {
        Pair pair = createPair();
        Files.write(pair.getP1(), "asdasdsadasdsa".getBytes());
        Files.write(pair.getP2(), "asdasdsadasdsa".getBytes());

        assertThat(deduplicator.eleminate(pair)).isTrue();
        assertThat(saved.getSavedBytes().longValue()).isEqualTo(14L);
        assertThat(saved.getCreatedLinks().intValue()).isEqualTo(1);
    }

    @Test
    public void shouldHaveTwoLinksAfterElemination() throws Exception {
        Pair pair = createPair();

        int hardlinksBefore = hardlinks(pair.getP1());

        boolean eleminated = deduplicator.eleminate(pair);

        assertThat(eleminated).isTrue();
        assertThat(hardlinksBefore + 1).isEqualTo(hardlinks(pair.getP2()));
    }

    @Test
    public void shouldFailIfFirstIsNotExisting() throws Exception {
        Pair pair = createPair();

        pair.getP1().toFile().delete();

        boolean eleminated = deduplicator.eleminate(pair);

        assertThat(eleminated).isFalse();
    }

    private Integer hardlinks(Path p1) throws IOException {
        return Integer.valueOf(Files.getAttribute(p1, "unix:nlink") + "");
    }

    @Test
    public void shouldNotEleminate() throws Exception {
        File file = temporaryFolder.newFile();

        Pair pair = new Pair(file.toPath(), file.toPath());

        assertThat(deduplicator.eleminate(pair)).isFalse();
    }

    private PosixFileAttributes fileAttributes(Path path) throws IOException {
        return fileAttributeView(path).readAttributes();
    }

    private PosixFileAttributeView fileAttributeView(Path path) {
        FileSystemProvider provider = path.getFileSystem().provider();
        return provider.getFileAttributeView(path, PosixFileAttributeView.class);
    }

    private Pair createPair() throws IOException {
        File fileA = temporaryFolder.newFile();
        File fileB = temporaryFolder.newFile();

        return new Pair(fileA.toPath(), fileB.toPath());
    }
}
