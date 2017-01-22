package de.lgohlke.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

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
    public void shouldReturnDevice() throws Exception {
        File file = temporaryFolder.newFile();

        int device = new FileInfo(file.toPath()).getDevice();
        assertThat(device).isGreaterThan(0);
    }
}