package uk.co.sevendigital.android.sdk.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

/**
 * An {@link com.android.volley.toolbox.HttpStack HttpStack} implementation which uses OkHttp as its transport.
 */
public class OkHttpStack extends HurlStack {
	private final OkUrlFactory okUrlFactory;

	public OkHttpStack() {
		this(new OkHttpClient());
	}

	public OkHttpStack(OkHttpClient client) {
		if (client == null) {
			throw new NullPointerException("Client must not be null.");
		}
		okUrlFactory = new OkUrlFactory(client);
	}

	@Override protected HttpURLConnection createConnection(URL url) throws IOException {
		return okUrlFactory.open(url);
	}
}
