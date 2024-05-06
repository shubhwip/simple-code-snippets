package me.shubhamjain.codesamples.filemanagement;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For code review on
 * https://codereview.stackexchange.com/questions/287207/add-offset-to-all-filenames/287214
 */
public class FileNameManipulationDirectoryTraversal {
    record Filename(File file, String prefix, int number, String ending) {
    }

    private static final String regex = "(.*)(\\d+)\\.([^.]*)";
    private static final Pattern pattern = Pattern.compile(regex);
    private static final Comparator<Filename> comparator = Comparator.comparingInt(Filename::number);
    public static void shiftFilenamesOffset(final File dir, final int offset) {
        Comparator<Filename> filenameComparator = offset <= 0 ? comparator : comparator.reversed();
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .map(f -> {
                    Matcher mat = pattern.matcher(f.getName());
                    return mat.find() ? new Filename(f, mat.group(1), Integer.parseInt(mat.group(2)), mat.group(3)) : null;
                })
                .filter(Objects::nonNull)
                .sorted(filenameComparator)
                .forEach(fn -> {
                    File nf = new File(fn.file().getParent(), fn.prefix() + (fn.number() + offset) + "." + fn.ending());
                    System.out.println(fn + " -> " + nf + " -> " + fn.file().renameTo(nf));
                });
    }

    public static void main(final String[] args) {
        shiftFilenamesOffset(new File("test1"), -3);
    }
}
