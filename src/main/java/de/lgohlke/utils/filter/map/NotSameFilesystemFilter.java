package de.lgohlke.utils.filter.map;

import com.google.common.annotations.VisibleForTesting;
import de.lgohlke.utils.FileInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class NotSameFilesystemFilter implements MapFilter {

    private final int deviceNo;

    public NotSameFilesystemFilter(Path rootPath) {
        deviceNo = getDeviceNo(rootPath);
    }

    @Override
    public Map<Long, Set<Path>> filter(Map<Long, Set<Path>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .map(this::filterOutNotSameDevice)
                            .filter(entry -> entry.getValue().size() > 1)
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Long, Set<Path>> filterOutNotSameDevice(Map.Entry<Long, Set<Path>> entry) {
        Set<Path> filteredList = entry.getValue()
                                      .stream()
                                      .filter(path -> getDeviceNo(path) == deviceNo)
                                      .collect(toSet());

        return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), filteredList);
    }

    @VisibleForTesting
    int getDeviceNo(Path path) {
        FileInfo fileInfo = new FileInfo(path);
        try {
            return fileInfo.getDevice();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}