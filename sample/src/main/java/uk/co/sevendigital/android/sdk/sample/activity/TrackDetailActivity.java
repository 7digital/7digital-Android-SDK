package uk.co.sevendigital.android.sdk.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.fragment.detail.TrackDetailFragment;

public class TrackDetailActivity extends Activity implements TrackDetailFragment.TrackDetailListener {

	public static final String EXTRA_TRACK_ID = "trackId";

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * start activity
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private String mTrackId;

	public static Intent startActivity(Activity activity, String trackId) {
		Intent intent = new Intent(activity, TrackDetailActivity.class);
		intent.putExtra(EXTRA_TRACK_ID, trackId);
		activity.startActivity(intent);
		return intent;
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_detail_activity);

		mTrackId = getIntent().getExtras().getString(EXTRA_TRACK_ID);
	}

	@Override public String getTrackId() {
		return mTrackId;
	}

}
