package seers.appcore.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;

public class CSVHelper {

	public static <T> List<T> readCsv(String filePath, boolean skipHeader, Function<List<String>, T> entryFunction)
			throws UnsupportedEncodingException, IOException {
		return readCsv(new File(filePath), skipHeader, entryFunction);
	}

	public static <T> List<T> readCsv(File file, boolean skipHeader, Function<List<String>, T> entryFunction)
			throws UnsupportedEncodingException, IOException {

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
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
			Function<T, List<String>> entryFunction) throws IOException {
		writeCsv(new File(filePath), header, data, entryPrefix, entryFunction);
	}

	public static <T> void writeCsv(File file, List<String> header, List<T> data, List<String> entryPrefix,
			Function<T, List<String>> entryFunction) throws IOException {

		try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(file)).separator(';').build()) {

			if (header != null) {
				writer.writeNext(header);
			}

			for (T t : data) {

				List<String> nextLine = new ArrayList<>();
				if (entryPrefix != null) {
					nextLine.addAll(entryPrefix);
				}

				List<String> entrySuffix = entryFunction.apply(t);
				nextLine.addAll(entrySuffix);

				writer.writeNext(nextLine);
			}

		}

	}
}
