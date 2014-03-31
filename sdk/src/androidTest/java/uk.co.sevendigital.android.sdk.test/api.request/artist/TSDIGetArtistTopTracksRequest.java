package uk.co.sevendigital.android.sdk.api.request.artist;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetArtistTopTracksRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetArtistTopTracks() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		String[] artistIds = new String[] { "1", "2", "4", "5", "6" };
		for (String artistId : artistIds) {

			SDIGetArtistTopTracksRequest.Result result = getArtistTopTracks(getContext(), artistId);

			assertEquals(SDIGetArtistDetailsRequest.ResultCode.SUCCESS, result.getResultCode());

			//make sure we have content
			assertNotNull(result.getTracks());
			assertTrue(result.getTracks().size() > 0);
			assertNotNull(result.getTracks().get(0));
			assertNotNull(result.getTracks().get(0).getTitle());
			assertNotNull(result.getTracks().get(0).getExternalId());
			assertNotNull(result.getTracks().get(0).getArtist());
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetArtistTopTracksRequest.Result getArtistTopTracks(Context context, String artistId) throws SignatureException,
			InterruptedException, ExecutionException, IOException, Exception {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.artist().getTopTracks(artistId);
	}

}
