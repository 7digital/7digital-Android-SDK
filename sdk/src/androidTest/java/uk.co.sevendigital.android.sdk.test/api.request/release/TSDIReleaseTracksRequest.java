package uk.co.sevendigital.android.sdk.api.request.release;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.BuildConfig;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIArtistSearchRequest;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIReleaseTracksRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testReleaseTracks() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIGetReleaseTracksRequest.Result result = getReleaseTracks(getContext(), "1259019");

		assertEquals(SDIArtistSearchRequest.ResultCode.SUCCESS, result.getResultCode());

		//make sure we have content
		assertNotNull(result.getTracks());
		assertNotNull(result.getTracks().get(0));
		assertNotNull(result.getTracks().get(0).getTitle());
		assertTrue(result.getTracks().get(0) instanceof SDITrack);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetReleaseTracksRequest.Result getReleaseTracks(Context context, String releaseId) throws SignatureException,
			InterruptedException, ExecutionException, IOException, Exception {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.release().getTracks(releaseId);
	}

}
