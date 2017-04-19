package de.lgohlke.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class FileInfo {
    private final static Cache<PathAttribute, String> CACHE = CacheBuilder.newBuilder().maximumSize(1000).build();

    private final Path path;

    public int getDevice() {
        return readAttributeAsInt(path, "unix:dev");
    }

    public int getUid() {
        return readAttributeAsInt(path, "unix:uid");
    }

    public int getGid() {
        return readAttributeAsInt(path, "unix:gid");
    }

    private static int readAttributeAsInt(Path path, String attribute) {

        PathAttribute key = new PathAttribute(path, attribute);

        try {
            String result = CACHE.get(key, () -> Files.getAttribute(path, attribute).toString());
            return Integer.valueOf(result);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class PathAttribute {
        private final Path path;
        private final String attribute;
    }
}
