package me.shubhamjain.codesamples.filemanagement;

public interface FileOperations {
    String readFile(String filename);

    // I wrote two methods like below at first for append and truncate
    // But it is not a good pattern
    // This way I will end up with multiple write methods for different options
    // What should be my design pattern then ?
    // Learn from writeFile method itself ? Pass an enumType of Option.
    void writeFileWithAppend(String filename, String content);

    void writeFileWithTruncate(String filename, String content);
}
