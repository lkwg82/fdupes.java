package de.lgohlke.utils.filter.pair;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class Same4KHashFilterTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private PairFilter filter = new Same4KHashFilter();

    @Test
    public void shouldBeSameExact4K() throws IOException {
        byte[] content = createRandomStream(4 * 1024);

        File fileA = temporaryFolder.newFile();
        Files.write(fileA.toPath(), content);

        assertThat(filter.select(new Pair(fileA.toPath(), fileA.toPath()))).isTrue();
    }

    @Test
    public void shouldBeSameLessThan4K() throws IOException {
        byte[] content = createRandomStream(1024);

        File fileA = temporaryFolder.newFile();
        Files.write(fileA.toPath(), content);

        assertThat(filter.select(new Pair(fileA.toPath(), fileA.toPath()))).isTrue();
    }

    @Test
    public void shouldBeDifferentExact4K() throws IOException {
        File fileA = temporaryFolder.newFile();
        Files.write(fileA.toPath(), createRandomStream(4 * 1024));
        File fileB = temporaryFolder.newFile();
        Files.write(fileB.toPath(), createRandomStream(4 * 1024));

        assertThat(filter.select(new Pair(fileA.toPath(), fileB.toPath()))).isFalse();
    }

    @Test
    public void shouldBeSameMoreThan4() throws IOException {
        byte[] first4K = createRandomStream(4 * 1024);
        byte[] randomA = createRandomStream(1024 * 1024);
        byte[] randomB = createRandomStream(1024 * 1024);

        byte[] contentA = new byte[first4K.length + randomA.length];
        byte[] contentB = new byte[first4K.length + randomB.length];

        System.arraycopy(first4K, 0, contentA, 0, first4K.length);
        System.arraycopy(randomA, 0, contentA, first4K.length, randomA.length);
        System.arraycopy(first4K, 0, contentB, 0, first4K.length);
        System.arraycopy(randomB, 0, contentA, first4K.length, randomB.length);

        File fileA = temporaryFolder.newFile();
        Files.write(fileA.toPath(), contentA);
        File fileB = temporaryFolder.newFile();
        Files.write(fileB.toPath(), contentB);

        assertThat(filter.select(new Pair(fileA.toPath(), fileB.toPath()))).isTrue();
    }

    private byte[] createRandomStream(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
