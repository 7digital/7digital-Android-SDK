package uk.co.sevendigital.android.sdk.api.request.artist;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDIChart;
import uk.co.sevendigital.android.sdk.api.object.SDIChartItem;
import uk.co.sevendigital.android.sdk.api.request.abs.SDIAbsJsonRequest;
import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

/**
 * A request for fetching artist charts.
 *
 * This should be executed inside an asynctask, intent service, or a background (non UI) thread.
 */
public final class SDIGetArtistChartsRequest extends SDIAbsJsonRequest<SDIGetArtistChartsRequest.Result> {
	private static final String EXTRA_SERVER_ACCESS_TOKEN = "extra_server_access_token";
	private static final String EXTRA_PAGE = "extra_page";
	private static final String EXTRA_PAGE_SIZE = "extra_page_size";
	private static final String EXTRA_PERIOD = "extra_period";
	private static final String EXTRA_TO_DATE = "extra_toDate";

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
	 * @param page The page to retrieve (note: one-indexed).
	 *
	 * @return Return the result of the network retrieval.
	 *
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, int page)
			throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException, SignatureException {
		return execute(context, consumer, queue, page, DEFAULT_PAGE_SIZE, null, null);
	}

	/**
	 * Get the current charts for tracks
	 *
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 *
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param page The page to retrieve (note: one-indexed).
	 * @param pageSize The size of the page to retrieve. This value must not exceed {@link #MAX_PAGE_SIZE}.
	 *
	 * @return Return the result of the network retrieval.
	 *
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, int page, int pageSize)
			throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException, SignatureException {
		return execute(context, consumer, queue, page, pageSize, null, null);
	}

	/**
	 * Get the current charts for tracks
	 *
	 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
	 *
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param page The page to retrieve (note: one-indexed).
	 * @param pageSize The size of the page to retrieve. This value must not exceed {@link #MAX_PAGE_SIZE}.
	 * @param period optional The time period for which the chart is generated. Default value is a week.
	 * @param toDate format is YYYYMMDD optional The last day the chart should include data for. If not provided, by default the most recent
	 *        chart for requested period will be returned.
	 *
	 * @return Return the result of the network retrieval.
	 *
	 * @throws java.util.concurrent.ExecutionException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @throws com.fasterxml.jackson.databind.JsonMappingException
	 * @throws com.fasterxml.jackson.core.JsonParseException
	 * @throws java.security.SignatureException
	 */
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer, RequestQueue queue, int page, int pageSize,
			String period, String toDate) throws InterruptedException, ExecutionException, IOException, SignatureException {

		ObjectMapper mapper = SDICore.getMapper();

		if (context == null) throw new NullPointerException("context cannot be null");
		if (consumer == null) throw new NullPointerException("consumer cannot be null");
		if (queue == null) queue = SDICore.getQueue(context);
		if (page < 1) throw new IllegalArgumentException("page one-indexed, invalid page: " + page);
		if (pageSize < 1) throw new IllegalArgumentException("page size invalid: " + pageSize);
		if (pageSize > MAX_PAGE_SIZE) throw new IllegalArgumentException("page size: " + pageSize + " exceeds maximum: " + MAX_PAGE_SIZE);

		String timestamp = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer.toTuple());
		String nonce = SDIOauthHelper.getNonce();

		String url = SDIConstants.ENDPOINT_ARTIST_CHARTS;

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("pageSize", Integer.toString(pageSize)));
		parameters.add(new Pair<String, String>("page", Integer.toString(page)));
		parameters.add(new Pair<String, String>("oauth_consumer_key", consumer.getKey()));
		parameters.add(new Pair<String, String>("oauth_nonce", nonce));
		parameters.add(new Pair<String, String>("oauth_signature_method", "HMAC-SHA1"));
		parameters.add(new Pair<String, String>("oauth_timestamp", timestamp));
		parameters.add(new Pair<String, String>("period", period));

		/* optional values */
		if (null != toDate) parameters.add(new Pair<String, String>("toDate", toDate));

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
		if (jsonResponse == null || !jsonResponse.mStatus.equals("ok") || jsonResponse.mChart == null
				|| jsonResponse.mChart.mChartItems == null) return new Result(ResultCode.FAILURE_UNKNOWN);

		SDIChart chart = new SDIChart<SDIArtist>(jsonResponse.mChart);

		List<SDIChartItem<SDIArtist>> chartItems = new ArrayList<SDIChartItem<SDIArtist>>();
		for (ArtistChartItem item : jsonResponse.mChart.mChartItems) {
			SDIChartItem<SDIArtist> artistItem = new SDIChartItem<SDIArtist>(item);
			SDIArtist release = new SDIArtist(item.getItem());
			artistItem.mItem = release;
			chartItems.add(artistItem);
		}

		chart.items = chartItems;

		return new Result(ResultCode.SUCCESS, response.getCacheEntry(), chart);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * result
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * The result of a {@link uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistChartsRequest} network request.
	 */
	public static final class Result {
		private final/* ResultCode */int mResultCode;
		private final Cache.Entry mCacheEntry;
		private final SDIChart mChart;

		protected Result(int resultCode) {
			this(resultCode, null, null);
		}

		protected Result(int resultCode, Cache.Entry entry, SDIChart chart) {
			mResultCode = resultCode;
			mCacheEntry = entry;
			mChart = chart;
		}

		public int getResultCode() {
			return mResultCode;
		}

		public Cache.Entry getCacheEntry() {
			return mCacheEntry;
		}

		public SDIChart getChart() {
			return mChart;
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
		@JsonProperty("chart") private JsonChart<ArtistChartItem> mChart;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * json playlist details
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static final class ArtistChartItem implements SDIChartItem.Composition {
		@JsonProperty("position") private int mPosition;
		@JsonProperty("change") private String mChange;
		@JsonProperty("artist") private JsonArtist mItem;

		@Override public JsonArtist getItem() {
			return mItem;
		}

		@Override public int getPosition() {
			return mPosition;
		}

		@Override public String getChange() {
			return mChange;
		}
	}

}
