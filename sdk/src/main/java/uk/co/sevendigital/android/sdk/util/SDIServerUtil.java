package uk.co.sevendigital.android.sdk.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uk.co.sevendigital.android.sdk.core.SDIConstants;

public class SDIServerUtil {
	private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
	private static final int DEFAULT_READ_TIMEOUT = 30000;

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get http client
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get the http client using default params.
	 *
	 * Note: Default params set the default connection and socket timeout.
	 */
	public static HttpClient getHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient(getDefaultParams());
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, SDIConstants.USER_AGENT);
		return client;
	}

	/**
	 * Get the http client using given params.
	 *
	 * Note: If no connection and socket timeouts are provided in the given params, the default values are used: infinite timeout.
	 */
	public static HttpClient getHttpClient(HttpParams params) {
		DefaultHttpClient client = new DefaultHttpClient(params);
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, SDIConstants.USER_AGENT);
		return client;
	}

	/**
	 * Get the http client using given connection manager and params.
	 *
	 * Note: If no connection and socket timeouts are provided in the given params, the default values are used: infinite timeout.
	 */
	public static HttpClient getHttpClient(ClientConnectionManager conman, HttpParams params) {
		HttpClient client = new DefaultHttpClient(conman, params);
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, SDIConstants.USER_AGENT);
		return client;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get default params
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static HttpParams getDefaultParams() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_READ_TIMEOUT);
		return params;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get http get
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static HttpGet getHttpGet(String uri) {
		return getHttpGet(uri, false, false);
	}

	public static HttpGet getHttpGet(String uri, boolean acceptGzipEncoding, boolean spoofUserAgent) {
		HttpGet method = new HttpGet(uri);
		if (acceptGzipEncoding) acceptGzipEncoding(method);
		//		if (spoofUserAgent) spoofUserAgent(method);
		return method;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get http post
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static HttpPost getHttpPost(String uri) {
		return getHttpPost(uri, false, false);
	}

	public static HttpPost getHttpPost(String uri, boolean acceptGzipEncoding, boolean spoofUserAgent) {
		HttpPost method = new HttpPost(uri);
		if (acceptGzipEncoding) acceptGzipEncoding(method);
		//		if (spoofUserAgent) spoofUserAgent(method);
		return method;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * http configuration methods
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static void acceptGzipEncoding(AbstractHttpMessage method) {
		method.setHeader("Accept-Encoding", "gzip");
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * build parameters
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/** Shorthand for building a parameter string used in the url of GET requests; */
	public static String buildUrlParameterString(List<Pair<String, String>> parameters) throws UnsupportedEncodingException {
		return buildParameterString(parameters, "&", false);
	}

	/** Shorthand for building a parameter string used in the body of POST requests */
	public static String buildBodyParameterString(List<Pair<String, String>> parameters) throws UnsupportedEncodingException {
		return buildParameterString(parameters, ",", true);
	}

	/** Builds a parameter string based on the given parameters, separated by given delimiter, and optionally quoted values */
	public static String buildParameterString(List<Pair<String, String>> parameters, String delimiter, boolean quote)
			throws UnsupportedEncodingException {

		if (parameters == null || parameters.isEmpty()) return "";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parameters.size(); i++) {
			String key = parameters.get(i).first;
			String value = parameters.get(i).second;
			// skip 'null' values
			if (value == null) continue;
			String a = URLEncoder.encode(key, "UTF-8");
			String b = URLEncoder.encode(value, "UTF-8");
			builder.append(a + "="); // append key + "="
			if (quote) builder.append("\""); // optionally append quote
			builder.append(b); // append value
			if (quote) builder.append("\""); // optionally append quote
			if (i != parameters.size() - 1) builder.append(delimiter);
		}
		return builder.toString();
	}

	/** Builds a list of name value pairs (suitable for a post body) based on the given parameters */
	public static List<NameValuePair> buildNameValuePairList(List<Pair<String, String>> parameters) throws Exception {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (parameters == null || parameters.isEmpty()) return nameValuePairs;
		for (int i = 0; i < parameters.size(); i++) {
			// skip 'null' values
			if (parameters.get(i).second== null) continue;
			String key = URLEncoder.encode(parameters.get(i).first, "UTF-8");
			String value = URLEncoder.encode(parameters.get(i).second, "UTF-8");
			nameValuePairs.add(new BasicNameValuePair(key, value));
		}
		return nameValuePairs;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * params comparator
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static final Comparator<Pair<String, String>> SORT_ALPHABETICAL_BY_KEY = new Comparator<Pair<String, String>>() {
		@Override public int compare(Pair<String, String> o1, Pair<String, String> o2) {
			return o1.first.compareTo(o2.first);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * generic result code
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static class GenericResultCode {
		public static final int SUCCESS = 101;
		public static final int FAILURE_UNKNOWN = 102;

		public static boolean isSuccess(int code) {
			return code == SUCCESS;
		}

		public static boolean isFailure(int code) {
			return code == FAILURE_UNKNOWN;
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * generic network result code
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static class GenericNetworkResultCode extends GenericResultCode {
		public static final int FAILURE_NETWORK = 103;

		public static boolean isFailure(int code) {
			return GenericResultCode.isFailure(code) || code == FAILURE_NETWORK;
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * server access token
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * The {@link SDIServerUtil.ServerAccessToken} class represents the access token and access token secret
	 * used to pass through in authenticated server requests.
	 *
	 * The {@link SDIServerUtil.ServerAccessToken} class should replace the {@link
	 * Pair} returned from {@code SDIApplicationModel.#getServerAccessTokenTuple()} to improve
	 * readability and reduce errors.
	 */

	public static final class ServerAccessToken implements Serializable, Parcelable {
		private static final long serialVersionUID = 8949643487303750989L;

		private final String mToken;
		private final String mSecret;

		public ServerAccessToken(Pair<String, String> tuple) {
			this(tuple.first, tuple.second);
		}

		public ServerAccessToken(String token, String secret) {
			mToken = token;
			mSecret = secret;
		}

		public String getToken() {
			return mToken;
		}

		public String getSecret() {
			return mSecret;
		}

		/**
		 * Convert the server access token to a tuple containing (token, secret).
		 *
		 * The {@link SDIServerUtil.ServerAccessToken} class replaces the {@link
		 * Pair} returned from {@code SDIApplicationModel.#getServerAccessTokenTuple()}. For methods that
		 * still require a tuple to be sent through, this method provides that bridge.
		 *
		 * @return Return the server access token as a tuple containing (token, secret).
		 */
		public Pair<String, String> toTuple() {
			return new Pair<String, String>(mToken, mSecret);
		}

		@Override public int describeContents() {
			return 0;
		}

		private ServerAccessToken(Parcel in) {
			mToken = in.readString();
			mSecret = in.readString();
		}

		@Override public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mToken);
			dest.writeString(mSecret);
		}

		public static final Creator<ServerAccessToken> CREATOR = new Creator<ServerAccessToken>() {
			public ServerAccessToken createFromParcel(Parcel p) {
				return new ServerAccessToken(p);
			}

			public ServerAccessToken[] newArray(int size) {
				return new ServerAccessToken[size];
			}
		};
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * oauth consumer
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * The {@link SDIServerUtil.OauthConsumer} class represents the details of the oauth consumer for this
	 * device. requests.
	 *
	 * The {@link SDIServerUtil.OauthConsumer} class should replace the {@link
	 * Pair} returned from {@code SDIApplicationModel.#getOauthConsumerTuple()} to improve readability
	 * and reduce errors.
	 */

	public static final class OauthConsumer implements Serializable, Parcelable {
		private static final long serialVersionUID = 5922271796714297224L;

		private final String mKey;
		private final String mSecret;

		public OauthConsumer(Pair<String, String> keySecret) {
			this(keySecret.first, keySecret.second);
		}

		public OauthConsumer(String key, String secret) {
			mKey = key;
			mSecret = secret;
		}

		public String getKey() {
			return mKey;
		}

		public String getSecret() {
			return mSecret;
		}

		/**
		 * Convert the oauth consumer object to a tuple containing (key, secret).
		 *
		 * The {@link SDIServerUtil.OauthConsumer} class replaces the {@link
		 * Pair} returned from {@code SDIApplicationModel.#getOauthConsumerTuple()}. For methods that
		 * still require a tuple to be sent through, this method provides that bridge.
		 *
		 * @return Return the server access token as a tuple containing (token, secret).
		 */
		public Pair<String, String> toTuple() {
			return new Pair<String, String>(mKey, mSecret);
		}

		@Override public int describeContents() {
			return 0;
		}

		private OauthConsumer(Parcel in) {
			mKey = in.readString();
			mSecret = in.readString();
		}

		@Override public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mKey);
			dest.writeString(mSecret);
		}

		public static final Creator<OauthConsumer> CREATOR = new Creator<OauthConsumer>() {
			public OauthConsumer createFromParcel(Parcel p) {
				return new OauthConsumer(p);
			}

			public OauthConsumer[] newArray(int size) {
				return new OauthConsumer[size];
			}
		};
	}

}
