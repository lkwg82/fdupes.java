package de.lgohlke.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

class DirWalker {

    static Map<Long, List<String>> walk(String path) throws IOException {
        MyFileVisitor visitor = new MyFileVisitor();
        Files.walkFileTree(Paths.get(path), visitor);
        return visitor.getSizeToPathMap();
    }
}
