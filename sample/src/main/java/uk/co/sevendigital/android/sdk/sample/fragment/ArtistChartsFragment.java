package uk.co.sevendigital.android.sdk.sample.fragment;

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
import android.widget.TextView;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.sevendigital.android.sdk.api.SDIApi;
import uk.co.sevendigital.android.sdk.api.request.artist.SDIGetArtistChartsRequest;
import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.activity.ArtistDetailActivity;
import uk.co.sevendigital.android.sdk.sample.app.SampleApplication;
import uk.co.sevendigital.android.sdk.api.object.SDIArtist;
import uk.co.sevendigital.android.sdk.api.object.SDIChart;
import uk.co.sevendigital.android.sdk.api.object.SDIChartItem;

public class ArtistChartsFragment extends ListFragment {

	private ArtistAdapter mAdapter;
	private List<SDIChartItem<SDIArtist>> mChartItems = new ArrayList<>();

	private boolean mDataLoaded;

	public static ArtistChartsFragment newInstance() {
		ArtistChartsFragment fragment = new ArtistChartsFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ArtistChartsFragment() {}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mAdapter = new ArtistAdapter(getActivity(), mChartItems);
		getListView().setAdapter(mAdapter);

		setListShown(false);

		//load chart items

	}

	@Override public void onResume() {
		super.onResume();
		if (!mDataLoaded) new ArtistChartAsync().execute();
	}

	public static class ArtistAdapter extends ArrayAdapter<SDIChartItem<SDIArtist>> {

		public ArtistAdapter(Context context, List<SDIChartItem<SDIArtist>> objects) {
			super(context, 0, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = ViewHolder.get(convertView, parent);
			SDIChartItem<SDIArtist> chartItem = getItem(position);

			vh.position.setText(Integer.toString(chartItem.getPosition()));
			vh.name.setText(chartItem.getItem().getName());

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
		ArtistDetailActivity.startActivity(getActivity(), mAdapter.getItem(position).getItem().getExternalId());
	}

	private class ArtistChartAsync extends AsyncTask<Void, Void, SDIChart<SDIArtist>> {

		private ArtistChartAsync() {}

		@Override protected SDIChart<SDIArtist> doInBackground(Void... params) {

			SDIGetArtistChartsRequest.Result result = null;

			SDIApi api = SampleApplication.getApi();

			try {
				result = api.artist().getCharts(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}

			if (null != result) return result.getChart();
			return null;
		}

		@Override protected void onPostExecute(SDIChart<SDIArtist> chart) {
			super.onPostExecute(chart);
			if (!isAdded()) return;
			mChartItems.clear();
			mChartItems.addAll(chart.getItems());
			mAdapter.notifyDataSetChanged();

			mDataLoaded = true;
			setListShown(true);

		}
	}

}
