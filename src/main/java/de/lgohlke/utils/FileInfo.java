package de.lgohlke.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class FileInfo {
    private final static Cache<PathAttribute, String> CACHE = CacheBuilder.newBuilder().maximumSize(1000).build();

    private final Path path;

    public int getDevice() throws IOException {
        return readAttributeAsInt(path, "unix:dev");
    }

    public int getUid() throws IOException {
        return readAttributeAsInt(path, "unix:uid");
    }

    public int getGid() throws IOException {
        return readAttributeAsInt(path, "unix:gid");
    }

    private static int readAttributeAsInt(Path path, String attribute) throws IOException {

        PathAttribute key = new PathAttribute(path, attribute);
        String result = CACHE.getIfPresent(key);
        if (result == null) {
            result = Files.getAttribute(path, attribute).toString();
            CACHE.put(key, result);
        }
        return Integer.valueOf(result);
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class PathAttribute {
        private final Path path;
        private final String attribute;
    }
}
