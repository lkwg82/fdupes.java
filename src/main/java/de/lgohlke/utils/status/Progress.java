package de.lgohlke.utils.status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Progress {
    private final long totalSize;
    private long processedSize = 0;

    public void reduce(long number) {
        processedSize += number;
    }

    public String status() {
        double percentage = (double) 100 * processedSize / totalSize;
        return String.format("%3.0f%% processed %s", percentage, SizeFormatter.readableFileSize(processedSize));
    }
}
