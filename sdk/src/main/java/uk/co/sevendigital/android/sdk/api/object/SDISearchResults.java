package uk.co.sevendigital.android.sdk.api.object;

import java.util.List;

public class SDISearchResults<T> {
	private int mPage;
	private int mTotalItems;
	private int mPageSize;
	public List<SDISearchResults.SDISearchResult<T>> mResults;

	public SDISearchResults() {}

	public SDISearchResults(Composition composition) {
		if (composition == null) throw new NullPointerException("composition cannot be null");

		mPage = composition.getPage();
		mTotalItems = composition.getTotalItems();
		mPageSize = composition.getPageSize();
		mResults = composition.getResults();
	}

	/** A chart is composed of the following methods */
	public static interface Composition<T> {

		public abstract int getPage();

		public abstract int getTotalItems();

		public abstract int getPageSize();

		public abstract List<SDISearchResults.SDISearchResult<T>> getResults();
	}

	public List<SDISearchResults.SDISearchResult<T>> getResults() {
		return mResults;
	}

	public static class SDISearchResult<T> {
		private String mType;
		private Double mScore;
		private T mResult;

		public SDISearchResult(SDISearchResult.Composition<T> composition) {
			mType = composition.getType();
			mScore = composition.getScore();
			mResult = composition.getResult();
		}

		/** A chart is composed of the following methods */
		public static interface Composition<T> {

			public abstract String getType();

			public abstract Double getScore();

			public abstract T getResult();
		}

		public String getType() {
			return mType;
		}

		public Double getScore() {
			return mScore;
		}

		public T getResult() {
			return mResult;
		}
	}

}
