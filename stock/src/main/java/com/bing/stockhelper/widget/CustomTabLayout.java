package com.bing.stockhelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.DecorView;
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.bing.stockhelper.R;
import com.google.android.material.R.attr;
import com.blankj.utilcode.util.SizeUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@DecorView
public class CustomTabLayout extends HorizontalScrollView {
	private static final int TAB_MIN_WIDTH_MARGIN = 56;

	private final ArrayList<Tab> tabs;
	private final LinearLayout tabsContainer;
	private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
	int tabSpacing;
	int tabPaddingStart;
	int tabPaddingTop;
	int tabPaddingEnd;
	int tabPaddingBottom;
	int tabTextAppearance;
	int selectedTabTextColor;
	int tabTextColor;
	float tabTextSize;
	ViewPager viewPager;
	private Tab selectedTab;
	private int contentInsetStart;
	private BaseOnTabSelectedListener currentVpSelectedListener;
	private PagerAdapter pagerAdapter;
	private DataSetObserver pagerAdapterObserver;
	private TabLayoutOnPageChangeListener pageChangeListener;
	private AdapterChangeListener adapterChangeListener;
	private boolean setupViewPagerImplicitly;

	public CustomTabLayout(Context context) {
		this(context, null);
	}

	public CustomTabLayout(Context context, AttributeSet attrs) {
		this(context, attrs, attr.tabStyle);
	}

	public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.tabs = new ArrayList<>();
		this.selectedListeners = new ArrayList<>();
		this.setHorizontalScrollBarEnabled(false);
		this.tabsContainer = new LinearLayout(context);
		super.addView(this.tabsContainer, 0, new LayoutParams(WRAP_CONTENT, MATCH_PARENT));
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTabLayout, defStyleAttr, 0);
		this.tabSpacing = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabSpacing_custom, TAB_MIN_WIDTH_MARGIN);
		this.tabPaddingStart = this.tabPaddingTop = this.tabPaddingEnd = this.tabPaddingBottom = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabPadding_custom, 0);
		this.tabPaddingStart = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabPaddingStart_custom, this.tabPaddingStart);
		this.tabPaddingTop = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabPaddingTop_custom, this.tabPaddingTop);
		this.tabPaddingEnd = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabPaddingEnd_custom, this.tabPaddingEnd);
		this.tabPaddingBottom = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabPaddingBottom_custom, this.tabPaddingBottom);
		this.tabTextAppearance = a.getResourceId(R.styleable.CustomTabLayout_tabTextAppearance_custom, androidx.appcompat.R.style.TextAppearance_AppCompat);

		this.tabTextSize = a.getDimension(R.styleable.CustomTabLayout_tabTextSize_custom, SizeUtils.sp2px(14));

		tabTextColor = a.getColor(R.styleable.CustomTabLayout_tabTextColor_custom, Color.GRAY);
		selectedTabTextColor = a.getColor(R.styleable.CustomTabLayout_tabSelectedTextColor_custom, Color.BLACK);

		this.contentInsetStart = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabContentStart_custom, 0);
		a.recycle();
		this.applyModeAndGravity();
	}

	void setScrollPosition(int position, float positionOffset) {
		int roundedPosition = Math.round((float) position + positionOffset);
		if (roundedPosition >= 0 && roundedPosition < this.tabsContainer.getChildCount()) {
			scrollTo(this.calculateScrollXForTab(position, positionOffset), 0);
			setSelectedTabView(roundedPosition);

			scaleTab(position, positionOffset);
			changeColor(position, positionOffset);
		}
	}

	private void scaleTab(int position, float positionOffset) {
		int nextPosition = position + 1;
		if (nextPosition >= tabs.size()) {
			nextPosition = tabs.size() -1;
			positionOffset = 1;
		}

		for (int i = 0; i < tabs.size(); i++) {
			Tab tab = tabs.get(i);
			if (i != position && i != nextPosition) {
				tab.view.setScaleX(1);
				tab.view.setScaleY(1);
			} else if (i == position) {
				if (position != nextPosition) {
					float scale1 = 1 + (1 - positionOffset) * 0.15f;
					tab.view.setScaleX(scale1);
					tab.view.setScaleY(scale1);
				}
			} else {
				float scale2 = 1 + positionOffset * 0.15f;
				tab.view.setScaleX(scale2);
				tab.view.setScaleY(scale2);
			}
		}
	}

	private void changeColor(int position, float positionOffset) {
//		System.out.println("hahahachange:" + position + "," + positionOffset);
		int nextPosition = position + 1;
		if (nextPosition >= tabs.size()) {
			nextPosition = tabs.size() -1;
			positionOffset = 1;
		}
		for (int i = 0; i < tabs.size(); i++) {
			Tab tab = tabs.get(i);
			if (i != position && i != nextPosition) {
				tab.setTextColor(tabTextColor);
			} else if (i == position) {
				if (position != nextPosition) {
					tab.setTextColor(getColor(tabTextColor, selectedTabTextColor, 1 - positionOffset));
				}
			} else {
				tab.setTextColor(getColor(tabTextColor, selectedTabTextColor, positionOffset));
			}
		}
	}

	private int getColor(int color1, int color2, @FloatRange(from = 0, to = 1) float offset) {
		float r1 = ((color1 >> 16) & 0xff) / 255.0f;
		float g1 = ((color1 >>  8) & 0xff) / 255.0f;
		float b1 = (color1 & 0xff) / 255.0f;
		float a1 = ((color1 >> 24) & 0xff) / 255.0f;
		float r2 = ((color2 >> 16) & 0xff) / 255.0f;
		float g2 = ((color2 >>  8) & 0xff) / 255.0f;
		float b2 = (color2 & 0xff) / 255.0f;
		float a2 = ((color2 >> 24) & 0xff) / 255.0f;
		float a = a1 * (1 - offset) + a2 * offset;
		float r = r1 * (1 - offset) + r2 * offset;
		float g = g1 * (1 - offset) + g2 * offset;
		float b = b1 * (1 - offset) + b2 * offset;
		return ((int) (a * 255.0f + 0.5f) << 24) |
				((int) (r  * 255.0f + 0.5f) << 16) |
				((int) (g * 255.0f + 0.5f) <<  8) |
				(int) (b  * 255.0f + 0.5f);
	}

	public void addTab(Tab tab) {
		tabs.add(tab);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
		if (tab.position > 0) {
			layoutParams.setMarginStart(tabSpacing);
		}
		tabsContainer.addView(tab.view, layoutParams);
	}

	public void addOnTabSelectedListener(@NonNull BaseOnTabSelectedListener listener) {
		if (!this.selectedListeners.contains(listener)) {
			this.selectedListeners.add(listener);
		}

	}

	public void removeOnTabSelectedListener(@NonNull BaseOnTabSelectedListener listener) {
		this.selectedListeners.remove(listener);
	}

	public void clearOnTabSelectedListeners() {
		this.selectedListeners.clear();
	}

	public int getTabCount() {
		return this.tabs.size();
	}

	@Nullable
	public Tab getTabAt(int index) {
		return index >= 0 && index < this.getTabCount() ? this.tabs.get(index) : null;
	}

	public int getSelectedTabPosition() {
		return this.selectedTab != null ? this.selectedTab.position : -1;
	}

	public void removeAllTabs() {
		for (int i = this.tabsContainer.getChildCount() - 1; i >= 0; --i) {
			tabsContainer.removeViewAt(i);
		}
		tabs.clear();
		this.selectedTab = null;
		requestLayout();
	}

	public int getSelectedTabTextColor() {
		return selectedTabTextColor;
	}

	public void setSelectedTabTextColor(int selectedTabTextColor) {
		this.selectedTabTextColor = selectedTabTextColor;
	}

	public int getTabTextColor() {
		return tabTextColor;
	}

	public void setTabTextColor(int tabTextColor) {
		this.tabTextColor = tabTextColor;
	}

	public void setupWithViewPager(@Nullable ViewPager viewPager) {
		this.setupWithViewPager(viewPager, true);
	}

	public void setupWithViewPager(@Nullable ViewPager viewPager, boolean autoRefresh) {
		this.setupWithViewPager(viewPager, autoRefresh, false);
	}

	private void setupWithViewPager(@Nullable ViewPager viewPager, boolean autoRefresh, boolean implicitSetup) {
		if (this.viewPager != null) {
			if (this.pageChangeListener != null) {
				this.viewPager.removeOnPageChangeListener(this.pageChangeListener);
			}

			if (this.adapterChangeListener != null) {
				this.viewPager.removeOnAdapterChangeListener(this.adapterChangeListener);
			}
		}

		if (this.currentVpSelectedListener != null) {
			this.removeOnTabSelectedListener(this.currentVpSelectedListener);
			this.currentVpSelectedListener = null;
		}

		if (viewPager != null) {
			this.viewPager = viewPager;
			if (this.pageChangeListener == null) {
				this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
			}

			viewPager.addOnPageChangeListener(this.pageChangeListener);
			this.currentVpSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
			this.addOnTabSelectedListener(this.currentVpSelectedListener);
			PagerAdapter adapter = viewPager.getAdapter();
			if (adapter != null) {
				this.setPagerAdapter(adapter, autoRefresh);
			}

			if (this.adapterChangeListener == null) {
				this.adapterChangeListener = new AdapterChangeListener();
			}

			this.adapterChangeListener.setAutoRefresh(autoRefresh);
			viewPager.addOnAdapterChangeListener(this.adapterChangeListener);
			this.setScrollPosition(viewPager.getCurrentItem(), 0.0F);
		} else {
			this.viewPager = null;
			this.setPagerAdapter(null, false);
		}

		this.setupViewPagerImplicitly = implicitSetup;
	}

	public boolean shouldDelayChildPressedState() {
		return this.getTabScrollRange() > 0;
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (this.viewPager == null) {
			ViewParent vp = this.getParent();
			if (vp instanceof ViewPager) {
				this.setupWithViewPager((ViewPager) vp, true, true);
			}
		}
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (this.setupViewPagerImplicitly) {
			this.setupWithViewPager(null);
			this.setupViewPagerImplicitly = false;
		}
	}

	private int getTabScrollRange() {
		return Math.max(0, this.tabsContainer.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
	}

	void setPagerAdapter(@Nullable PagerAdapter adapter, boolean addObserver) {
		if (this.pagerAdapter != null && this.pagerAdapterObserver != null) {
			this.pagerAdapter.unregisterDataSetObserver(this.pagerAdapterObserver);
		}

		this.pagerAdapter = adapter;
		if (addObserver && adapter != null) {
			if (this.pagerAdapterObserver == null) {
				this.pagerAdapterObserver = new PagerAdapterObserver();
			}

			adapter.registerDataSetObserver(this.pagerAdapterObserver);
		}

		this.populateFromPagerAdapter();
	}

	void populateFromPagerAdapter() {
		this.removeAllTabs();
		if (this.pagerAdapter != null) {
			int adapterCount = this.pagerAdapter.getCount();

			int curItem;
			for (curItem = 0; curItem < adapterCount; ++curItem) {
				CharSequence text = pagerAdapter.getPageTitle(curItem);
				Tab tab = new Tab(this, text, tabTextColor, Typeface.DEFAULT, curItem, false);
				this.addTab(tab);
			}

			if (this.viewPager != null && adapterCount > 0) {
				curItem = this.viewPager.getCurrentItem();
				if (curItem != this.getSelectedTabPosition() && curItem < this.getTabCount()) {
					this.selectTab(curItem);
				}
			}
		}
	}

	/**
	 * 滑过一半的时候调用
	 */
	private void setSelectedTabView(int position) {
		int tabCount = this.tabsContainer.getChildCount();
		if (position < tabCount) {
			for (int i = 0; i < tabCount; ++i) {
				tabs.get(i).setSelected(i == position);
			}
		}
	}

	/**
	 * onPageSelected是调用
	 */
	void selectTab(int position) {
		if (position < 0 || position > tabs.size()) {
			return;
		}
		Tab currentTab = this.selectedTab;
		Tab tab = tabs.get(position);
		if (currentTab == tab) {
			this.dispatchTabReselected(tab);
		} else {
			this.selectedTab = tab;
			if (currentTab != null) {
				this.dispatchTabUnselected(currentTab);
			}
			this.dispatchTabSelected(tab);
		}
	}

	private void selectViewPager(int position) {
		if (position < 0 || position >= viewPager.getChildCount()) {
			return;
		}
		if (viewPager.getCurrentItem() != position) {
			viewPager.setCurrentItem(position);
		}
	}

	private void dispatchTabSelected(@NonNull Tab tab) {
		for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
			this.selectedListeners.get(i).onTabSelected(tab);
		}

	}

	private void dispatchTabUnselected(@NonNull Tab tab) {
		for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
			this.selectedListeners.get(i).onTabUnselected(tab);
		}

	}

	private void dispatchTabReselected(@NonNull Tab tab) {
		for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
			this.selectedListeners.get(i).onTabReselected(tab);
		}
	}

	private int calculateScrollXForTab(int position, float positionOffset) {
			View selectedChild = this.tabsContainer.getChildAt(position);
			View nextChild = position + 1 < this.tabsContainer.getChildCount() ? this.tabsContainer.getChildAt(position + 1) : null;
			int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
			int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
			int scrollBase = selectedChild.getLeft() + selectedWidth / 2 - this.getWidth() / 2;
			int scrollOffset = (int) ((float) (selectedWidth + nextWidth) * 0.5F * positionOffset);
			return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR ? scrollBase + scrollOffset : scrollBase - scrollOffset;
	}

	private void applyModeAndGravity() {
		int paddingStart = Math.max(0, this.contentInsetStart - this.tabPaddingStart);

		ViewCompat.setPaddingRelative(this.tabsContainer, paddingStart, 0, 0, 0);
		this.tabsContainer.setGravity(Gravity.START);

		this.updateTabViews(true);
	}

	void updateTabViews(boolean requestLayout) {
		for (int i = 0; i < this.tabsContainer.getChildCount(); ++i) {
			View child = this.tabsContainer.getChildAt(i);
			if (requestLayout) {
				child.requestLayout();
			}
		}
	}

	public interface OnTabSelectedListener extends BaseOnTabSelectedListener<Tab> {
	}

	public interface BaseOnTabSelectedListener<T extends Tab> {
		void onTabSelected(T var1);

		void onTabUnselected(T var1);

		void onTabReselected(T var1);
	}

	public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
		private final ViewPager viewPager;

		public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
			this.viewPager = viewPager;
		}

		public void onTabSelected(Tab tab) {
			this.viewPager.setCurrentItem(tab.position);
		}

		public void onTabUnselected(Tab tab) {
		}

		public void onTabReselected(Tab tab) {
		}
	}

	public static class TabLayoutOnPageChangeListener implements OnPageChangeListener {
		private final WeakReference<CustomTabLayout> tabLayoutRef;

		public TabLayoutOnPageChangeListener(CustomTabLayout tabLayout) {
			this.tabLayoutRef = new WeakReference<>(tabLayout);
		}

		public void onPageScrollStateChanged(int state) {
		}

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			CustomTabLayout tabLayout = this.tabLayoutRef.get();
			if (tabLayout != null) {
				tabLayout.setScrollPosition(position, positionOffset);
			}
		}

		public void onPageSelected(int position) {
			CustomTabLayout tabLayout = this.tabLayoutRef.get();
			if (tabLayout != null && tabLayout.getSelectedTabPosition() != position && position < tabLayout.getTabCount()) {
				tabLayout.selectTab(position);
			}
		}
	}

	public class Tab {
		public CustomTabLayout parent;
		public TextView view;
		private CharSequence text;
		private int textColor;
		private Typeface typeface;
		private int position;
		private boolean selected;

		public Tab(CustomTabLayout parent, CharSequence text, int textColor, Typeface typeface, int position, boolean selected) {
			this.parent = parent;
			this.text = text;
			this.textColor = textColor;
			this.typeface = typeface;
			this.position = position;
			this.selected = selected;
			initTextView();
		}

		private void initTextView() {
			view = new TextView(parent.getContext());
			ViewCompat.setPaddingRelative(view, tabPaddingStart, tabPaddingTop, tabPaddingEnd, tabPaddingBottom);
			view.setGravity(Gravity.CENTER);
			view.setText(text);
			view.setTextColor(textColor);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
			view.setTypeface(typeface);
			view.setOnClickListener(v -> parent.selectViewPager(position));
		}

		public CharSequence getText() {
			return this.text;
		}

		public void setText(@Nullable CharSequence text) {
			this.text = text;
			view.setText(text);
		}

		public void setTextColor(int textColor) {
			this.textColor = textColor;
			view.setTextColor(textColor);
		}

		public int getTextColor() {
			return textColor;
		}

		public void setText(@StringRes int resId) {
			setText(parent.getResources().getText(resId));
		}

		public Typeface getTypeface() {
			return typeface;
		}

		public void setTypeface(Typeface typeface) {
			this.typeface = typeface;
			view.setTypeface(typeface);
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			if (this.selected != selected) {
				this.selected = selected;
				typeface = selected ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
				view.setTypeface(typeface);
			}
		}
	}

	private class AdapterChangeListener implements OnAdapterChangeListener {
		private boolean autoRefresh;

		AdapterChangeListener() {
		}

		public void onAdapterChanged(@NonNull ViewPager _viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
			if (viewPager == _viewPager) {
				setPagerAdapter(newAdapter, this.autoRefresh);
			}
		}

		void setAutoRefresh(boolean autoRefresh) {
			this.autoRefresh = autoRefresh;
		}
	}

	private class PagerAdapterObserver extends DataSetObserver {
		PagerAdapterObserver() {
		}

		public void onChanged() {
			populateFromPagerAdapter();
		}

		public void onInvalidated() {
			populateFromPagerAdapter();
		}
	}
}
