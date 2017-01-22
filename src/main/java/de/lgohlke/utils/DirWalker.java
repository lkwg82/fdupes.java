package de.lgohlke.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class DirWalker {

    static Map<Long, List<Path>> walk(Path path) throws IOException {
        MyFileVisitor visitor = new MyFileVisitor();
        Files.walkFileTree(path, visitor);
        return visitor.getSizeToPathMap();
    }
}
