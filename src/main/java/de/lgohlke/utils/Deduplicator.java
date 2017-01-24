package de.lgohlke.utils;

import de.lgohlke.utils.filter.pair.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

@Slf4j
public class Deduplicator implements Function<Pair, Void> {
    /**
     * @return eleminated
     */
    public boolean eleminate(Pair pair) throws IOException {
        Path p1 = pair.getP1();
        Path p2 = pair.getP2();

        if (!p1.toFile().exists()) {
            log.warn("file missing: {}", p1);
            return false;
        }

        if (!p2.toFile().exists()) {
            log.warn("file missing: {}", p2);
            return false;
        }

        if (Files.isSameFile(p1, p2)) {
            return false;
        }

        Path temporaryName = Paths.get(p2.toAbsolutePath() + ".temp.for.deduplicate");
        Path link = Files.createLink(temporaryName, p1);
        try {
            Files.move(temporaryName, p2, ATOMIC_MOVE);
        } catch (IOException e) {
            link.toFile().delete();
            throw e;
        }
        return true;
    }

    @Override
    public Void apply(Pair pair) {
        try {
            eleminate(pair);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
