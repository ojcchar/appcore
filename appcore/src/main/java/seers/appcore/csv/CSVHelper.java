package seers.appcore.csv;

import net.quux00.simplecsv.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "WeakerAccess"})
public class CSVHelper {

    public static final char DEFAULT_SEPARATOR = ';';
    public static final String DEFAULT_CHARSET = "Cp1252";

    public static List<List<String>> readCsv(String filePath, boolean skipHeader)
            throws IOException {
        return readCsv(new File(filePath), skipHeader, Function.identity(), DEFAULT_SEPARATOR, DEFAULT_CHARSET);
    }

    public static List<List<String>> readCsv(String filePath, boolean skipHeader, char separator)
            throws IOException {
        return readCsv(new File(filePath), skipHeader, Function.identity(), separator, DEFAULT_CHARSET);
    }

    public static <T> List<T> readCsv(String filePath, boolean skipHeader, Function<List<String>, T> entryFunction,
                                      char separator) throws IOException {
        return readCsv(new File(filePath), skipHeader, entryFunction, separator, DEFAULT_CHARSET);
    }


    public static List<List<String>> readCsv(File file, boolean skipHeader, char separator, String charSet)
            throws IOException {
        return readCsv(file, skipHeader, Function.identity(), separator, charSet);
    }


    public static <T> List<T> readCsv(File file, boolean skipHeader, Function<List<String>, T> entryFunction,
                                      char separator, String charSet) throws IOException {

        CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(separator).build();
        try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(file),
                charSet),
                csvParser)) {

            boolean headerSkipped = false;

            List<T> data = new ArrayList<>();
            for (List<String> line : csvReader) {

                if (!headerSkipped && skipHeader) {
                    headerSkipped = true;
                    continue;
                }

                T entry = entryFunction.apply(line);
                if (entry != null) {
                    data.add(entry);
                }
            }

            return data;
        }
    }

    public static void writeCSV(Path filePath, Stream<Stream<Object>> rows) throws IOException {
        try (CsvWriter writer = getWriter(filePath.toFile(), DEFAULT_SEPARATOR, false)) {
            rows.forEachOrdered(s ->
                    writer.writeNext(
                            s.map(String::valueOf)
                                    .collect(Collectors.toList())
                    )
            );
        }
    }

    public static <T> void writeCsv(String filePath, List<String> header, List<T> data, List<String> entryPrefix,
                                    Function<T, List<String>> entryFunction, char separator) throws IOException {
        writeCsv(new File(filePath), header, data, entryPrefix, entryFunction, separator);
    }

    public static <T> void writeCsv(File file, List<String> header, Collection<T> data, List<String> entryPrefix,
                                    Function<T, List<String>> entryFunction, char separator) throws IOException {

        try (CsvWriter writer = getWriter(file, separator)) {
            writeCsv(writer, header, data, entryPrefix, entryFunction);
        }

    }

    public static <T> void writeCsv(CsvWriter writer, List<String> header, Collection<T> data, List<String> entryPrefix,
                                    Function<T, List<String>> entryFunction) {
        if (header != null) {
            writer.writeNext(header);
        }

        for (T t : data) {

            List<String> entrySuffix = entryFunction.apply(t);
            if (entrySuffix == null) {
                continue;
            }

            List<String> nextLine = new ArrayList<>();
            if (entryPrefix != null) {
                nextLine.addAll(entryPrefix);
            }

            nextLine.addAll(entrySuffix);

            writer.writeNext(nextLine);
        }
    }

    private static CsvWriter getWriter(File file, char separator) throws IOException {
        return getWriter(file, separator, false);
    }

    public static CsvWriter getWriter(File file, char separator, boolean append) throws IOException {
        StandardOpenOption option = append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING;

        BufferedWriter writer =
                Files.newBufferedWriter(file.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, option);

        return new CsvWriterBuilder(writer).separator(separator)
                .escapeChar(CsvWriter.NO_ESCAPE_CHARACTER).build();
    }

    public static <T> void writeCsvMultiple(File file, List<String> header, Collection<T> data,
                                            List<String> entryPrefix, Function<T, List<List<String>>> entryFunction,
                                            char separator)
            throws IOException {

        try (CsvWriter writer = getWriter(file, separator)) {
            writeCsVMultiple(writer, header, data, entryPrefix, entryFunction);
        }

    }

    public static <T> void writeCsVMultiple(CsvWriter writer, List<String> header, Collection<T> data,
                                            List<String> entryPrefix, Function<T, List<List<String>>> entryFunction) {

        if (header != null) {
            writer.writeNext(header);
        }

        for (T t : data) {

            List<List<String>> entries = entryFunction.apply(t);

            if (entries == null) {
                continue;
            }

            List<List<String>> allLines = new ArrayList<>();

            for (List<String> entry : entries) {
                List<String> nextLine = new ArrayList<>();
                if (entryPrefix != null) {
                    nextLine.addAll(entryPrefix);
                }
                nextLine.addAll(entry);

                allLines.add(nextLine);
            }
            writer.writeAll(allLines);

        }
    }
}
