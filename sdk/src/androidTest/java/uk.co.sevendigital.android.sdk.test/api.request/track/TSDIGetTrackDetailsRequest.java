package uk.co.sevendigital.android.sdk.api.request.details;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.BuildConfig;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackDetailsRequest;
import uk.co.sevendigital.android.sdk.util.SDIServerUtil;
import uk.co.sevendigital.android.sdk.util.Utils;

public class TSDIGetTrackDetailsRequest extends AndroidTestCase {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testGetTrackDetails() throws Exception {

		Utils.trustSelfSignedCertificates();//for charles

		SDIGetTrackDetailsRequest.Result result = getTrackDetails(getContext(), "12345");

		assertEquals(SDIGetTrackDetailsRequest.ResultCode.SUCCESS, result.getResultCode());

		//		//make sure we have content
		//		assertNotNull(result.getArtist());
		//		assertNotNull(result.getArtist().getName());
		//		assertNotNull(result.getArtist().getExternalId());
		//		assertNotNull(result.getArtist().getUrl());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get user playlists
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	protected static SDIGetTrackDetailsRequest.Result getTrackDetails(Context context, String artistId) throws SignatureException,
			InterruptedException, ExecutionException, IOException, Exception {

		//construct new api client
		SDIApi api = new SDIApi(context, new SDIServerUtil.OauthConsumer(BuildConfig.TEST_OAUTH_CONSUMER_KEY, BuildConfig.TEST_OAUTH_CONSUMER_SECRET));
		//do the request
		return api.track().getDetails(artistId);
	}

}
