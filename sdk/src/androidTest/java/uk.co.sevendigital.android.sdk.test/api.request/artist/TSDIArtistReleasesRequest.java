package uk.co.sevendigital.android.sdk.api.request.artist;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIArtistReleasesRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testArtistReleases() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIGetArtistReleasesRequest.Result result = artistReleases(getContext(), "14607");

		assertEquals(SDIArtistSearchRequest.ResultCode.SUCCESS, result.getResultCode());

		//make sure we have content
		assertNotNull(result.getReleases());
		assertNotNull(result.getReleases().get(0));
		assertTrue(result.getReleases().get(0) instanceof SDIRelease);
		assertNotNull(result.getReleases().get(0).getTitle());
		assertNotNull(result.getReleases().get(0).getArtist());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetArtistReleasesRequest.Result artistReleases(Context context, String artistId) throws SignatureException,
			InterruptedException, ExecutionException, IOException, Exception {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.artist().getReleases(artistId);
	}

}
