package uk.co.sevendigital.android.sdk.api.request.release;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.request.abs.SDIAbsJsonRequest;
import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;
import uk.co.sevendigital.android.sdk.api.object.SDISearchResults;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

public final class SDIReleaseSearchRequest extends SDIAbsJsonRequest<SDIReleaseSearchRequest.Result> {

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
	 * @param query The search query string.
	 * @return Return the result of the network retrieval.
	 * 
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String query)
			throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {
		return execute(context, consumer, queue, query, 1, DEFAULT_PAGE_SIZE, null, null, null, null, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param query The search query string.
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String query, int page)
			throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {
		return execute(context, consumer, queue, query, page, DEFAULT_PAGE_SIZE, null, null, null, null, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param query The search query string.
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String query, int page,
			int pageSize) throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {
		return execute(context, consumer, queue, query, page, pageSize, null, null, null, null, null, null);
	}

	/**
	 * Get the current charts for tracks
	 * 
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 * 
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param query The search query string.
	 * @param page Page number of the result set. If not supplied, defaults to 1
	 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
	 * @param type If specified results are filtered by given release type (multiple types separated by commas can be passed).
	 * @param labelId If provided search results will only contain releases from this label.
	 * @param streamable If provided search results will contain only artists that can/cannot be streamed
	 * @param licensorId If provided search results will contain only artists from this licensor. A minus sign prefix excludes artists from
	 *        that licensor (e.g. -5)
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, String query, int page,
			int pageSize, String type, String streamable, String licensorId, String labelId, String country, Integer imageSize)
			throws UnsupportedEncodingException, ExecutionException, InterruptedException, Exception {

		ObjectMapper mapper = SDICore.getMapper();

		if (context == null) throw new NullPointerException("context cannot be null");
		if (consumer == null) throw new NullPointerException("consumer cannot be null");
		if (queue == null) queue = SDICore.getQueue(context);
		if (null == query) throw new IllegalArgumentException("query cannot be null");
		if (page < 1) throw new IllegalArgumentException("page one-indexed, invalid page: " + page);
		if (pageSize < 1) throw new IllegalArgumentException("page size invalid: " + pageSize);
		if (pageSize > MAX_PAGE_SIZE) throw new IllegalArgumentException("page size: " + pageSize + " exceeds maximum: " + MAX_PAGE_SIZE);

		String timestamp = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer.toTuple());
		String nonce = SDIOauthHelper.getNonce();

		String url = SDIConstants.ENDPOINT_RELEASE_SEARCH;

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("q", query));
		parameters.add(new Pair<String, String>("oauth_consumer_key", consumer.getKey()));
		parameters.add(new Pair<String, String>("oauth_nonce", nonce));
		parameters.add(new Pair<String, String>("oauth_signature_method", "HMAC-SHA1"));
		parameters.add(new Pair<String, String>("oauth_timestamp", timestamp));
		parameters.add(new Pair<String, String>("pageSize", Integer.toString(pageSize)));
		parameters.add(new Pair<String, String>("page", Integer.toString(page)));

		//optional parameters
		if (null != type) parameters.add(new Pair<String, String>("type", type));
		if (null != streamable) parameters.add(new Pair<String, String>("streamable", streamable));
		if (null != licensorId) parameters.add(new Pair<String, String>("licensorId", licensorId));
		if (null != labelId) parameters.add(new Pair<String, String>("labelId", labelId));
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
		//
		// return if the response is invalid
		if (jsonResponse == null || !jsonResponse.mStatus.equals("ok") || jsonResponse.mSearchResult == null) return new Result(
				ResultCode.FAILURE_UNKNOWN);

		SDISearchResults<SDIRelease> artist = new SDISearchResults<SDIRelease>(jsonResponse.mSearchResult);

		return new Result(ResultCode.SUCCESS, response.getCacheEntry(), artist);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * result
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static final class Result {
		private final/* ResultCode */int mResultCode;
		private final Cache.Entry mCacheEntry;
		private final SDISearchResults<SDIRelease> mSearchResult;

		protected Result(int resultCode) {
			this(resultCode, null, null);
		}

		protected Result(int resultCode, Cache.Entry entry, SDISearchResults<SDIRelease> artist) {
			mResultCode = resultCode;
			mCacheEntry = entry;
			mSearchResult = artist;
		}

		public int getResultCode() {
			return mResultCode;
		}

		public Cache.Entry getCacheEntry() {
			return mCacheEntry;
		}

		public SDISearchResults<SDIRelease> getSearchResult() {
			return mSearchResult;
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
		@JsonProperty("searchResults") private SearchResult mSearchResult;
	}

	private static final class SearchResult implements SDISearchResults.Composition<SDIRelease> {
		@JsonProperty("page") private int mPage;
		@JsonProperty("pageSize") private int mPageSize;
		@JsonProperty("totalItems") private int mTotalItems;
		@JsonProperty("searchResult") private List<JsonReleaseSearchResult> mSearchResults;

		@Override public int getPage() {
			return mPage;
		}

		@Override public int getTotalItems() {
			return mTotalItems;
		}

		@Override public int getPageSize() {
			return mPageSize;
		}

		@Override public List<SDISearchResults.SDISearchResult<SDIRelease>> getResults() {
			if (null == mSearchResults) return null;

			List<SDISearchResults.SDISearchResult<SDIRelease>> results = new ArrayList<SDISearchResults.SDISearchResult<SDIRelease>>();
			for (JsonReleaseSearchResult result : mSearchResults) {
				results.add(new SDISearchResults.SDISearchResult<SDIRelease>(result));
			}
			return results;
		}
	}

	private static final class JsonReleaseSearchResult implements SDISearchResults.SDISearchResult.Composition<SDIRelease> {
		@JsonProperty("type") private String mType;
		@JsonProperty("score") private Double mScore;
		@JsonProperty("release") private JsonRelease mRelease;

		@Override public String getType() {
			return mType;
		}

		@Override public Double getScore() {
			return mScore;
		}

		@Override public SDIRelease getResult() {
			return null != mRelease ? new SDIRelease(mRelease) : null;
		}
	}

}
