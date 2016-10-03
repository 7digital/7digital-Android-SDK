package uk.co.sevendigital.android.sdk.api.request.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;

import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.api.request.abs.SDIAbsRequest;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.SDIStringUtil;
import uk.co.sevendigital.android.sdk.util.external.MD1HashCalculator;

/**
 * The {@link SDILoginUserRequest} performs the server interaction to log the user directly into the 7digital application. Specific
 * {@link SDILoginUserRequest.LoginConfig} instances can be passed into the execution to perform mock login requests for unit tests.
 */

public class SDILoginUserRequest extends SDIAbsRequest<SDILoginUserRequest.Result> {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * constants
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String LOGIN_CONFIG = "login_config";

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * injections
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * execute
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public Result execute(Context context, Bundle bundle) throws Exception {
		//		super.execute(context, bundle, uiThreadHandler);
		String email = bundle.getString(EMAIL);
		String password = bundle.getString(PASSWORD);
		LoginConfig config = (LoginConfig) bundle.getSerializable(LOGIN_CONFIG);
		Serializer serializer = SDICore.getSerializer();
		return execute(serializer, email, password, config);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * execute (static)
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static Result execute(Serializer serializer, String email, String password, LoginConfig config) throws Exception {

		// throw an exception if the parameters passed in are invalid
		if (email == null) throw new IllegalArgumentException("email cannot be null");
		if (password == null) throw new IllegalArgumentException("password cannot be null");
		if (config == null) throw new IllegalArgumentException("config cannot be null");

		// retrieve the consumer from the config
		Pair<String, String> consumer = config.getRequestOauthConsumer();

		// retrieve the current time from the server
		String serverTime = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer);

		// retrieve the request token from the server
		JsonRequestTokenResponse requestTokenResponse = getRequestToken(SDIServerUtil.getHttpClient(), serializer, consumer,
				config.getRequestShopId(), serverTime);

		// return an unknown response if the request token is invalid
		if (requestTokenResponse == null || requestTokenResponse.mStatus == null || !requestTokenResponse.mStatus.equals("ok")
				|| requestTokenResponse.mToken == null || requestTokenResponse.mSecret == null) return new Result(
				ResultCode.FAILURE_UNKNOWN);

		// construct the query parameters and the signature base string
		String uri = SDIConstants.ENDPOINT_AUTHORIZE;
		StringBuffer queryParameters = new StringBuffer();
		queryParameters.append("oauth_consumer_key=");
		queryParameters.append(URLEncoder.encode(consumer != null ? consumer.first : "", "UTF-8"));
		queryParameters.append("&oauth_nonce=");
		queryParameters.append(SDIOauthHelper.getNonce());
		queryParameters.append("&oauth_signature_method=HMAC-SHA1&oauth_timestamp=");
		queryParameters.append(serverTime);
		queryParameters.append("&password=");
		queryParameters.append(URLEncoder.encode(password, "UTF-8"));
		queryParameters.append("&shopid=" + URLEncoder.encode(config.getRequestShopId(), "UTF-8"));
		queryParameters.append("&token=");
		queryParameters.append(URLEncoder.encode(requestTokenResponse.mToken, "UTF-8"));
		queryParameters.append("&username=");
		queryParameters.append(URLEncoder.encode(email, "UTF-8"));
		String sbs = SDIOauthHelper.getGetSignatureBaseString(uri, queryParameters.toString());

		// construct the request url
		String secret = consumer != null ? consumer.second : "";
		String sig = MD1HashCalculator.calculateRFC2104HMAC(sbs, URLEncoder.encode(secret, "UTF-8") + "&");
		String url = uri + "?" + queryParameters.toString() + "&oauth_signature=" + URLEncoder.encode(sig, "UTF-8");

		// retrieve the login response from the server
		HttpClient client = SDIServerUtil.getHttpClient();
		HttpResponse response = client.execute(SDIServerUtil.getHttpGet(url, true, true));

		// return an unknown response if the login response is invalid
		if (response == null) return new Result(ResultCode.FAILURE_UNKNOWN);

		// return if the consumer key is not authorised to login the user
		if (response.getStatusLine().getStatusCode() == 401) return new Result(ResultCode.FAILURE_CONSUMER_UNAUTHORISED);

		// retrieve the xml string response
		InputStream instream = response.getEntity().getContent();
		Header contentEncoding = response.getFirstHeader("Content-Encoding");
		if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) instream = new GZIPInputStream(instream);
		String xmlResponse = SDIStringUtil.convertInputStreamToString(instream);

		// parse the xml string response
		AuthoriseResponse resp = serializer.read(AuthoriseResponse.class, xmlResponse);

		// return an invalid credentials result if the login request was not successful
		boolean isResponseInvalid = response == null || resp.status == null || !resp.status.toLowerCase(Locale.ENGLISH).equals("ok");
		if (isResponseInvalid) return new Result(ResultCode.FAILURE_INVALID_CREDENTIALS);

		// retrieve the access token from the server (swap the request token for an access token)
		JsonAccessTokenResponse ar = getAccessToken(SDIServerUtil.getHttpClient(), serializer, requestTokenResponse.mToken,
				requestTokenResponse.mSecret, consumer, config.getRequestShopId(), serverTime);

		// return an unknown result if the access token is invalid
		if (ar == null || ar.mStatus == null || !ar.mStatus.equals("ok") || ar.mToken == null || ar.mSecret == null) return new Result(
				ResultCode.FAILURE_UNKNOWN);

		// return a successful result with the returned access token
		return new Result(ResultCode.SUCCESS, ar.mToken, ar.mSecret);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get request token
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static JsonRequestTokenResponse getRequestToken(HttpClient client, Serializer serializer, Pair<String, String> consumer,
			String shopId, String serverTime) throws Exception {

		String queryParameters = getRequestTokenParameters(client, consumer, shopId, serverTime);

		String uri = SDIConstants.ENDPOINT_REQUEST_TOKEN;
		String secret = consumer != null ? consumer.second : "";

		String sig = MD1HashCalculator.calculateRFC2104HMAC(SDIOauthHelper.getGetSignatureBaseString(uri, queryParameters), secret + "&");
		String message = uri + "?" + queryParameters + "&oauth_signature=" + Uri.encode(sig);
		HttpGet getMethod = SDIServerUtil.getHttpGet(message, true, true);

		HttpResponse response = client.execute(getMethod);

		InputStream instream = response.getEntity().getContent();
		Header contentEncoding = response.getFirstHeader("Content-Encoding");
		if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) instream = new GZIPInputStream(instream);
		return serializer.read(JsonRequestTokenResponse.class, instream);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get access token
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static JsonAccessTokenResponse getAccessToken(HttpClient client, Serializer serializer, String requestToken,
			String requestTokenSecret, Pair<String, String> consumer, String shopId, String serverTime) throws Exception {

		String queryParameters = getAccessTokenParameters(client, requestToken, consumer, shopId, serverTime);

		String uri = SDIConstants.ENDPOINT_ACCESS_TOKEN;
		String sbs = SDIOauthHelper.getGetSignatureBaseString(uri, queryParameters);
		String consumerSecret = Uri.encode(consumer != null ? consumer.second : "");
		String tokenSecret = Uri.encode(requestTokenSecret);
		String sig = MD1HashCalculator.calculateRFC2104HMAC(sbs, consumerSecret + "&" + tokenSecret);
		String message = uri + "?" + queryParameters + "&oauth_signature=" + Uri.encode(sig);

		// Fetch request token
		HttpGet getMethod = SDIServerUtil.getHttpGet(message, true, true);
		HttpResponse response = client.execute(getMethod);

		InputStream instream = response.getEntity().getContent();
		Header contentEncoding = response.getFirstHeader("Content-Encoding");
		if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) instream = new GZIPInputStream(instream);

		return serializer.read(JsonAccessTokenResponse.class, instream);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get request token parameters
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static String getRequestTokenParameters(HttpClient client, Pair<String, String> consumer, String shopId, String serverTime)
			throws UnsupportedEncodingException {

		StringBuffer sb = new StringBuffer();
		sb.append("oauth_consumer_key=");
		sb.append(consumer != null ? consumer.first : "");
		sb.append("&oauth_nonce=");
		sb.append(SDIOauthHelper.getNonce());
		sb.append("&oauth_signature_method=HMAC-SHA1&oauth_timestamp=");
		sb.append(serverTime);
		sb.append("&shopid=" + URLEncoder.encode(shopId, "UTF-8"));
		return sb.toString();
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get access token parameters
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static String getAccessTokenParameters(HttpClient client, String requestToken, Pair<String, String> consumer, String shopId,
			String serverTime) throws UnsupportedEncodingException {

		StringBuffer sb = new StringBuffer();
		sb.append("oauth_consumer_key=");
		try {
			sb.append(URLEncoder.encode(consumer != null ? consumer.first : "", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&oauth_nonce=");
		sb.append(SDIOauthHelper.getNonce());
		sb.append("&oauth_signature_method=HMAC-SHA1&oauth_timestamp=");
		sb.append(serverTime);
		sb.append("&oauth_token=");
		sb.append(requestToken);
		sb.append("&shopid=" + URLEncoder.encode(shopId, "UTF-8"));
		return sb.toString();
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * result
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static class Result {
		private final ResultCode mResultCode;
		private final String mAuthorisedAccessToken;
		private final String mAuthorisedAccessTokenSecret;

		private Result(ResultCode code) {
			this(code, null, null);
		}

		private Result(ResultCode code, String authorisedAccessToken, String authorisedAccessTokenSecret) {
			mResultCode = code;
			mAuthorisedAccessToken = authorisedAccessToken;
			mAuthorisedAccessTokenSecret = authorisedAccessTokenSecret;
		}

		public ResultCode getResultCode() {
			return mResultCode;
		}

		public SDIServerUtil.ServerAccessToken getAuthorisedAccessToken() {
			if (mAuthorisedAccessToken == null) return null;
			return new SDIServerUtil.ServerAccessToken(mAuthorisedAccessToken, mAuthorisedAccessTokenSecret);
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * result code
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	// @formatter:off

	public static enum ResultCode {
		SUCCESS,									// the login was successful
		FAILURE_INVALID_CREDENTIALS,				// the login failed due to invalid credentials (username/password)
		FAILURE_NETWORK, 							// the login failed due to network connectivity
		FAILURE_UNKNOWN,							// the login failed for an unknown reason
		FAILURE_CONSUMER_UNAUTHORISED				// the login failed because the consumer (key) is unauthorised
	}

	// @formatter:on

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * authorise response
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Root(strict = false) private static class AuthoriseResponse {
		@Attribute private String status;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * login config
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static interface LoginConfig extends Serializable {

		/** Get the id of the shop being logged in to for the request. */
		String getRequestShopId();

		/** Get the oauth consumer details to use for the request. */
		Pair<String, String> getRequestOauthConsumer();
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * json request token response
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Root(strict = false) private static final class JsonRequestTokenResponse {
		@Attribute(name = "status") private String mStatus;
		@Element(name = "oauth_token") @Path("oauth_request_token") private String mToken;
		@Element(name = "oauth_token_secret") @Path("oauth_request_token") private String mSecret;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * json access token response
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Root(strict = false) private static final class JsonAccessTokenResponse {
		@Attribute(name = "status") private String mStatus;
		@Element(name = "oauth_token") @Path("oauth_access_token") private String mToken;
		@Element(name = "oauth_token_secret") @Path("oauth_access_token") private String mSecret;
	}
}
