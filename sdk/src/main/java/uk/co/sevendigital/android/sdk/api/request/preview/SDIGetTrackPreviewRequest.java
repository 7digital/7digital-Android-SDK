package uk.co.sevendigital.android.sdk.api.request.preview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.core.SDIConstants;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.api.request.abs.SDIAbsRequest;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

public final class SDIGetTrackPreviewRequest extends SDIAbsRequest<SDIGetTrackPreviewRequest.Result> {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * execute (static)
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 *
	 * Get a track preview for a track
	 *
	 * @param context the application context.
	 * @param consumer the OauthConsumer credentials for this call
	 * @param queue the volley request queue to use for this call
	 * @param token the server access token (optional)
	 * @param trackId the unique id of the track to preview
	 *
	 * @return the result of the request
	 *
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 * @throws SignatureException
	 */
	@SuppressWarnings("ConstantConditions") public static Result execute(@NonNull Context context,
			@NonNull SDIServerUtil.OauthConsumer consumer, @NonNull RequestQueue queue, @Nullable SDIServerUtil.ServerAccessToken token,
			@NonNull final String trackId) throws InterruptedException, ExecutionException, IOException, SignatureException {

		if (context == null) throw new NullPointerException("context cannot be null");
		if (consumer == null) throw new NullPointerException("consumer cannot be null");
		if (queue == null) queue = SDICore.getQueue(context);
		if (trackId == null) throw new IllegalArgumentException("track id must be provided");

		String timestamp = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer.toTuple());
		String nonce = SDIOauthHelper.getNonce();

		String url = SDIConstants.ENDPOINT_TRACK_PREVIEW.replace("${track_id}", trackId);

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("oauth_consumer_key", consumer.getKey()));
		parameters.add(new Pair<String, String>("oauth_nonce", nonce));
		parameters.add(new Pair<String, String>("oauth_signature_method", "HMAC-SHA1"));
		parameters.add(new Pair<String, String>("oauth_timestamp", timestamp));
		if (token != null) parameters.add(new Pair<String, String>("oauth_token", token.getToken()));
		Collections.sort(parameters, SDIServerUtil.SORT_ALPHABETICAL_BY_KEY);

		// calculate and add the signature (if required)
		String oauthSignature = token != null ? SDIOauthHelper.generateGetOauthSignature(url, parameters, consumer.getSecret(),
				token.getSecret()) : SDIOauthHelper.generateGetOauthSignature(url, parameters, consumer.getSecret());
		parameters.add(new Pair<String, String>("oauth_signature", oauthSignature));
		Collections.sort(parameters, SDIServerUtil.SORT_ALPHABETICAL_BY_KEY);

		// build full url
		url += "?" + SDIServerUtil.buildUrlParameterString(parameters);

		VolleyUtil.CacheEntryRequestParams params = new VolleyUtil.CacheEntryRequestParams(queue, Method.GET, url);
		addUserAgent(params);
		VolleyUtil.CacheEntryResponse<String> response = VolleyUtil.executeStringRequest(params);

		// throw an exception if the response is invalid
		if (response == null || response.getResponse() == null) throw new IllegalStateException("response invalid: " + response);

		String data = response.getResponse();

		// return if the response is invalid
		if (TextUtils.isEmpty(data)) return new Result(ResultCode.FAILURE_UNKNOWN);

		return new Result(ResultCode.SUCCESS, response.getCacheEntry(), data.getBytes());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * result
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static final class Result {
		private final/* ResultCode */int mResultCode;
		private final Cache.Entry mCacheEntry;
		private final byte[] mPreviewBytes;

		protected Result(int resultCode) {
			this(resultCode, null, null);
		}

		public Result(int success, Cache.Entry cacheEntry, byte[] bytes) {
			mResultCode = success;
			mCacheEntry = cacheEntry;
			mPreviewBytes = bytes;
		}

		public int getResultCode() {
			return mResultCode;
		}

		public byte[] getTrackPreview() {
			return mPreviewBytes;
		};

		public Cache.Entry getCacheEntry() {
			return mCacheEntry;
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
}
