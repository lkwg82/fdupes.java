package de.lgohlke.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

@RequiredArgsConstructor
class MyFileVisitor extends SimpleFileVisitor<Path> {
    private final long minimumSize;
    private final long maximumSize;
    private final Collection<SizePath> sizePaths;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (attrs.isSymbolicLink() || attrs.isDirectory()) {
            return FileVisitResult.CONTINUE;
        }

        long size = attrs.size();

        if (minimumSize <= size && size <= maximumSize) {
            sizePaths.add(new SizePath(size, file));
        }
        return FileVisitResult.CONTINUE;
    }

    @RequiredArgsConstructor
    @Getter
    static class SizePath {
        private final long size;
        private final Path path;
    }
}
