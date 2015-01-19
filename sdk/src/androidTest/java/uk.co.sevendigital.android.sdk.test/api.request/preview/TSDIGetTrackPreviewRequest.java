package uk.co.sevendigital.android.sdk.api.request.preview;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.login.SDILoginUserRequest;
import uk.co.sevendigital.android.sdk.api.request.login.TSDILoginUserRequest;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;
import android.content.Context;
import android.test.AndroidTestCase;

public class TSDIGetTrackPreviewRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testAuthenticatedGetTrackPreview() throws Exception {
		Utils.trustSelfSignedCertificates();

		// attempt to login the user
		SDILoginUserRequest.Result loginResult = TSDILoginUserRequest.loginTestUserDefaultConsumer(getContext(), SDICore.getSerializer());

		// cache the logged in user token and retrieve the track preview
		// note: based on server permissions around logging in the user, the login request may fail because the consumer key of the
		// developer running the tests does not have permission to login on a user's behalf. as a result, if login fails in this instance,
		// we retrieve the track preview without providing the server access token. providing the server access token for the track preview 
		// is optional.
		SDIServerUtil.ServerAccessToken token = loginResult.getResultCode().equals(
				SDILoginUserRequest.ResultCode.FAILURE_CONSUMER_UNAUTHORISED) ? null : loginResult.getAuthorisedAccessToken();
		SDIGetTrackPreviewRequest.Result result = getTrackPreview(getContext(), token, "1234");

		// assert the request was successful
		assertEquals(SDIGetTrackPreviewRequest.ResultCode.SUCCESS, result.getResultCode());
	}

	public void testGetTrackPreview() throws Exception {
		Utils.trustSelfSignedCertificates();

		SDIGetTrackPreviewRequest.Result result = getTrackPreview(getContext(),"1234");

		// assert the request was successful
		assertEquals(SDIGetTrackPreviewRequest.ResultCode.SUCCESS, result.getResultCode());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetTrackPreviewRequest.Result getTrackPreview(Context context,
			SDIServerUtil.ServerAccessToken authorisedAccessToken, String trackId) throws SignatureException, InterruptedException,
			ExecutionException, IOException {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,
				BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.streaming().getTrackPreview(authorisedAccessToken, trackId);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetTrackPreviewRequest.Result getTrackPreview(Context context, String trackId) throws SignatureException, InterruptedException,
			ExecutionException, IOException {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,
				BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.streaming().getTrackPreview(trackId);
	}

}
