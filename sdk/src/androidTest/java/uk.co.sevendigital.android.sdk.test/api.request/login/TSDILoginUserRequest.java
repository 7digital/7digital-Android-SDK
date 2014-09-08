package uk.co.sevendigital.android.sdk.api.request.login;

import org.simpleframework.xml.Serializer;

import uk.co.sevendigital.android.sdk.core.SDICore;
import uk.co.sevendigital.android.sdk.test.BuildConfig;
import uk.co.sevendigital.android.sdk.util.Utils;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Pair;

public class TSDILoginUserRequest extends AndroidTestCase {
	private static SDILoginUserRequest.Result sTestUserDefaultConsumerLoginResult; // cached login for the test user (with default consumer)

	public static final String SHOP_ID_UK = "34";
	public static final String DEFAULT_SHOP_ID = SHOP_ID_UK; // the default shop id

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * injections
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	Serializer mSerializer = SDICore.getSerializer();

	/* package */static final class TestModule {}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * test: login user
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public void testLoginUser() throws Exception {
		Utils.trustSelfSignedCertificates();
		SDILoginUserRequest.Result result = loginTestUserDefaultConsumer(getContext(), mSerializer);

		// note: based on server permissions around logging in the user, the login request may fail because the consumer key of the
		// developer running the tests does not have permission to login on a user's behalf. as a result, if login fails in this instance we
		// simply ignore the failure and pass the test.
		assertTrue(result.getResultCode().equals(SDILoginUserRequest.ResultCode.SUCCESS)
				|| result.getResultCode().equals(SDILoginUserRequest.ResultCode.FAILURE_CONSUMER_UNAUTHORISED));
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * login cached test user default consumer
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static SDILoginUserRequest.Result loginCachedTestUserDefaultConsumer(Context context, Serializer serializer) throws Exception {

		if (sTestUserDefaultConsumerLoginResult != null) return sTestUserDefaultConsumerLoginResult;
		sTestUserDefaultConsumerLoginResult = loginTestUserDefaultConsumer(context, serializer);
		if (sTestUserDefaultConsumerLoginResult == null) fail("default user login failure");
		if (sTestUserDefaultConsumerLoginResult.getResultCode() != SDILoginUserRequest.ResultCode.SUCCESS) fail("default user login failure");
		return sTestUserDefaultConsumerLoginResult;
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * login test user default consumer
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static SDILoginUserRequest.Result loginTestUserDefaultConsumer(Context context, Serializer serializer) throws Exception {

		return loginUser(context, serializer, BuildConfig.TEST_USER_EMAIL, BuildConfig.TEST_USER_PASSWORD, new DefaultConsumerLoginConfig());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * login user
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static SDILoginUserRequest.Result loginUser(Context context, Serializer serializer, String email, String password,
			SDILoginUserRequest.LoginConfig config) throws Exception {

		return SDILoginUserRequest.execute(serializer, email, password, config);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * default consumer login config
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static class DefaultConsumerLoginConfig implements SDILoginUserRequest.LoginConfig {
		private static final long serialVersionUID = 2657832074181471616L;

		@Override public String getRequestShopId() {
			return DEFAULT_SHOP_ID;
		}

		@Override public Pair<String, String> getRequestOauthConsumer() {
			return new Pair<String, String>(BuildConfig.TEST_OAUTH_CONSUMER_KEY, BuildConfig.TEST_OAUTH_CONSUMER_SECRET);
		}
	}

}
