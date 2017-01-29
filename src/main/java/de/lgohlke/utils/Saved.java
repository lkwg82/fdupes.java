package de.lgohlke.utils;

import de.lgohlke.utils.status.SizeFormatter;
import lombok.Getter;

import java.util.concurrent.atomic.LongAdder;

@Getter
class Saved {
    private final LongAdder savedBytes = new LongAdder();
    private final LongAdder createdLinks = new LongAdder();

    @Override
    public String toString() {
        return "Saved{" +
                "savedBytes=" + SizeFormatter.readableFileSize(savedBytes.longValue()) +
                ", createdLinks=" + createdLinks +
                '}';
    }
}
