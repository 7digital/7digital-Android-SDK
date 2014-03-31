package uk.co.sevendigital.android.sdk.api.request.preview;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.api.request.login.SDILoginUserRequest;
import uk.co.sevendigital.android.sdk.api.request.login.TSDILoginUserRequest;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetTrackPreviewRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetTrackPreview() throws Exception {
		Utils.trustSelfSignedCertificates();

		//todo: do we need the server access token?
		SDILoginUserRequest.Result loginResult = TSDILoginUserRequest.loginCachedTestUserDefaultConsumer(getContext(),
				SDICore.getSerializer());
		assertEquals(SDILoginUserRequest.ResultCode.SUCCESS, loginResult.getResultCode());

		SDIGetTrackPreviewRequest.Result result = getTrackPreview(getContext(), loginResult.getAuthorisedAccessToken(), "1234");

		assertEquals(SDIGetTrackPreviewRequest.ResultCode.SUCCESS, result.getResultCode());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetTrackPreviewRequest.Result getTrackPreview(Context context,
			SDIServerUtil.ServerAccessToken authorisedAccessToken, String trackId) throws SignatureException, InterruptedException,
			ExecutionException, IOException {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.streaming().getTrackPreview(authorisedAccessToken, trackId);
	}

}
