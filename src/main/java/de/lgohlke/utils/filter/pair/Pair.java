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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        return pair.p1.equals(p1) && pair.p2.equals(p2) || pair.p1.equals(p2) && pair.p2.equals(p1);
    }

    @Override
    public int hashCode() {
        int h1 = p1.hashCode();
        int h2 = p2.hashCode();

        return (h1 < h2) ? hash(h1, h2) : hash(h2, h1);
    }

    private int hash(int lower, int higher) {
        int result = lower;
        result = 31 * result + higher;
        return result;
    }
}
