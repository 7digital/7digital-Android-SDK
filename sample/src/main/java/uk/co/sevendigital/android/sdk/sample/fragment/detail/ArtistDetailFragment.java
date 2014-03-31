package uk.co.sevendigital.android.sdk.sample.fragment.detail;

import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistDetailsRequest;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistReleasesRequest;
import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.activity.ReleaseDetailActivity;
import uk.co.sevendigital.android.sdk.sample.app.SampleApplication;
import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDIRelease;

/**
 * Demonstrates how to load metadata and details for an artist
 */
public class ArtistDetailFragment extends ListFragment {

	@InjectView(R.id.title_textview) TextView mTitleTextview;
	@InjectView(R.id.image_imageview) NetworkImageView mImageImageview;
	@InjectView(R.id.content) View mContent;
	@InjectView(R.id.progressBar) ProgressBar mProgressBar;

	private ReleaseAdapter mReleaseAdapter;
	private List<SDIRelease> mReleases = new ArrayList<>();

	public static ArtistDetailFragment newInstance() {
		ArtistDetailFragment fragment = new ArtistDetailFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	private SDIArtist mArtist;

	private ArtistDetailListener mListener;

	public ArtistDetailFragment() {}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.artist_detail_fragment, container);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		if (getActivity() instanceof ArtistDetailListener) mListener = (ArtistDetailListener) getActivity();

		mReleaseAdapter = new ReleaseAdapter(getActivity(),mReleases);
		getListView().setAdapter(mReleaseAdapter);
	}

	@Override public void onResume() {
		super.onResume();

		new ArtistDetailAsync().execute(mListener.getArtistId());
	}

	private void setProgressVisible(boolean visible) {
		mContent.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
		mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}

	private void updateView() {
		if (null == mArtist) return;//todo: better handling of a null artist
		mTitleTextview.setText(mArtist.getName());

		if (null != mArtist.getImageUrl()) {

			SampleApplication.getImageLoader().get(mArtist.getImageUrl(),
					ImageLoader.getImageListener(mImageImageview, R.drawable.placeholder_image, R.drawable.placeholder_image));
		}
	}

	/**
	 * Loads Artist metadata and artists' releases (albums)
	 */
	private class ArtistDetailAsync extends AsyncTask<String, Void, ArtistDetailResult> {

		private ArtistDetailAsync() {}

		@Override protected void onPreExecute() {
			super.onPreExecute();
			setProgressVisible(true);
		}

		@Override protected ArtistDetailResult doInBackground(String... params) {

			String artistId = params[0];

			/* load artist details */
			SDIGetArtistDetailsRequest.Result result = null;
			SDIApi api = SampleApplication.getApi();

			try {
				result = api.artist().getDetails(artistId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* load artist albums */

			SDIGetArtistReleasesRequest.Result albumsResult = null;
			try {
				albumsResult = api.artist().getReleases(artistId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new ArtistDetailResult(null != result ? result.getArtist() : null, null != albumsResult ? albumsResult.getReleases()
					: null);
		}

		@Override protected void onPostExecute(ArtistDetailResult result) {
			super.onPostExecute(result);
			if (!isAdded()) return;
			setProgressVisible(false);

			mArtist = result.mArtist;

			mReleases.clear();
			mReleases.addAll(result.mAlbums);
			mReleaseAdapter.notifyDataSetChanged();

			updateView();
		}
	}

	private static class ArtistDetailResult {

		private SDIArtist mArtist;
		private List<SDIRelease> mAlbums;

		private ArtistDetailResult(SDIArtist artist, List<SDIRelease> albums) {
			mArtist = artist;
			mAlbums = albums;
		}
	}

	public interface ArtistDetailListener {
		String getArtistId();
	}

	@Override public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		SDIRelease release = mReleases.get(position);
		ReleaseDetailActivity.startActivity(getActivity(), release.getExternalId());
	}

	public static class ReleaseAdapter extends ArrayAdapter<SDIRelease> {

		public ReleaseAdapter(Context context, List<SDIRelease> objects) {
			super(context, 0, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = ViewHolder.get(convertView, parent);
			SDIRelease release = getItem(position);

			vh.title.setText(release.getTitle());
			vh.artist.setText(release.getArtistName());

			//todo: image loading
			return vh.root;
		}

		//By making this its own class we allow for reuse in other adapters
		private static class ViewHolder {
			public static ViewHolder get(View convertView, ViewGroup parent) {
				if (convertView == null) {
					return new ViewHolder(parent);
				}
				return (ViewHolder) convertView.getTag();
			}

			public final View root;
			public final ImageView image;
			public final TextView title;
			public final TextView artist;
			public final TextView position;

			private ViewHolder(ViewGroup parent) {
				root = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_row, parent, false);
				root.setTag(this);

				title = (TextView) root.findViewById(R.id.title_textview);
				artist = (TextView) root.findViewById(R.id.artist_textview);
				position = (TextView) root.findViewById(R.id.chart_position_textview);
				image = (ImageView) root.findViewById(R.id.image_imageview);
			}
		}
	}


}
