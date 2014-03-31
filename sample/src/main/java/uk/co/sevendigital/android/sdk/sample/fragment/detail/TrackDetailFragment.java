package uk.co.sevendigital.android.sdk.sample.fragment.detail;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackDetailsRequest;
import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.app.SampleApplication;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;

/**
 * Demonstrates how to load metadata and details for a track
 */
public class TrackDetailFragment extends Fragment {

	@InjectView(R.id.title_textview) TextView mTitleTextview;
	@InjectView(R.id.image_imageview) NetworkImageView mImageImageview;
	@InjectView(R.id.content) View mContent;
	@InjectView(R.id.progressBar) ProgressBar mProgressBar;

	public static TrackDetailFragment newInstance() {
		TrackDetailFragment fragment = new TrackDetailFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	private SDITrack mTrack;

	private TrackDetailListener mListener;

	public TrackDetailFragment() {}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.track_detail_fragment, container);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		if (getActivity() instanceof TrackDetailListener) mListener = (TrackDetailListener) getActivity();

	}

	@Override public void onResume() {
		super.onResume();

		new TrackDetailAsync().execute(mListener.getTrackId());
	}

	private void setProgressVisible(boolean visible) {
		mContent.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
		mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}

	private void updateView() {
		if (null==mTrack)return;
		mTitleTextview.setText(mTrack.getTitle());

		if (null != mTrack && null != mTrack.getRelease()) {
			SampleApplication.getImageLoader().get(mTrack.getRelease().getImageUrl(),
					ImageLoader.getImageListener(mImageImageview, R.drawable.placeholder_image, R.drawable.placeholder_image));
		}
	}

	private class TrackDetailAsync extends AsyncTask<String, Void, SDITrack> {

		private TrackDetailAsync() {}

		@Override protected void onPreExecute() {
			super.onPreExecute();
			setProgressVisible(true);
		}

		@Override protected SDITrack doInBackground(String... params) {

			String trackId = params[0];

			SDIGetTrackDetailsRequest.Result result = null;
			SDIApi api = SampleApplication.getApi();

			try {
				result = api.track().getDetails(trackId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (null != result) return result.getTrack();
			return null;
		}

		@Override protected void onPostExecute(SDITrack track) {
			super.onPostExecute(track);
			if (!isAdded()) return;
			setProgressVisible(false);

			mTrack = track;
			updateView();

		}
	}

	public interface TrackDetailListener {
		String getTrackId();
	}

}
