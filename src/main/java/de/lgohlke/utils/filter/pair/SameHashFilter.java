package de.lgohlke.utils.filter.pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class SameHashFilter implements PairFilter {
    private final static Cache<File, String> CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    @Override
    public boolean select(Pair pair) {
        String h1 = hash(pair.getP1());
        String h2 = hash(pair.getP2());
        return h1.equals(h2);
    }

    private String hash(Path path) {
        File file = path.toFile();
        String hash = CACHE.getIfPresent(file);
        if (hash == null) {
            try {
                hash = com.google.common.io.Files.hash(file, Hashing.md5()).toString();
            } catch (IOException e) {
                log.warn("skip file {}", file);
                hash = "0";
            }
            CACHE.put(file, hash);
        }
        return hash;
    }
}
