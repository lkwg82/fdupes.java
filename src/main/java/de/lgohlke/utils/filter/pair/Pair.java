package de.lgohlke.utils.filter.pair;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
@Getter
public class Pair {
    private final Path p1;
    private final Path p2;
}
