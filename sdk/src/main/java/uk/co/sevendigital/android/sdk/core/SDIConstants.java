package uk.co.sevendigital.android.sdk.core;

import android.os.Build;

import uk.co.sevendigital.android.sdk.BuildConfig;

public class SDIConstants {

	public static final String TAG = "SDISDK";

	public static final String USER_AGENT;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("7digital Android SDK");
		sb.append("/");
		sb.append(BuildConfig.VERSION_NAME);
		sb.append("; ");
		sb.append(Build.MODEL);
		USER_AGENT = sb.toString();
	}

	public static final String SERVER_BASE_URL = "http://api.7digital.com/1.2";
	public static final String SERVER_BASE_ACCOUNT_URL = "https://api.7digital.com/1.2";

	public static String getServerBaseUrlAccount() {
		return SERVER_BASE_ACCOUNT_URL;
	}

	public static String getServerBaseUrl() {
		return SERVER_BASE_URL;
	}

	public static final String ENDPOINT_STATUS = getServerBaseUrl() + "/status";

	public static final String ENDPOINT_REQUEST_TOKEN = getServerBaseUrl() + "/oauth/requesttoken";

	public static final String ENDPOINT_ACCESS_TOKEN = getServerBaseUrl() + "/oauth/accesstoken";

	public static final String ENDPOINT_AUTHORIZE = getServerBaseUrlAccount() + "/oauth/requesttoken/authorise";

	public static final String ENDPOINT_USERS = getServerBaseUrl() + "/users";

	public static final String ENDPOINT_PLAYLISTS = getServerBaseUrl() + "/playlists";

	public static final String ENDPOINT_USER_PLAYLISTS = getServerBaseUrl() + "/user/playlists";

	public static final String ENDPOINT_PLAYLIST_TRACKS = getServerBaseUrl() + "/playlists/${playlist_id}";

	public static final String ENDPOINT_POST_PLAYLIST = getServerBaseUrl() + "/playlists";

	public static final String ENDPOINT_POST_PLAYLIST_TRACKS = getServerBaseUrl() + "/playlists/${playlist_id}/tracks";

	public static final String ENDPOINT_POST_PLAYLIST_DETAILS = getServerBaseUrl() + "/playlists/${playlist_id}/details";

	public static final String ENDPOINT_DELETE_PLAYLIST = getServerBaseUrl() + "/playlists/${playlist_id}";

	public static final String ENDPOINT_DELETE_PLAYLIST_TRACK = getServerBaseUrl() + "/playlists/${playlist_id}/tracks/${track_id}";

	public static final String ENDPOINT_TRACK_PREVIEW = "http://previews.7digital.com/clip/${track_id}";

	/*
		Track Endpoints
	 */
	public static final String ENDPOINT_TRACK_SEARCH = getServerBaseUrl() + "/track/search";
	public static final String ENDPOINT_TRACK_DETAILS = getServerBaseUrl() + "/track/details";
	public static final String ENDPOINT_TRACK_CHARTS = getServerBaseUrl() + "/track/chart";

	/*
		Artist Endpoints
	 */
	public static final String ENDPOINT_ARTIST_SEARCH = getServerBaseUrl() + "/artist/search";
	public static final String ENDPOINT_ARTIST_RELEASES = getServerBaseUrl() + "/artist/releases";
	public static final String ENDPOINT_ARTIST_DETAILS = getServerBaseUrl() + "/artist/details";
	public static final String ENDPOINT_ARTIST_CHARTS = getServerBaseUrl() + "/artist/chart";
	public static final String ENDPOINT_ARTIST_TOPTRACKS = getServerBaseUrl() + "/artist/toptracks";

	/*
		Release Endpoints
	 */
	public static final String ENDPOINT_RELEASE_CHARTS = getServerBaseUrl() + "/release/chart";
	public static final String ENDPOINT_RELEASE_DETAILS = getServerBaseUrl() + "/release/details";
	public static final String ENDPOINT_RELEASE_SEARCH = getServerBaseUrl() + "/release/search";
	public static final String ENDPOINT_RELEASE_TRACKS = getServerBaseUrl() + "/release/tracks";


}
