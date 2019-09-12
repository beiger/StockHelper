package com.fanhantech.baselib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fanhantech.baselib.R;
import com.fanhantech.baselib.model.datawrapper.Status;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 里面只有一个layout，类似ScrollView
 */
public class StatusLayout extends FrameLayout {

	private static final int NULL_RESOURCE_ID = -1;
	private LayoutParams mLayoutParams;
	private View mEmptyView;
	private View mErrorView;
	private View mLoadingView;
	private View mContentView;
	private int mEmptyViewResId;
	private int mErrorViewResId;
	private int mLoadingViewResId;
	private int mContentViewResId;
	private Status mViewStatus;
	private LayoutInflater mInflater;
	private OnClickListener mOnEmptyClickListener;
	private OnClickListener mOnErrorClickListener;
	private OnClickListener mOnLoadingClickListener;

	public StatusLayout(Context context) {
		this(context, null);
	}

	public StatusLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		final TypedArray a =
				context.obtainStyledAttributes(attrs, R.styleable.StatusLayout, defStyleAttr, 0);

		mEmptyViewResId = a.getResourceId(R.styleable.StatusLayout_emptyView, R.layout.layout_empty);
		mErrorViewResId = a.getResourceId(R.styleable.StatusLayout_errorView, R.layout.layout_no_network);
		mLoadingViewResId = a.getResourceId(R.styleable.StatusLayout_loadingView, R.layout.layout_loading);
		mContentViewResId = a.getResourceId(R.styleable.StatusLayout_successView, NULL_RESOURCE_ID);
		mInflater = LayoutInflater.from(getContext());
		a.recycle();

		mLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		mLayoutParams.gravity = Gravity.CENTER;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (mContentViewResId != NULL_RESOURCE_ID) {
			mContentView = findViewById(mContentViewResId);
		} else {
			if (getChildCount() > 1) {
				try {
					throw new StatusException("content is not a single View or Layout");
				} catch (StatusException e) {
					e.printStackTrace();
				}
			}
			mContentView = getChildAt(0);
		}
	}

	static class StatusException extends Exception {
		StatusException(String message) {
			super(message);
		}
	}

	/**
	 * 获取当前状态
	 */
	public Status getViewStatus() {
		return mViewStatus;
	}

	public void setOnEmptyClickListener(OnClickListener onEmptyClickListener) {
		mOnEmptyClickListener = onEmptyClickListener;
	}

	public void setOnErrorClickListener(OnClickListener onErrorClickListener) {
		mOnErrorClickListener = onErrorClickListener;
	}

	public void setOnLoadingClickListener(OnClickListener onLoadingClickListener) {
		mOnLoadingClickListener = onLoadingClickListener;
	}

	public void showViewByStatus(Status viewStatus) {
		mViewStatus = viewStatus;
		createView(viewStatus);
		if (null != mLoadingView) {
			mLoadingView.setVisibility(viewStatus == Status.LOADING ? View.VISIBLE : View.GONE);
		}
		if (null != mEmptyView) {
			mEmptyView.setVisibility(viewStatus == Status.EMPTY ? View.VISIBLE : View.GONE);
		}
		if (null != mErrorView) {
			mErrorView.setVisibility(viewStatus == Status.ERROR ? View.VISIBLE : View.GONE);
		}
		if (null != mContentView) {
			mContentView.setVisibility(viewStatus == Status.SUCCESS ? View.VISIBLE : View.GONE);
		}
	}

	private void createView(Status viewStatus) {
		switch (viewStatus) {
			case LOADING:
				mLoadingView = createView(mLoadingView, mLoadingViewResId, mOnLoadingClickListener);
				break;

			case EMPTY:
				mEmptyView = createView(mEmptyView, mEmptyViewResId, mOnEmptyClickListener);
				break;

			case ERROR:
				mErrorView = createView(mErrorView, mErrorViewResId, mOnErrorClickListener);
				break;

			default:
				break;
		}
	}

	private View createView(View view, int resId, OnClickListener listener) {
		if (view == null) {
			view = mInflater.inflate(resId, null);
			addView(view, 0, mLayoutParams);
			view.setLayoutParams(mLayoutParams);
			view.setOnClickListener(listener);
		}
		return view;
	}

}
