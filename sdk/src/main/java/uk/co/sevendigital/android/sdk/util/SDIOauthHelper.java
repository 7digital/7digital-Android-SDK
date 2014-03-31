package uk.co.sevendigital.android.sdk.util;

import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.util.external.MD1HashCalculator;
import uk.co.sevendigital.android.sdk.util.external.PercentEscaper;

public class SDIOauthHelper {
	private static final PercentEscaper percentEncoder = new PercentEscaper("-._~", false);
	private static final Random sRandom = new Random();

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get nonce
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String getNonce() {
		return Long.toString(sRandom.nextLong());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get server time
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get the time according to the server through a network request.
	 *
	 * @return Return the current time if the server response cannot be parsed.
	 */
	public static String getServerTime(HttpClient client, Pair<String, String> consumer) {
		return Long.toString(getServerDate(client, consumer).getTime() / 1000l);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get server date
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get the date according to the server through a network request.
	 *
	 * @return Return the current time if the server response cannot be parsed.
	 */
	private static Date getServerDate(HttpClient client, Pair<String, String> consumer) {
		String key = consumer != null ? consumer.first : "";
		String getServerTimeUri = SDIConstants.ENDPOINT_STATUS + "?oauth_consumer_key=" + key;
		HttpGet httpGet = SDIServerUtil.getHttpGet(getServerTimeUri);
		try {
			HttpResponse response = client.execute(httpGet);
			String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
			Integer responseStart = responseBody.indexOf("<serverTime>");
			Integer responseEnd = responseBody.indexOf("</serverTime>", responseStart);
			String responseTimeString = responseBody.substring(responseStart + 12, responseEnd);
			DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
			return new Date(parser.parseMillis(responseTimeString));
		} catch (Exception e) {
			Log.e(SDIConstants.TAG, "error parsing server time", e);
			return new Date();
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get get signature base string
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String getGetSignatureBaseString(String uri, String queryParameters) {
		return getMethodSignatureBaseString(Request.Method.GET, uri, queryParameters);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get post signature base string
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String getPostSignatureBaseString(String uri, String queryParameters) {
		return getMethodSignatureBaseString(Request.Method.POST, uri, queryParameters);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get put signature base string
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String getPutSignatureBaseString(String uri, String queryParameters) {
		return getMethodSignatureBaseString(Request.Method.PUT, uri, queryParameters);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get delete signature base string
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String getDeleteSignatureBaseString(String uri, String queryParameters) {
		return getMethodSignatureBaseString(Request.Method.DELETE, uri, queryParameters);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get method signature base string
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get the signature base string (used in oauth transactions) for the given request.
	 *
	 * @param volleyRequestMethod The method type. Taken from {@link Request.Method}.
	 * @param uri The uri (excluding appended query parameters).
	 * @param queryParameters The query parameters to append to the uri.
	 *
	 * @return Return the signature base string.
	 */
	public static String getMethodSignatureBaseString(int volleyRequestMethod, String uri, String queryParameters) {
		return getRequestMethodName(volleyRequestMethod) + "&" + percentEncoder.escape(uri) + "&" + percentEncoder.escape(queryParameters);
	}

	/**
	 * Get the name of the http request method from the given method type.
	 *
	 * The following method names are supported: GET, POST, PUT, DELETE.
	 *
	 * @param volleyRequestMethod The method type. Taken from {@link Request.Method}.
	 *
	 * @return Return the name of the http request method.
	 */
	private static String getRequestMethodName(int volleyRequestMethod) {
		if (volleyRequestMethod == Request.Method.GET) return "GET";
		if (volleyRequestMethod == Request.Method.POST) return "POST";
		if (volleyRequestMethod == Request.Method.PUT) return "PUT";
		if (volleyRequestMethod == Request.Method.DELETE) return "DELETE";
		throw new IllegalArgumentException("unknown method: " + volleyRequestMethod);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * generate get oauth signature
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String generateGetOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret)
			throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.GET, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, null);
	}

	public static String generateGetOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret,
			String accessTokenSecret) throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.GET, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, accessTokenSecret);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * generate post oauth signature
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String generatePostOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret)
			throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.POST, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, null);
	}

	public static String generatePostOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret,
			String accessTokenSecret) throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.POST, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, accessTokenSecret);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * generate put oauth signature
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String generatePutOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret)
			throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.PUT, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, null);
	}

	public static String generatePutOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret,
			String accessTokenSecret) throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.PUT, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, accessTokenSecret);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * generate delete oauth signature
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String generateDeleteOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret)
			throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.DELETE, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, null);
	}

	public static String generateDeleteOauthSignature(String url, List<Pair<String, String>> queryParameters, String consumerSecret,
			String accessTokenSecret) throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(Request.Method.DELETE, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, accessTokenSecret);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * generate method oauth signature
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Generates an Oauth signature for the given request based on the url, query parameters, consumer secret and access token secret.
	 *
	 * @param volleyRequestMethod The method type. Taken from {@link Request.Method}.
	 * @param url The url (excluding query parameters).
	 * @param queryParameters The query parameters to append to the uri.
	 * @param consumerSecret The consumer secret.
	 * @param accessTokenSecret The access token secret, or null if no access token secret is required.
	 *
	 * @throws java.io.UnsupportedEncodingException
	 * @throws java.security.SignatureException
	 */
	private static String generateMethodOauthSignature(int volleyRequestMethod, String url, List<Pair<String, String>> queryParameters,
			String consumerSecret, String accessTokenSecret) throws SignatureException, UnsupportedEncodingException {
		return generateMethodOauthSignature(volleyRequestMethod, url, SDIServerUtil.buildUrlParameterString(queryParameters),
				consumerSecret, accessTokenSecret);
	}

	/**
	 * Generates an Oauth signature for the given request based on the url, query parameters, consumer secret and access token secret.
	 *
	 * @param volleyRequestMethod The method type. Taken from {@link Request.Method}.
	 * @param url The url (excluding query parameters).
	 * @param queryParameters The query parameters to append to the uri.
	 * @param consumerSecret The consumer secret.
	 * @param accessTokenSecret The access token secret, or null if no access token secret is required.
	 *
	 * @throws java.io.UnsupportedEncodingException
	 * @throws java.security.SignatureException
	 */
	private static String generateMethodOauthSignature(int volleyRequestMethod, String url, String queryParameters, String consumerSecret,
			String accessTokenSecret) throws SignatureException, UnsupportedEncodingException {
		String sbs = SDIOauthHelper.getMethodSignatureBaseString(volleyRequestMethod, url, queryParameters);
		return MD1HashCalculator.calculateRFC2104HMAC(sbs, consumerSecret,
				accessTokenSecret != null ? accessTokenSecret : "");
	}

}
