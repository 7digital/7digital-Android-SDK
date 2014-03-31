package uk.co.sevendigital.android.sdk.sample.fragment.search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIArtistSearchRequest;
import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.activity.ArtistDetailActivity;
import uk.co.sevendigital.android.sdk.sample.app.SampleApplication;
import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDISearchResults;

public class ArtistSearchFragment extends AbsSearchFragment {

	private ArtistAdapter mAdapter;
	private List<SDISearchResults.SDISearchResult<SDIArtist>> mSearchResults = new ArrayList<>();

	public static ArtistSearchFragment newInstance() {
		ArtistSearchFragment fragment = new ArtistSearchFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ArtistSearchFragment() {}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mAdapter = new ArtistAdapter(getActivity(), mSearchResults);
		getListView().setAdapter(mAdapter);
	}

	@Override protected void doSearch() {
		super.doSearch();
		new ArtistSearchAsync().execute(mSearchEdittext.getText().toString());
	}

	public static class ArtistAdapter extends ArrayAdapter<SDISearchResults.SDISearchResult<SDIArtist>> {

		public ArtistAdapter(Context context, List<SDISearchResults.SDISearchResult<SDIArtist>> objects) {
			super(context, 0, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = ViewHolder.get(convertView, parent);
			SDISearchResults.SDISearchResult<SDIArtist> searchItem = getItem(position);

			//			vh.position.setText(Integer.toString(searchItem.getResult());
			vh.name.setText(searchItem.getResult().getName());

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
			public final TextView name;
			public final TextView position;

			private ViewHolder(ViewGroup parent) {
				root = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_row, parent, false);
				root.setTag(this);

				name = (TextView) root.findViewById(R.id.title_textview);
				position = (TextView) root.findViewById(R.id.chart_position_textview);
				image = (ImageView) root.findViewById(R.id.image_imageview);
			}
		}
	}

	@Override public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ArtistDetailActivity.startActivity(getActivity(), mAdapter.getItem(position).getResult().getExternalId());
	}

	private class ArtistSearchAsync extends AsyncTask<String, Void, SDISearchResults<SDIArtist>> {

		private ArtistSearchAsync() {}

		@Override protected SDISearchResults<SDIArtist> doInBackground(String... params) {

			String query = params[0];

			SDIArtistSearchRequest.Result result = null;

			SDIApi api = SampleApplication.getApi();

			try {
				result = api.artist().search(query);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (null != result) return result.getSearchResult();
			return null;
		}

		@Override protected void onPostExecute(SDISearchResults<SDIArtist> searchResults) {
			super.onPostExecute(searchResults);
			if (!isAdded()) return;
			mSearchResults.clear();
			mSearchResults.addAll(searchResults.getResults());
			mAdapter.notifyDataSetChanged();

			setProgressVisible(false);
		}
	}

	@Override protected String getTitle() {
		return "Artist";
	}
}
