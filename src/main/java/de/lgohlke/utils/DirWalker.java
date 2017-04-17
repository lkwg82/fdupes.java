package de.lgohlke.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class DirWalker {

    static Map<Long, List<Path>> walk(Path path, long minimumSize, long maximumSize) throws IOException {
        MyFileVisitor visitor = new MyFileVisitor(minimumSize, maximumSize);
        Files.walkFileTree(path, visitor);
        return visitor.getSizeToPathMap();
    }
}
