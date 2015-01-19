package uk.co.sevendigital.android.sdk.sample.fragment.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.object.SDITrack;
import uk.co.sevendigital.android.sdk.api.request.preview.SDIGetTrackPreviewRequest;
import uk.co.sevendigital.android.sdk.api.request.track.SDIGetTrackDetailsRequest;
import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.app.SampleApplication;
import uk.co.sevendigital.android.sdk.sample.util.CoverHelper;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.toolbox.ImageLoader;

/**
 * Demonstrates how to load metadata and details for a track
 */
public class TrackDetailFragment extends Fragment {

	MediaPlayer mMediaPlayer = new MediaPlayer();
	@InjectView(R.id.title_textview) TextView mTitleTextview;
	@InjectView(R.id.image_imageview) ImageView mImageImageview;
	@InjectView(R.id.content) View mContent;
	@InjectView(R.id.preview_button) Button mPreviewButton;
	@InjectView(R.id.progressBar) ProgressBar mProgressBar;

	public static TrackDetailFragment newInstance() {
		TrackDetailFragment fragment = new TrackDetailFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	private SDITrack mTrack;
	private byte[] mPreview;

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
		if (null == mTrack) return;
		mTitleTextview.setText(mTrack.getTitle());

		if (null != mTrack && null != mTrack.getRelease()) {
			SampleApplication.getImageLoader().get(CoverHelper.swapSizedImageUrlSize(mTrack.getRelease().getImageUrl(), 350),
					ImageLoader.getImageListener(mImageImageview, R.drawable.placeholder_image, R.drawable.placeholder_image));
		}

		if (null != mPreview && mPreview.length > 0) mPreviewButton.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.preview_button) public void onPreviewClick() {
		playMp3(mPreview);
	}

	private class TrackDetailAsync extends AsyncTask<String, Void, TrackDetails> {

		private TrackDetailAsync() {}

		@Override protected void onPreExecute() {
			super.onPreExecute();
			setProgressVisible(true);
		}

		@Override protected TrackDetails doInBackground(String... params) {

			String trackId = params[0];

			SDIGetTrackDetailsRequest.Result result = null;
			SDIApi api = SampleApplication.getApi();

			try {
				result = api.track().getDetails(trackId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* attempt to get track preview*/
			byte[] preview = null;
			try {
				SDIGetTrackPreviewRequest.Result previewResult = api.streaming().getTrackPreview(trackId);
				preview = previewResult.getTrackPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (null != result) return new TrackDetails(result.getTrack(), preview);
			return null;
		}

		@Override protected void onPostExecute(TrackDetails track) {
			super.onPostExecute(track);
			if (!isAdded()) return;
			setProgressVisible(false);

			mTrack = track.getTrack();
			mPreview = track.getPreview();
			updateView();

		}
	}

	@Override public void onPause() {
		super.onPause();
		try {

			if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
			mMediaPlayer.release();
			mMediaPlayer = null;
		} catch (Exception e) {

		}
	}

	public static class TrackDetails {
		private SDITrack mTrack;
		private byte[] mPreview;

		public TrackDetails(SDITrack track, byte[] preview) {
			mTrack = track;
			mPreview = preview;
		}

		public SDITrack getTrack() {
			return mTrack;
		}

		public byte[] getPreview() {
			return mPreview;
		}
	}

	private void playMp3(byte[] mp3SoundByteArray) {
		try {
			mMediaPlayer.reset();
			String previewPath = getActivity().getCacheDir() + "/preview.mp3";
			File path = new File(previewPath);

			writeFile(path, new String(mp3SoundByteArray));

			FileInputStream fis = new FileInputStream(path);
			mMediaPlayer.setDataSource(fis.getFD());

			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IOException ex) {
			String s = ex.toString();
			ex.printStackTrace();
		}
	}

	public static void writeFile(File file, String contents) throws FileNotFoundException, UnsupportedEncodingException {
		if (file == null || contents == null) throw new IllegalArgumentException();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"), true);
		try {
			out.write(contents);
		} finally {
			out.close();
		}
	}

	public interface TrackDetailListener {
		String getTrackId();
	}

}
