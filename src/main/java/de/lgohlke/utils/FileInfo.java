package de.lgohlke.utils;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class FileInfo {
    private final Path path;

    int getInode() throws IOException {
        return readAttributeAsInt(path, "unix:ino");
    }

    public int getDevice() throws IOException {
        return readAttributeAsInt(path, "unix:dev");
    }

    int getHardlinks() throws IOException {
        return readAttributeAsInt(path, "unix:nlink");
    }

    private static int readAttributeAsInt(Path path, String attribute) throws IOException {
        return Integer.valueOf(Files.getAttribute(path, attribute).toString());
    }
}
