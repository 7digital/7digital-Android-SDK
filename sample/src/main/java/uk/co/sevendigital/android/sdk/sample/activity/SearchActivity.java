package uk.co.sevendigital.android.sdk.sample.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uk.co.sevendigital.android.sdk.sample.R;
import uk.co.sevendigital.android.sdk.sample.fragment.search.ArtistSearchFragment;
import uk.co.sevendigital.android.sdk.sample.fragment.search.ReleaseSearchFragment;
import uk.co.sevendigital.android.sdk.sample.fragment.search.TrackSearchFragment;

public class SearchActivity extends Activity implements ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	public static Intent startActivity(Activity activity) {
		Intent intent = new Intent(activity, SearchActivity.class);
		activity.startActivity(intent);
		return intent;
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
		new NavSpinner(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
				getString(R.string.title_artist_section), getString(R.string.title_album_section),
				getString(R.string.title_track_section) }), this);
	}

	@Override public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override public boolean onNavigationItemSelected(int position, long id) {
		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = ArtistSearchFragment.newInstance();
			break;
		case 1:
			fragment = ReleaseSearchFragment.newInstance();
			break;
		case 2:
			fragment = TrackSearchFragment.newInstance();
			break;
		}

		if (null != fragment) getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		return true;
	}

	private class NavSpinner extends ArrayAdapter<String> {

		private NavSpinner(Context context, int resource, int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(getContext()).inflate(R.layout.ab_row, null);
			((TextView) view.findViewById(R.id.textview1)).setText(getString(R.string.charts));
			((TextView) view.findViewById(R.id.textview2)).setText(getItem(getActionBar().getSelectedNavigationIndex()).toUpperCase());
			return view;
		}

		@Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, null, parent);
		}
	}

}
