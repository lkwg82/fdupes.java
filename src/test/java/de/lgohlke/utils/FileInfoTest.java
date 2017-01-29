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