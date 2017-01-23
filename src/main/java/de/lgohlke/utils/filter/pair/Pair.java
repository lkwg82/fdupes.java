package de.lgohlke.utils.filter.pair;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.file.Path;

@RequiredArgsConstructor
@Getter
@ToString
public class Pair {
    private final Path p1;
    private final Path p2;

    public boolean same(Pair pair) {
        return pair.p1.equals(p1) && pair.p2.equals(p2) || pair.p1.equals(p2) && pair.p2.equals(p1);
    }
}
