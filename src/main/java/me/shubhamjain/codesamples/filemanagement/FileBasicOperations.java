package me.shubhamjain.codesamples.filemanagement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileBasicOperations {

    public String readFile(String fileName) {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFileWithAppend(String fileName, String content) {
        try {
            Files.writeString(Path.of(fileName), content, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFileWithTruncate(String fileName, String content) {
        try {
            // By default, option is truncate
            Files.writeString(Path.of(fileName), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
