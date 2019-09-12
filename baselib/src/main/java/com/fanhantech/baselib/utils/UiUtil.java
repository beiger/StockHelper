package com.fanhantech.baselib.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;

public class UiUtil {
	/**
	 * fitSystemsWindow=true就是让布局延伸到状态栏和导航栏下面，同时设置一个对应大小的padding(如果布局是固定大小，内容就会错位)，在padding中background是会显示的，而src或者其他子控件不会显示。
	 * 1. 如果要使状态栏的背景与content的背景相同，只需要把对应view设置background，同时把此view设置fitSystemsWindow=true, 父控件不要设置（CoordinateLayout是个例外）。
	 * 2. 如果状态栏颜色与content不需要相同，只是单纯的某个颜色，那就window.setStatusBarColor(color)，同时不要设置fitSystemsWindow=true。
	 * 3. 有时背景图片并不正好可用，需要设置scaleType，这时就不能用background，通过设置fitSystemsWindow=true的方式就不能实现，可以先设置全屏，然后手动给某个view设置padding，即setBarColorAndFontBlackByChangeView的方式。
	 */


	/**
	 * 会使activity全屏
	 * 1. 如果状态栏是单一的颜色，可以直接在xml中添加fitSystemWindow=true，比较简便;
	 *      （如果color有透明度，而且布局有背景颜色，颜色叠加会导致不一致，比较少见）
	 * 2. 如果要实现沉浸式，可以用FullscreenActivity的方式,或者下面的方式
	 */
	public static void setBarColorAndFontBlack(Activity activity, int color) {
		Window window = activity.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window.setStatusBarColor(color);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
				} else {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				}
			} else {
				window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}
	}

	public static void setBarColorAndFontWhite(Activity activity, int color) {
		Window window = activity.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window.setStatusBarColor(color);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				} else {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				}
			} else {
				window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}
	}

	/**
	 * 不需要fitSystemWindow,注意只能使用一次
	 */
	public static void setBarColorAndFontBlackByChangeView(Activity activity, int color, View view) {
		Window window = activity.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window.setStatusBarColor(color);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
				} else {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				}
			} else {
				window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
			ViewGroup.LayoutParams params = view.getLayoutParams();
			int statusHeight = BarUtils.getStatusBarHeight();
			params.height += statusHeight;
			view.setLayoutParams(params);
			view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusHeight, view.getPaddingRight(), view.getPaddingBottom());
		}
	}

	/**
	 * 不需要fitSystemWindow
	 */
	public static void setBarColorAndFontWhiteByChangeView(Activity activity, int color, View view) {
		Window window = activity.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window.setStatusBarColor(color);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				} else {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				}
			} else {
				window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
			ViewGroup.LayoutParams params = view.getLayoutParams();
			int statusHeight = BarUtils.getStatusBarHeight();
			params.height += statusHeight;
			view.setLayoutParams(params);
			view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusHeight, view.getPaddingRight(), view.getPaddingBottom());
		}
	}


	/**
	 * 实现类似斗鱼播放视频时的页面，隐藏状态栏和导航栏，点击屏幕显示
	 * @param activity
	 */
	public static void hideStatusAndNavigation(Activity activity) {
		if (Build.VERSION.SDK_INT >= 19) {
			View decorView = activity.getWindow().getDecorView();
			decorView.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

}
