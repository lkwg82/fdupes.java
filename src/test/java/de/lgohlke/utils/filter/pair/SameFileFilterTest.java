package de.lgohlke.utils.filter.pair;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SameFileFilterTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private SameFileFilter filter = new SameFileFilter();

    @Test
    public void shouldBeSameFilesWhenHardlinked() throws IOException {
        File file = temporaryFolder.newFile();

        Path link = Paths.get(file.toPath().toAbsolutePath() + "2");
        Files.createLink(link, file.toPath());

        assertThat(filter.select(new Pair(link, file.toPath()))).isFalse();
    }

    @Test
    public void shouldBeSameFilesWhenSymboliclinked() throws IOException {
        File file = temporaryFolder.newFile();

        Path link = Paths.get(file.toPath().toAbsolutePath() + "2");
        Files.createSymbolicLink(link, file.toPath());

        assertThat(filter.select(new Pair(link, file.toPath()))).isFalse();
    }

}
