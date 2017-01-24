package de.lgohlke.utils.filter.pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
public class Same4KHashFilter implements PairFilter {
    private final static Cache<File, String> CACHE = CacheBuilder.newBuilder().maximumSize(1000).build();
    private final static int SIZE = 4 * 1024;

    @Override
    public boolean select(Pair pair) {
        String h1 = hash(pair.getP1());
        String h2 = hash(pair.getP2());
        return Objects.equals(h1, h2);
    }

    private String hash(Path path) {
        File file = path.toFile();

        String hash = CACHE.getIfPresent(file);
        if (hash == null) {
            try {
                byte[] data = new byte[SIZE];
                try (FileInputStream out = new FileInputStream(file)) {
                    long size = file.length();
                    long length = size < SIZE ? size : SIZE;
                    out.read(data, 0, (int) length);
                }
                hash = Hashing.md5().hashBytes(data).toString();
                CACHE.put(file, hash);
            } catch (IOException e) {
                log.warn("skip file {}", file);
                return null;
            }
        }
        return hash;
    }
}
