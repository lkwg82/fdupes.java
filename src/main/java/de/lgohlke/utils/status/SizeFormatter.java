package de.lgohlke.utils.status;

import java.text.DecimalFormat;

public final class SizeFormatter {
    private SizeFormatter() {
    }

    private final static String[] UNITS = new String[]{"B", "KB", "MB", "GB", "TB"};

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
    }
}
