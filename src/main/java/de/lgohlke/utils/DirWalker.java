package de.lgohlke.utils;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
class DirWalker {

    static Map<Long, Set<Path>> walk(Path path, long minimumSize, long maximumSize) throws IOException {
        Collection<MyFileVisitor.SizePath> sizePaths = new ArrayList<>();

        MyFileVisitor visitor = new MyFileVisitor(minimumSize, maximumSize, sizePaths);
        Files.walkFileTree(path, visitor);

        Map<Long, Set<Path>> sizeToPathMap = new TreeMap<>(Long::compareTo);

        sizePaths.forEach(sizePath -> {
            long size = sizePath.getSize();
            Path file = sizePath.getPath();

            if (sizeToPathMap.containsKey(size)) {
                sizeToPathMap.get(size).add(file);
            } else {
                sizeToPathMap.put(size, Sets.newHashSet(file));
            }
        });
        sizePaths.clear();

        return sizeToPathMap;
    }
}
