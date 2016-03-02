package seers.appcore.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class HttpHelper {

	private final static HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

	public static String sendGetRequest(String inputUrl) throws IOException {

		URL url = new URL(inputUrl);

		HttpRequest httpRequest = requestFactory.buildGetRequest(new GenericUrl(url));
		HttpResponse response = httpRequest.execute();

		InputStream stream = response.getContent();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
				result.append("\r\n");
			}
			return result.toString();
		}

	}
}
