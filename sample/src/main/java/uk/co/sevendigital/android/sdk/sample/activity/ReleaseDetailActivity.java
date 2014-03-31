package uk.co.sevendigital.android.sdk.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.fragment.detail.ReleaseDetailFragment;

public class ReleaseDetailActivity extends Activity implements ReleaseDetailFragment.ReleaseDetailListener {

	public static final String EXTRA_RELEASE_ID = "releaseId";

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * start activity
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private String mReleaseId;

	public static Intent startActivity(Activity activity, String releaseId) {
		Intent intent = new Intent(activity, ReleaseDetailActivity.class);
		intent.putExtra(EXTRA_RELEASE_ID, releaseId);
		activity.startActivity(intent);
		return intent;
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.release_detail_activity);

		mReleaseId = getIntent().getExtras().getString(EXTRA_RELEASE_ID);
	}

	@Override public String getReleaseId() {
		return mReleaseId;
	}
}
