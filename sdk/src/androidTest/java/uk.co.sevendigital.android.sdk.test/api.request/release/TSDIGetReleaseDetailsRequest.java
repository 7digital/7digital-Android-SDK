package uk.co.sevendigital.android.sdk.api.request.details;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.BuildConfig;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistChartsRequest;
import uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseDetailsRequest;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetReleaseDetailsRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetReleaseDetails() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		String[] releaseIds = new String[] { "12345", "12346", "12331", "12332", "12312"};
		for (String releaseId : releaseIds) {
			SDIGetReleaseDetailsRequest.Result result = getReleaseDetails(getContext(), releaseId);

			assertEquals(SDIGetArtistChartsRequest.ResultCode.SUCCESS, result.getResultCode());

			//make sure we have content
			assertNotNull(result.getRelease());
			assertNotNull(result.getRelease().getArtist());
			assertNotNull(result.getRelease().getExternalId());
			assertNotNull(result.getRelease().getYear());
			assertNotNull(result.getRelease().getPrice());
			assertNotNull(result.getRelease().getPrice().getCurrencySymbol());
			assertNotNull(result.getRelease().getPrice().getCurrencyCode());
		}
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetReleaseDetailsRequest.Result getReleaseDetails(Context context, String artistId) throws SignatureException,
			InterruptedException, ExecutionException, IOException, Exception {

		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY,BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.release().getDetails(artistId);
	}

}
