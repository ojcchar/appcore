package seers.appcore.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;

public class CSVHelper {

	public static final char DEFAULT_SEPARATOR = ';';

	public static List<List<String>> readCsv(String filePath, boolean skipHeader, char separator)
			throws UnsupportedEncodingException, IOException {
		Function<List<String>, List<String>> entryFunction = (entry) -> {
			return entry;
		};
		return readCsv(new File(filePath), skipHeader, entryFunction, separator);
	}

	public static <T> List<T> readCsv(String filePath, boolean skipHeader, Function<List<String>, T> entryFunction,
			char separator) throws UnsupportedEncodingException, IOException {
		return readCsv(new File(filePath), skipHeader, entryFunction, separator);
	}

	public static <T> List<T> readCsv(File file, boolean skipHeader, Function<List<String>, T> entryFunction,
			char separator) throws UnsupportedEncodingException, IOException {

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(separator).build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(file), "Cp1252"),
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
		return new CsvWriterBuilder(new FileWriter(file, append)).separator(separator)
				.escapeChar(CsvWriter.NO_ESCAPE_CHARACTER).build();
	}

	public static <T> void writeCsvMultiple(File file, List<String> header, Collection<T> data,
			List<String> entryPrefix, Function<T, List<List<String>>> entryFunction, char separator)
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
