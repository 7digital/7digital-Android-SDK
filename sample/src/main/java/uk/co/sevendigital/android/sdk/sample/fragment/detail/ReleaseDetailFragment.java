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
import uk.co.sevendigital.android.sdk.api.request.release.SDIGetReleaseDetailsRequest;
import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.app.SampleApplication;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;

/**
 * Demonstrates how to load metadata and details for a release (album)
 */
public class ReleaseDetailFragment extends Fragment {

	@InjectView(R.id.title_textview) TextView mTitleTextview;
	@InjectView(R.id.image_imageview) NetworkImageView mImageImageview;
	@InjectView(R.id.content) View mContent;
	@InjectView(R.id.progressBar) ProgressBar mProgressBar;

	public static ReleaseDetailFragment newInstance() {
		ReleaseDetailFragment fragment = new ReleaseDetailFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	private SDIRelease mRelease;

	private ReleaseDetailListener mListener;

	public ReleaseDetailFragment() {}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.release_detail_fragment, container);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		if (getActivity() instanceof ReleaseDetailListener) mListener = (ReleaseDetailListener) getActivity();

	}

	@Override public void onResume() {
		super.onResume();

		new ReleaseDetailAsync().execute(mListener.getReleaseId());
	}

	private void setProgressVisible(boolean visible) {
		mContent.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
		mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}

	private void updateView() {
		if (null==mRelease)return;//todo: better handling of null release
		mTitleTextview.setText(mRelease.getTitle());
		if (null != mRelease.getImageUrl()) {
			SampleApplication.getImageLoader().get(mRelease.getImageUrl(),
					ImageLoader.getImageListener(mImageImageview, R.drawable.placeholder_image, R.drawable.placeholder_image));
		}
	}

	private class ReleaseDetailAsync extends AsyncTask<String, Void, SDIRelease> {

		private ReleaseDetailAsync() {}

		@Override protected void onPreExecute() {
			super.onPreExecute();
			setProgressVisible(true);
		}

		@Override protected SDIRelease doInBackground(String... params) {

			String releaseId = params[0];

			SDIGetReleaseDetailsRequest.Result result = null;

			SDIApi api = SampleApplication.getApi();

			try {
				result = api.release().getDetails(releaseId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (null != result) return result.getRelease();
			return null;
		}

		@Override protected void onPostExecute(SDIRelease release) {
			super.onPostExecute(release);
			if (!isAdded()) return;
			setProgressVisible(false);

			mRelease = release;
			updateView();

		}
	}

	public interface ReleaseDetailListener {
		String getReleaseId();
	}

}
