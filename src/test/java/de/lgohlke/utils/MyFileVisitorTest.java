package de.lgohlke.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static org.assertj.core.api.Assertions.assertThat;

public class MyFileVisitorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void happyPath() throws Exception {
        File file = temporaryFolder.newFile();
        Path path = file.toPath();

        MyFileVisitor visitor = new MyFileVisitor(0, Long.MAX_VALUE);

        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        visitor.visitFile(path, attributes);

        assertThat(visitor.getSizeToPathMap()).containsKeys(0L);
    }

    @Test
    public void shouldNotVisitSymbolicLinks() throws Exception {
        Path root = temporaryFolder.getRoot().toPath();
        Path link = Paths.get(root + "/symboliclink");
        Files.createSymbolicLink(link, Paths.get("/dev/null"));

        MyFileVisitor visitor = new MyFileVisitor(0, Long.MAX_VALUE);

        BasicFileAttributes attributes = Files.readAttributes(link, BasicFileAttributes.class);
        visitor.visitFile(link, attributes);

        assertThat(visitor.getSizeToPathMap()).isEmpty();
    }

    @Test
    public void shouldNotAddDirectories() throws Exception {
        Path root = temporaryFolder.getRoot().toPath();

        MyFileVisitor visitor = new MyFileVisitor(0, Long.MAX_VALUE);

        BasicFileAttributes attributes = Files.readAttributes(root, BasicFileAttributes.class);
        visitor.visitFile(root, attributes);

        assertThat(visitor.getSizeToPathMap()).isEmpty();
    }
}