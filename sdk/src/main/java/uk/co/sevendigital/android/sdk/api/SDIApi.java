package uk.co.sevendigital.android.sdk.api;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.request.artist.SDIArtistSearchRequest;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistChartsRequest;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistDetailsRequest;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistReleasesRequest;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistTopTracksRequest;
import uk.co.sevendigital.android.sdk.api.request.preview.SDIGetTrackPreviewRequest;
import uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseChartsRequest;
import uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseDetailsRequest;
import uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseTracksRequest;
import uk.co.sevendigital.android.sdk.api.request.release.SDIReleaseSearchRequest;
import uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackChartsRequest;
import uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackDetailsRequest;
import uk.co.sevendigital.android.sdk.api.request.track.SDITrackSearchRequest;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

// @formatter:off
/**
 * Main entry point to the 7Digital api
 *
 * Contains the following Api access objects:
 *
 *  - {@link uk.co.sevendigital.android.sdk.api.SDIApi.Artist} api
 *  - {@link uk.co.sevendigital.android.sdk.api.SDIApi.Release} api
 *  - {@link uk.co.sevendigital.android.sdk.api.SDIApi.Streaming} api
 *  - {@link uk.co.sevendigital.android.sdk.api.SDIApi.Streaming} api
 *
 */
// @formatter:on
public class SDIApi {

	private SDIServerUtil.OauthConsumer mConsumer;
	private RequestQueue mRequestQueue = null;
	private Context mContext;

	public SDIApi(Context context, SDIServerUtil.OauthConsumer consumer) {
		this(context, null, consumer);
	}

	public SDIApi(Context context, RequestQueue queue, SDIServerUtil.OauthConsumer consumer) {
		mContext = context;
		mConsumer = consumer;
		mRequestQueue = queue;
		if (null == context) throw new IllegalArgumentException("Context cannot be null");
		if (null == consumer) throw new IllegalArgumentException("OauthConsumer cannot be null");
	}

	/**
	 * 
	 * @return an {@link SDIApi.Artist} api object for accessing artist endpoints
	 */
	public Artist artist() {
		return new Artist();
	}

	/**
	 * 
	 * @return an {@link SDIApi.Release} api object for accessing release endpoints
	 */
	public Release release() {
		return new Release();
	}

	/**
	 * 
	 * @return an {@link SDIApi.Track} api object for accessing track endpoints
	 */
	public Track track() {
		return new Track();
	}

	/**
	 * 
	 * @return an {@link SDIApi.Streaming} api object for accessing streaming endpoints
	 */
	public Streaming streaming() {
		return new Streaming();
	}

	/**
	 * The Artist api for accessing api methods relating to an artist
	 */
	public class Artist {

		/**
		 * Get the top tracks for an artist
		 * 
		 * @param artistId - the id of the artist for which to get the top tracks
		 * @return {@link SDIGetArtistTopTracksRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistTopTracksRequest.Result getTopTracks(String artistId) throws InterruptedException, ExecutionException,
				IOException {
			return SDIGetArtistTopTracksRequest.execute(mContext, mConsumer, mRequestQueue, artistId);
		}

		/**
		 * Get the top tracks for an artist
		 * 
		 * @param artistId - the id of the artist for which to get the top tracks
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @return {@link SDIGetArtistTopTracksRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistTopTracksRequest.Result getTopTracks(String artistId, int page) throws InterruptedException, ExecutionException,
				IOException {
			return SDIGetArtistTopTracksRequest.execute(mContext, mConsumer, mRequestQueue, artistId, page);
		}

		/**
		 * Get the top tracks for an artist
		 * 
		 * @param artistId - the id of the artist for which to get the top tracks
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @return {@link SDIGetArtistTopTracksRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistTopTracksRequest.Result getTopTracks(String artistId, int page, int pageSize) throws InterruptedException,
				ExecutionException, IOException {
			return SDIGetArtistTopTracksRequest.execute(mContext, mConsumer, mRequestQueue, artistId, page, pageSize);
		}

		/**
		 * Get the top tracks for an artist
		 * 
		 * @param artistId - the id of the artist for which to get the top tracks
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @param streamable If provided search results will contain only artists that can/cannot be streamed
		 * @param licensorId If provided search results will contain only artists from this licensor. A minus sign prefix excludes artists
		 *        from that licensor (e.g. -5)
		 * @param country 2 letter ISO country code of the country whose artists you would like to search
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @return {@link SDIGetArtistTopTracksRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistTopTracksRequest.Result getTopTracks(String artistId, int page, int pageSize, Boolean streamable,
				String licensorId, String country, Integer imageSize) throws InterruptedException, ExecutionException, IOException {
			return SDIGetArtistTopTracksRequest.execute(mContext, mConsumer, mRequestQueue, artistId, page, pageSize, streamable,
					licensorId, country, imageSize);
		}

		/**
		 * Get the releases (albums) for an artist
		 * 
		 * @param artistId the id of the artist for which to get the releases
		 * @return
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistReleasesRequest.Result getReleases(String artistId) throws InterruptedException, ExecutionException, IOException {
			return SDIGetArtistReleasesRequest.execute(mContext, mConsumer, mRequestQueue, artistId);
		}

		/**
		 * Get the releases (albums) for an artist
		 * 
		 * @param artistId the id of the artist for which to get the releases * @param context The application context.
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @return Return the result of the network retrieval.
		 * @return
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistReleasesRequest.Result getReleases(String artistId, int page) throws InterruptedException, ExecutionException,
				IOException {
			return SDIGetArtistReleasesRequest.execute(mContext, mConsumer, mRequestQueue, artistId, page);
		}

		/**
		 * Get the releases (albums) for an artist
		 * 
		 * @param artistId the id of the artist for which to get the releases * @param context The application context.
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @return Return the result of the network retrieval.
		 * @return
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistReleasesRequest.Result getReleases(String artistId, int page, int pageSize) throws InterruptedException,
				ExecutionException, IOException {
			return SDIGetArtistReleasesRequest.execute(mContext, mConsumer, mRequestQueue, artistId, page, pageSize);
		}

		/**
		 * Get the releases (albums) for an artist
		 * 
		 * @param artistId the id of the artist for which to get the releases * @param context The application context.
		 * @param type Releases can be of type album, single or video. If specified, results are filtered by release type.
		 * @param streamable If provided search results will contain only artists that can/cannot be streamed
		 * @param licensorId If provided search results will contain only artists from this licensor. A minus sign prefix excludes artists
		 *        from that licensor (e.g. -5)
		 * @param country 2 letter ISO country code of the country whose artists you would like to search
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @param imageSize The requested width of the image in pixels
		 * @return Return the result of the network retrieval.
		 * @return
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIGetArtistReleasesRequest.Result getReleases(String artistId, int page, int pageSize, String type, String streamable,
				String licensorId, String country, Integer imageSize) throws InterruptedException, ExecutionException, IOException {
			return SDIGetArtistReleasesRequest.execute(mContext, mConsumer, mRequestQueue, artistId, page, pageSize, type, streamable,
					licensorId, country, imageSize);
		}

		/**
		 * Search the 7Digital catalogue for artists
		 * 
		 * @param query The search query string.
		 * @return {@link SDIArtistSearchRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIArtistSearchRequest.Result search(String query) throws InterruptedException, ExecutionException, IOException {
			return SDIArtistSearchRequest.execute(mContext, mConsumer, mRequestQueue, query);
		}

		/**
		 * Search the 7Digital catalogue for artists
		 * 
		 * @param query The search query string.
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @return {@link SDIArtistSearchRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIArtistSearchRequest.Result search(String query, int page) throws InterruptedException, ExecutionException, IOException {
			return SDIArtistSearchRequest.execute(mContext, mConsumer, mRequestQueue, query, page);
		}

		/**
		 * Search the 7Digital catalogue for artists
		 * 
		 * @param query The search query string.
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @return {@link SDIArtistSearchRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIArtistSearchRequest.Result search(String query, int page, int pageSize) throws InterruptedException, ExecutionException,
				IOException {
			return SDIArtistSearchRequest.execute(mContext, mConsumer, mRequestQueue, query, page, pageSize);
		}

		/**
		 * Search the 7Digital catalogue for artists
		 * 
		 * @param query The search query string.
		 * @param sort Orders the returned results. Supplied as a string in the format: "{sortColumn} {sortOrder}", for example:
		 *        "popularity desc" will show most popular artists first regardless of search match score. Currently you can sort by name,
		 *        popularity and score. If no sort is supplied, the default is "score desc". If no order is supplied the default is
		 *        ascending.
		 * @param streamable If provided search results will contain only artists that can/cannot be streamed
		 * @param licensorId If provided search results will contain only artists from this licensor. A minus sign prefix excludes artists
		 *        from that licensor (e.g. -5)
		 * @param country 2 letter ISO country code of the country whose artists you would like to search
		 * @param page Page number of the result set. If not supplied, defaults to 1
		 * @param pageSize Number of items to be returned per page. If not supplied, defaults to 10. Maximum page size is 500
		 * @param imageSize The requested width of the image in pixels
		 * @return {@link SDIArtistSearchRequest.Result}
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws IOException
		 */
		public SDIArtistSearchRequest.Result search(String query, int page, int pageSize, String sort, String streamable,
				String licensorId, String country, Integer imageSize) throws InterruptedException, ExecutionException, IOException {
			return SDIArtistSearchRequest.execute(mContext, mConsumer, mRequestQueue, query, page, pageSize, sort, streamable, licensorId,
					country, imageSize);
		}

		/**
		 * 
		 * Get the details for an artist
		 * 
		 * @param artistId The id of the mArtist to retrieve details for
		 * 
		 * @return Return the result of the network retrieval.
		 * @throws Exception
		 */
		public SDIGetArtistDetailsRequest.Result getDetails(String artistId) throws Exception {
			return SDIGetArtistDetailsRequest.execute(mContext, mConsumer, mRequestQueue, artistId);
		}

		/**
		 * Get the current charts for tracks
		 * 
		 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
		 * 
		 * @param page The page to retrieve (note: one-indexed).
		 * @return Return the result of the network retrieval.
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws SignatureException
		 * @throws IOException
		 */
		public SDIGetArtistChartsRequest.Result getCharts(int page) throws InterruptedException, ExecutionException, SignatureException,
				IOException {
			return SDIGetArtistChartsRequest.execute(mContext, mConsumer, mRequestQueue, page);
		}

		/**
		 * Get the current charts for tracks
		 * 
		 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
		 * 
		 * @param page The page to retrieve (note: one-indexed).
		 * @param pageSize The size of the page to retrieve.
		 * @return Return the result of the network retrieval.
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws SignatureException
		 * @throws IOException
		 */
		public SDIGetArtistChartsRequest.Result getCharts(int page, int pageSize) throws InterruptedException, ExecutionException,
				SignatureException, IOException {
			return SDIGetArtistChartsRequest.execute(mContext, mConsumer, mRequestQueue, page, pageSize);
		}

		/**
		 * Get the current charts for tracks
		 * 
		 * Note: The <var>page</var> property is one-indexed (hence the first page is 1, not zero).
		 * 
		 * @param page The page to retrieve (note: one-indexed).
		 * @param pageSize The size of the page to retrieve.
		 * @param period optional The time period for which the chart is generated. Default value is a week.
		 * @param toDate format is YYYYMMDD optional The last day the chart should include data for. If not provided, by default the most
		 *        recent chart for requested period will be returned.
		 * @return Return the result of the network retrieval.
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws SignatureException
		 * @throws IOException
		 */
		public SDIGetArtistChartsRequest.Result getCharts(int page, int pageSize, String period, String toDate)
				throws InterruptedException, ExecutionException, SignatureException, IOException {
			return SDIGetArtistChartsRequest.execute(mContext, mConsumer, mRequestQueue, page, pageSize, period, toDate);
		}

	}

	/**
	 * The Release api for accessing api methods relating to an release
	 */
	public class Release {

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseTracksRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String)
		 */
		public SDIGetReleaseTracksRequest.Result getTracks(String releaseId) throws Exception {
			return SDIGetReleaseTracksRequest.execute(mContext, mConsumer, mRequestQueue, releaseId);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIReleaseSearchRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String)
		 */
		public SDIReleaseSearchRequest.Result search(String query) throws Exception {
			return SDIReleaseSearchRequest.execute(mContext, mConsumer, mRequestQueue, query);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseDetailsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String)
		 */
		public SDIGetReleaseDetailsRequest.Result getDetails(String releaseId) throws Exception {
			return SDIGetReleaseDetailsRequest.execute(mContext, mConsumer, mRequestQueue, releaseId);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseDetailsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String, String, Integer)
		 */
		public SDIGetReleaseDetailsRequest.Result getDetails(String releaseId, String country, Integer imageSize) throws Exception {
			return SDIGetReleaseDetailsRequest.execute(mContext, mConsumer, mRequestQueue, releaseId, country, imageSize);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseChartsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, int)
		 */
		public SDIGetReleaseChartsRequest.Result getCharts(int page) throws InterruptedException, ExecutionException, JsonParseException,
				JsonMappingException, IOException, SignatureException {
			return SDIGetReleaseChartsRequest.execute(mContext, mConsumer, mRequestQueue, page);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseChartsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, int, int)
		 */
		public SDIGetReleaseChartsRequest.Result getCharts(int page, int pageSize) throws InterruptedException, ExecutionException,
				IOException, SignatureException {
			return SDIGetReleaseChartsRequest.execute(mContext, mConsumer, mRequestQueue, page, pageSize);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseChartsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, int, int, String, String,
		 *      Boolean, Integer, String, Integer)
		 */
		public SDIGetReleaseChartsRequest.Result getCharts(int page, int pageSize, String period, String toDate, Boolean streamable,
				Integer licensorId, String country, Integer imageSize) throws InterruptedException, ExecutionException, IOException,
				SignatureException {
			return SDIGetReleaseChartsRequest.execute(mContext, mConsumer, mRequestQueue, page, pageSize, period, toDate, streamable,
					licensorId, country, imageSize);
		}

	}

	/**
	 * The Track api for accessing api methods relating to an track
	 */
	public class Track {

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDITrackSearchRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String)
		 */
		public SDITrackSearchRequest.Result search(String query) throws Exception {
			return SDITrackSearchRequest.execute(mContext, mConsumer, mRequestQueue, query);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDITrackSearchRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String, int)
		 */
		public SDITrackSearchRequest.Result search(String query, int page) throws Exception {
			return SDITrackSearchRequest.execute(mContext, mConsumer, mRequestQueue, query, page);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDITrackSearchRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String, int, int)
		 */
		public SDITrackSearchRequest.Result search(String query, int page, int pageSize) throws Exception {
			return SDITrackSearchRequest.execute(mContext, mConsumer, mRequestQueue, query, page, pageSize);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDITrackSearchRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String, int, int, String,
		 *      String, String, Integer)
		 */
		public SDITrackSearchRequest.Result search(String query, int page, int pageSize, String streamable, String licensorId,
				String country, Integer imageSize) throws Exception {
			return SDITrackSearchRequest.execute(mContext, mConsumer, mRequestQueue, query, page, pageSize, streamable, licensorId,
					country, imageSize);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackDetailsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String)
		 */
		public SDIGetTrackDetailsRequest.Result getDetails(String trackId) throws Exception {
			return SDIGetTrackDetailsRequest.execute(mContext, mConsumer, mRequestQueue, trackId);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackDetailsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, String, String, Integer)
		 */
		public SDIGetTrackDetailsRequest.Result getDetails(String trackId, String country, Integer imageSize) throws Exception {
			return SDIGetTrackDetailsRequest.execute(mContext, mConsumer, mRequestQueue, trackId, country, imageSize);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackChartsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, int)
		 */
		public SDIGetTrackChartsRequest.Result getCharts(int page) throws InterruptedException, ExecutionException, IOException,
				SignatureException {
			return SDIGetTrackChartsRequest.execute(mContext, mConsumer, mRequestQueue, page);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackChartsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, int, int)
		 */
		public SDIGetTrackChartsRequest.Result getCharts(int page, int pageSize) throws InterruptedException, ExecutionException,
				IOException, SignatureException {
			return SDIGetTrackChartsRequest.execute(mContext, mConsumer, mRequestQueue, page, pageSize);
		}

		/**
		 * @see uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackChartsRequest#execute(android.content.Context,
		 *      uk.co.sevendigital.android.sdk.util.SDIServerUtil.OauthConsumer, com.android.volley.RequestQueue, int, int, String, String,
		 *      Boolean, Integer, String, Integer)
		 */
		public SDIGetTrackChartsRequest.Result getCharts(int page, int pageSize, String period, String toDate, Boolean streamable,
				Integer licensorId, String country, Integer imageSize) throws InterruptedException, ExecutionException, IOException,
				SignatureException {
			return SDIGetTrackChartsRequest.execute(mContext, mConsumer, mRequestQueue, page, pageSize, period, toDate, streamable,
					licensorId, country, imageSize);
		}
	}

	/**
	 * The Streaming api for accessing api methods relating to streaming tracks
	 */
	public class Streaming {

		/**
		 * Get the url of a track preview
		 *
		 * @param trackId - id of the track get preview
		 * 
		 * @return the bytes for the track preview
		 */
		public SDIGetTrackPreviewRequest.Result getTrackPreview(@NonNull String trackId)
				throws InterruptedException, ExecutionException, SignatureException, IOException {
			return getTrackPreview(null, trackId);
		}

		/**
		 *
		 * Get the url of a track preview
		 *
		 * @param token a authenticated token for a logged in user
		 * @param trackId - the bytes for the track preview
		 * @return
		 * @throws InterruptedException
		 * @throws ExecutionException
		 * @throws SignatureException
		 * @throws IOException
		 */
		public SDIGetTrackPreviewRequest.Result getTrackPreview(@Nullable SDIServerUtil.ServerAccessToken token, @NonNull String trackId)
				throws InterruptedException, ExecutionException, SignatureException, IOException {
			return SDIGetTrackPreviewRequest.execute(mContext, mConsumer, mRequestQueue, token, trackId);
		}
	}
}
