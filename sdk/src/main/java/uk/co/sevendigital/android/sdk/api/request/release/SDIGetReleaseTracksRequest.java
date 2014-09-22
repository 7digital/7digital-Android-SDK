package uk.co.sevendigital.android.sdk.api.request.release;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.request.abs.SDIAbsXmlRequest;
import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

public final class SDIGetReleaseTracksRequest extends SDIAbsXmlRequest<SDIGetReleaseTracksRequest.Result> {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * constants
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static final String DEFAULT_PERIOD = "week";
	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 500;

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * execute (static)
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param releaseId The search releaseId string.
	 * @return Return the result of the network retrieval.
	 * 
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String releaseId)
			throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {
		return execute(context, consumer, queue, releaseId, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param releaseId The search releaseId string.
	 * @param page Page number of the result set. If not supplied, defaults to 1
	 * @return Return the result of the network retrieval.
	 * 
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String releaseId, int page)
			throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {
		return execute(context, consumer, queue, releaseId, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param releaseId The search releaseId string.
	 * @param page Page number of the result set. If not supplied, defaults to 1
	 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
	 * @return Return the result of the network retrieval.
	 * 
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String releaseId, int page,
			int pageSize) throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {
		return execute(context, consumer, queue, releaseId, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param releaseId The search releaseId string.
	 * @param country 2 letter ISO country code of the country whose artists you would like to search
	 * @param imageSize The requested width of the image in pixels
	 * @return Return the result of the network retrieval.
	 * 
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String releaseId,
			String country, Integer imageSize) throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {

		Serializer serializer = SDICore.getSerializer();

		if (context == null) throw new NullPointerException("context cannot be null");
		if (consumer == null) throw new NullPointerException("consumer cannot be null");
		if (queue == null) queue = SDICore.getQueue(context);
		if (null == releaseId) throw new IllegalArgumentException("releaseId cannot be null");

		String timestamp = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer.toTuple());
		String nonce = SDIOauthHelper.getNonce();

		String url = SDIConstants.ENDPOINT_RELEASE_TRACKS;

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("releaseId", releaseId));
		parameters.add(new Pair<String, String>("oauth_consumer_key", consumer.getKey()));
		parameters.add(new Pair<String, String>("oauth_nonce", nonce));
		parameters.add(new Pair<String, String>("oauth_signature_method", "HMAC-SHA1"));
		parameters.add(new Pair<String, String>("oauth_timestamp", timestamp));

		//optional parameters
		if (null != country) parameters.add(new Pair<String, String>("country", country));
		if (null != imageSize) parameters.add(new Pair<String, String>("page", Integer.toString(imageSize)));

		Collections.sort(parameters, SDIServerUtil.SORT_ALPHABETICAL_BY_KEY);

		// build full url
		url += "?" + SDIServerUtil.buildUrlParameterString(parameters);

		VolleyUtil.CacheEntryRequestParams params = new VolleyUtil.CacheEntryRequestParams(queue, Method.GET, url);
		addUserAgent(params);
		//params.addHeader("Accept", "application/json");//only xml available for this request

		VolleyUtil.CacheEntryResponse<String> response = VolleyUtil.executeStringRequest(params);

		// throw an exception if the response is invalid
		if (response == null || response.getResponse() == null) throw new IllegalStateException("response invalid: " + response);

		Response xmlResponse = parseResponse(serializer, response.getResponse());

		// throw an exception if the response is invalid
		if (xmlResponse == null || xmlResponse.mTracksResult == null || xmlResponse.mTracksResult.mTracks == null) throw new
				IllegalStateException("response " + "invalid");

		List<SDITrack> tracks = new ArrayList<SDITrack>();
		for(XMLTrack track: xmlResponse.mTracksResult.mTracks) {
			tracks.add(new SDITrack(track));
		}

		return new Result(ResultCode.SUCCESS, response.getCacheEntry(), tracks);
	}

	public static Response parseResponse(Serializer serializer, String xmlResponse) throws Exception {
		return serializer.read(Response.class, xmlResponse);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * result
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static final class Result {
		private final/* ResultCode */int mResultCode;
		private final Cache.Entry mCacheEntry;
		private final List<SDITrack> mTracks;

		protected Result(int resultCode) {
			this(resultCode, null, null);
		}

		protected Result(int resultCode, Cache.Entry entry, List<SDITrack> tracks) {
			mResultCode = resultCode;
			mCacheEntry = entry;
			mTracks = tracks;
		}

		public int getResultCode() {
			return mResultCode;
		}

		public Cache.Entry getCacheEntry() {
			return mCacheEntry;
		}

		public List<SDITrack> getTracks() {
			return mTracks;
		}

		public boolean isSuccess() {
			return ResultCode.isSuccess(mResultCode);
		}

		public boolean isFailure() {
			return ResultCode.isFailure(mResultCode);
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * result code
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static class ResultCode extends SDIServerUtil.GenericNetworkResultCode {
		// no specific result codes
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * xml response
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Root(strict = false) public static class Response {
		@Attribute public String status;
		@Element(name = "error", required = false) public Error mError;
		@Element(name = "tracks", required = false) public XMLTracksResult mTracksResult;
	}

	@Root(strict = false) public static class XMLTracksResult {
		@Element(name = "page", required = false) public int mPage;
		@Element(name = "pageSize", required = false) public int mPageSize;
		@Element(name = "totalItems", required = false) public int mTotalItems;
		@ElementList(entry = "track", required = false,inline = true) public List<XMLTrack> mTracks;
	}

}
