package de.lgohlke.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DirWalkerTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void name() throws Exception {
        createFile("a");
        createFile("b/c");

        // action
        Map<Long, Set<Path>> sizeToFileMap = DirWalker.walk(temporaryFolder.getRoot().toPath(),
                                                            0,
                                                            Long.MAX_VALUE);

        assertThat(sizeToFileMap).containsKeys(0L);
        assertThat(sizeToFileMap.get(0L)).hasSize(2);
    }

    private void createFile(String name) throws IOException {
        System.out.println("create " + name);
        String[] splitted = name.split("/");
        if (name.contains("/")) {
            String[] folders = Arrays.copyOfRange(splitted, 0, splitted.length - 1);
            temporaryFolder.newFolder(folders).createNewFile();
        }

        File file = temporaryFolder.newFile(name);
        file.createNewFile();
    }
}
