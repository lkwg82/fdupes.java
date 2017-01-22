package de.lgohlke.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FileInfoTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldReturnInode() throws Exception {
        File file = temporaryFolder.newFile();

        int inode = new FileInfo(file.toPath()).getInode();
        assertThat(inode).isGreaterThan(0);
    }

    @Test
    public void shouldHaveExactOneHardLink() throws Exception {
        File file = temporaryFolder.newFile();

        int hardlinks = new FileInfo(file.toPath()).getHardlinks();
        assertThat(hardlinks).isEqualTo(1);
    }

    @Test
    public void shouldHaveExactTwoHardLink() throws Exception {
        File file = temporaryFolder.newFile();

        Files.createLink(Paths.get(file.toPath().toAbsolutePath() + "2"), file.toPath());

        int hardlinks = new FileInfo(file.toPath()).getHardlinks();
        assertThat(hardlinks).isEqualTo(2);
    }

    @Test
    public void shouldReturnUID() throws Exception {
        int uid = new FileInfo(temporaryFolder.getRoot().toPath()).getUid();

        assertThat(uid).isNotZero();
    }

    @Test
    public void shouldReturnGID() throws Exception {
        int gid = new FileInfo(temporaryFolder.getRoot().toPath()).getGid();

        assertThat(gid).isNotZero();
    }

    @Test
    public void shouldReturnDevice() throws Exception {
        File file = temporaryFolder.newFile();

        int device = new FileInfo(file.toPath()).getDevice();
        assertThat(device).isGreaterThan(0);
    }
}