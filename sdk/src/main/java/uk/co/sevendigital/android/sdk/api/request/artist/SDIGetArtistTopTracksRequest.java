package uk.co.sevendigital.android.sdk.api.request.artist;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.object.SDITrack;
import uk.co.sevendigital.android.sdk.api.request.abs.SDIAbsJsonRequest;
import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

public final class SDIGetArtistTopTracksRequest extends SDIAbsJsonRequest<SDIGetArtistTopTracksRequest.Result> {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * constants
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

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
	 * @param artistId The unique identifier of the artist.
	 * @return Return the result of the network retrieval.
	 * 
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String artistId)
			throws IOException, ExecutionException, InterruptedException{
		return execute(context, consumer, queue, artistId, 1, DEFAULT_PAGE_SIZE, null, null, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param artistId The unique identifier of the artist.
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String artistId, int page)
			throws IOException, ExecutionException, InterruptedException{
		return execute(context, consumer, queue, artistId, page, DEFAULT_PAGE_SIZE, null, null, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param artistId The unique identifier of the artist.
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String artistId, int page,
			int pageSize) throws IOException, ExecutionException, InterruptedException {
		return execute(context, consumer, queue, artistId, page, pageSize, null, null, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param artistId The unique identifier of the artist.
	 * @param streamable If provided search results will contain only artists that can/cannot be streamed
	 * @param licensorId If provided search results will contain only artists from this licensor. A minus sign prefix excludes artists from
	 *        that licensor (e.g. -5)
	 * @param country 2 letter ISO country code of the country whose artists you would like to search
	 * @param page Page number of the result set. If not supplied, defaults to 1
	 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String artistId, int page,
			int pageSize, Boolean streamable, String licensorId, String country, Integer imageSize) throws IOException,
			ExecutionException, InterruptedException {

		ObjectMapper mapper = SDICore.getMapper();

		if (context == null) throw new NullPointerException("context cannot be null");
		if (consumer == null) throw new NullPointerException("consumer cannot be null");
		if (queue == null) queue = SDICore.getQueue(context);
		if (null == artistId) throw new IllegalArgumentException("query cannot be null");
		if (page < 1) throw new IllegalArgumentException("page one-indexed, invalid page: " + page);
		if (pageSize < 1) throw new IllegalArgumentException("page size invalid: " + pageSize);
		if (pageSize > MAX_PAGE_SIZE) throw new IllegalArgumentException("page size: " + pageSize + " exceeds maximum: " + MAX_PAGE_SIZE);

		String timestamp = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer.toTuple());
		String nonce = SDIOauthHelper.getNonce();

		String url = SDIConstants.ENDPOINT_ARTIST_TOPTRACKS;

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("artistId", artistId));
		parameters.add(new Pair<String, String>("oauth_consumer_key", consumer.getKey()));
		parameters.add(new Pair<String, String>("oauth_nonce", nonce));
		parameters.add(new Pair<String, String>("oauth_signature_method", "HMAC-SHA1"));
		parameters.add(new Pair<String, String>("oauth_timestamp", timestamp));
		parameters.add(new Pair<String, String>("pageSize", Integer.toString(pageSize)));
		parameters.add(new Pair<String, String>("page", Integer.toString(page)));

		//optional parameters
		if (null != streamable) parameters.add(new Pair<String, String>("streamable", Boolean.toString(streamable)));
		if (null != licensorId) parameters.add(new Pair<String, String>("licensorId", licensorId));
		if (null != country) parameters.add(new Pair<String, String>("country", country));
		if (null != imageSize) parameters.add(new Pair<String, String>("page", Integer.toString(imageSize)));

		Collections.sort(parameters, SDIServerUtil.SORT_ALPHABETICAL_BY_KEY);

		// build full url
		url += "?" + SDIServerUtil.buildUrlParameterString(parameters);

		VolleyUtil.CacheEntryRequestParams params = new VolleyUtil.CacheEntryRequestParams(queue, Method.GET, url);
		params.addHeader("Accept", "application/json");
		addUserAgent(params);

		VolleyUtil.CacheEntryResponse<String> response = VolleyUtil.executeStringRequest(params);

		// throw an exception if the response is invalid
		if (response == null || response.getResponse() == null) throw new IllegalStateException("response invalid: " + response);

		// parse the string response into json
		JsonResponse jsonResponse = mapper.readValue(response.getResponse(), JsonResponse.class);

		// return if the response is invalid
		if (jsonResponse == null || !jsonResponse.mStatus.equals("ok") || jsonResponse.mTracks == null
				|| jsonResponse.mTracks.mItems == null) return new Result(ResultCode.FAILURE_UNKNOWN);

		List<SDITrack> tracks = new ArrayList<SDITrack>();
		for (JsonTrack track : jsonResponse.mTracks.mItems) {
			tracks.add(new SDITrack(track));
		}

		return new Result(ResultCode.SUCCESS, response.getCacheEntry(), tracks);
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
	 * json response
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static final class JsonResponse {
		@JsonProperty("status") private String mStatus;
		@JsonProperty("version") private String mVersion;
		@JsonProperty("tracks") private JsonArtistTracks mTracks;
	}

	private static final class JsonArtistTracks {
		@JsonProperty("page") private int mPage;
		@JsonProperty("pageSize") private int mPageSize;
		@JsonProperty("totalItems") private int mTotalItems;
		@JsonProperty("track") private List<JsonTrack> mItems;
	}
}
