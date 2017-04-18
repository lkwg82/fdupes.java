package de.lgohlke.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
class MyFileVisitor extends SimpleFileVisitor<Path> {
    private final long minimumSize;
    private final long maximumSize;
    @Getter
    private final Map<Long, List<Path>> sizeToPathMap = new TreeMap<>(Long::compareTo);

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (attrs.isSymbolicLink() || attrs.isDirectory()) {
            return FileVisitResult.CONTINUE;
        }

        long size = attrs.size();

        if (minimumSize <= size && size <= maximumSize) {
            map(file, size);
        }
        return FileVisitResult.CONTINUE;
    }

    private void map(Path file, long size) {
        if (sizeToPathMap.containsKey(size)) {
            sizeToPathMap.get(size).add(file);
        } else {
            List<Path> list = new ArrayList<>();
            list.add(file);
            sizeToPathMap.put(size, list);
        }
    }
}
