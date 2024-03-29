package com.fanhantech.bottomdialog.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Animations util
 * Created by caik on 16/9/22.
 */

public class AnimationUtils {

	public static void slideToUp(View view) {
		Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

		slide.setDuration(200);
		slide.setFillAfter(true);
		slide.setFillEnabled(true);
		view.startAnimation(slide);

		slide.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

	}

	public static void slideToDown(View view, final AnimationListener listener) {
		Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

		slide.setDuration(200);
		slide.setFillAfter(true);
		slide.setFillEnabled(true);
		view.startAnimation(slide);

		slide.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (listener != null) {
					listener.onFinish();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

	}

	public interface AnimationListener {
		void onFinish();
	}
}
