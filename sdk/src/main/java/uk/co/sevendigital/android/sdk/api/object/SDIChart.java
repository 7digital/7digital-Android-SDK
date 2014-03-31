package uk.co.sevendigital.android.sdk.api.object;

import java.util.Date;
import java.util.List;

public class SDIChart<T> {
	private int mTotalChartItemCount;
	private Date mFromDate;
	private int mPage;
	private int mTotalItems;
	private int mPageSize;
	private Date mToDate;
	private String mType;
	public List<SDIChartItem<T>> items;

	public SDIChart() {
	}

	public SDIChart(Composition composition) {
		if (composition == null) throw new NullPointerException("composition cannot be null");

		mTotalChartItemCount = composition.getTotalItems();
		mFromDate = composition.getFromDate();
		mPage = composition.getPage();
		mTotalItems = composition.getTotalItems();
		mPageSize = composition.getPageSize();
		mToDate = composition.getToDate();
		mType = composition.getType();
	}

	/** A chart is composed of the following methods */
	public static interface Composition {

		public abstract Date getFromDate();

		public abstract int getPage();

		public abstract int getTotalItems();

		public abstract int getPageSize();

		public abstract Date getToDate();

		public abstract String getType();
	}


	public List<SDIChartItem<T>> getItems() {
		return items;
	}
}
