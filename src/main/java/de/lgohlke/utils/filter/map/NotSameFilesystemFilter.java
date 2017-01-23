package de.lgohlke.utils.filter.map;

import com.google.common.annotations.VisibleForTesting;
import de.lgohlke.utils.FileInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class NotSameFilesystemFilter implements MapFilter {

    private final int deviceNo;

    public NotSameFilesystemFilter(Path rootPath) {
        deviceNo = getDeviceNo(rootPath);
    }

    @Override
    public Map<Long, List<Path>> filter(Map<Long, List<Path>> sizeToFileMap) {
        return sizeToFileMap.entrySet()
                            .stream()
                            .map(this::filterOutNotSameDevice)
                            .filter(entry -> entry.getValue().size() > 1)
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Long, List<Path>> filterOutNotSameDevice(Map.Entry<Long, List<Path>> entry) {
        List<Path> filteredList = entry.getValue()
                                         .stream()
                                         .filter(path -> getDeviceNo(path) == deviceNo)
                                         .collect(toList());

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