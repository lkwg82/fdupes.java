package de.lgohlke.utils;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyFileVisitor extends SimpleFileVisitor<Path> {
    @Getter
    private final Map<Long, List<String>> sizeToPathMap = new HashMap<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (Files.isSymbolicLink(file)) {
            return FileVisitResult.CONTINUE;
        }

        if (file.toFile().isDirectory()) {
            return FileVisitResult.CONTINUE;
        }

        long size = attrs.size();

        if (sizeToPathMap.containsKey(size)) {
            sizeToPathMap.get(size).add(file.toString());
        } else {
            List<String> list = new ArrayList<>();
            list.add(file.toString());
            sizeToPathMap.put(size, list);
        }
        return FileVisitResult.CONTINUE;
    }


}
