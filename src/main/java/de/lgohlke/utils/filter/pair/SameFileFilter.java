package de.lgohlke.utils.filter.pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

public class SameFileFilter implements PairFilter {
    private final static Cache<Pair, Boolean> CACHE = CacheBuilder.newBuilder().maximumSize(10000).build();

    @Override
    public boolean select(Pair pair) {
        try {
            return CACHE.get(pair, () -> !Files.isSameFile(pair.getP1(), pair.getP2()));
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }
}
