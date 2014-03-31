package uk.co.sevendigital.android.sdk.sample.fragment.search;

import android.app.ListFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.sevendigital.android.sdk.sample.R;

public abstract class AbsSearchFragment extends ListFragment {

	@InjectView(R.id.search_edittext) protected EditText mSearchEdittext;
	@InjectView(R.id.progressBar) protected ProgressBar mProgressBar;
	@InjectView(R.id.list_container) FrameLayout mListContainer;

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, null);

		ButterKnife.inject(this, view);
		mSearchEdittext.setHint(String.format("%s name", getTitle()));
		mSearchEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					doSearch();
					return true;
				}

				if (actionId == EditorInfo.IME_ACTION_DONE) {
					doSearch();
					return true;
				}
				return false;
			}
		});
		return view;
	}

	protected void setProgressVisible(boolean visible) {
		mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
		mListContainer.setVisibility(visible ? View.GONE : View.VISIBLE);
	}

	protected void doSearch() {
		setProgressVisible(true);
		//check the user entered text
		mSearchEdittext.setError(null);
		if (TextUtils.isEmpty(mSearchEdittext.getText())) {
			mSearchEdittext.setError(getString(R.string.enter_search_term));
			return;
		} else if (mSearchEdittext.getText().length() < 3) {
			mSearchEdittext.setError(getString(R.string.too_short));
			return;
		}
	}

	protected abstract String getTitle();

}
