package uk.co.sevendigital.android.sdk.api.request.artist;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
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
import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.util.SDIOauthHelper;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.VolleyUtil;

public final class SDIGetArtistDetailsRequest extends SDIAbsXmlRequest<SDIGetArtistDetailsRequest.Result> {

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
	 * Get the details for an artist
	 *
	 * @param context The application context.
	 * @param queue (Optional) The Volley request queue used to execute the network request.
	 * @param artistId The id of the mArtist to retrieve details for
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
	public static Result execute(Context context, SDIServerUtil.OauthConsumer consumer,RequestQueue queue, String artistId)
			throws UnsupportedEncodingException, ExecutionException, InterruptedException,Exception {

		Serializer serializer = SDICore.getSerializer();

		if (context == null) throw new NullPointerException("context cannot be null");
		if (consumer == null) throw new NullPointerException("consumer cannot be null");
		if (queue == null) queue = SDICore.getQueue(context);
		if (null == artistId) throw new IllegalArgumentException("artistId cannot be null");

		String timestamp = SDIOauthHelper.getServerTime(SDIServerUtil.getHttpClient(), consumer.toTuple());
		String nonce = SDIOauthHelper.getNonce();

		String url = SDIConstants.ENDPOINT_ARTIST_DETAILS;

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("artistId", artistId));
		parameters.add(new Pair<String, String>("oauth_consumer_key", consumer.getKey()));
		parameters.add(new Pair<String, String>("oauth_nonce", nonce));
		parameters.add(new Pair<String, String>("oauth_signature_method", "HMAC-SHA1"));
		parameters.add(new Pair<String, String>("oauth_timestamp", timestamp));

		Collections.sort(parameters, SDIServerUtil.SORT_ALPHABETICAL_BY_KEY);

		// build full url
		url += "?" + SDIServerUtil.buildUrlParameterString(parameters);

		VolleyUtil.CacheEntryRequestParams params = new VolleyUtil.CacheEntryRequestParams(queue, Method.GET, url);
		addUserAgent(params);
		//params.addHeader("Accept", "application/json");//this endpoint only supports xml right now

		VolleyUtil.CacheEntryResponse<String> response = VolleyUtil.executeStringRequest(params);

		// throw an exception if the response is invalid
		if (response == null || response.getResponse() == null) throw new IllegalStateException("response invalid: " + response);

		Response xmlResponse = parseResponse(serializer, response.getResponse());

		// throw an exception if the response is invalid
		if (xmlResponse == null || xmlResponse.mArtist == null) throw new IllegalStateException("response " + "invalid");

		SDIArtist artist = new SDIArtist(xmlResponse.mArtist);

		return new Result(ResultCode.SUCCESS, response.getCacheEntry(), artist);
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
		private final SDIArtist mArtist;

		protected Result(int resultCode) {
			this(resultCode, null, null);
		}

		protected Result(int resultCode, Cache.Entry entry, SDIArtist artist) {
			mResultCode = resultCode;
			mCacheEntry = entry;
			mArtist = artist;
		}

		public int getResultCode() {
			return mResultCode;
		}

		public Cache.Entry getCacheEntry() {
			return mCacheEntry;
		}

		public SDIArtist getArtist() {
			return mArtist;
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
		@Element(name="error",required = false) public Error mError;
		@Element(name="artist",required = false) public XMLArtist mArtist;
	}



}
