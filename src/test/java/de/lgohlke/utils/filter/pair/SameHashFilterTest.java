package de.lgohlke.utils.filter.pair;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class SameHashFilterTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private PairFilter filter = new SameHashFilter();

    @Test
    public void shouldBeSame() throws IOException {
        File fileA = temporaryFolder.newFile();
        Files.write(fileA.toPath(), "best".getBytes());

        assertThat(filter.select(new Pair(fileA.toPath(), fileA.toPath()))).isTrue();
    }

    @Test
    public void shouldBedifferent() throws IOException {
        File fileA = temporaryFolder.newFile();
        Files.write(fileA.toPath(), "best".getBytes());
        File fileB = temporaryFolder.newFile();
        Files.write(fileB.toPath(), "best2".getBytes());

        assertThat(filter.select(new Pair(fileA.toPath(), fileB.toPath()))).isFalse();
    }

}
