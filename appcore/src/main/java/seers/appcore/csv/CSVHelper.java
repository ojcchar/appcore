package seers.appcore.csv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;

public class CSVHelper {

	public static <T> List<T> readCsv(String filePath, boolean skipHeader, Function<List<String>, T> function)
			throws UnsupportedEncodingException, IOException {

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"),
				csvParser)) {

			boolean headerSkipped = false;

			List<T> data = new ArrayList<>();
			for (List<String> line : csvReader) {

				if (!headerSkipped && skipHeader) {
					headerSkipped = true;
					continue;
				}

				T entry = function.apply(line);
				if (entry != null) {
					data.add(entry);
				}
			}

			return data;
		}
	}

}
