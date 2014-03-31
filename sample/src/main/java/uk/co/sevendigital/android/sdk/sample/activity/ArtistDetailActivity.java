package uk.co.sevendigital.android.sdk.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.fragment.detail.ArtistDetailFragment;

public class ArtistDetailActivity extends Activity implements ArtistDetailFragment.ArtistDetailListener {

	public static final String EXTRA_ARTIST_ID = "artistId";

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * start activity
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private String mArtistId;

	public static Intent startActivity(Activity activity, String artistId) {
		Intent intent = new Intent(activity, ArtistDetailActivity.class);
		intent.putExtra(EXTRA_ARTIST_ID, artistId);
		activity.startActivity(intent);
		return intent;
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist_detail_activity);

		mArtistId = getIntent().getExtras().getString(EXTRA_ARTIST_ID);
	}

	@Override public String getArtistId() {
		return mArtistId;
	}


}
