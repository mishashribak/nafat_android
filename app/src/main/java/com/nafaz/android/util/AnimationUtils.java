package com.nafaz.android.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

import androidx.annotation.RequiresApi;

import com.nafaz.android.R;
import com.nafaz.android.app.App;

import org.jetbrains.annotations.NotNull;

public class AnimationUtils {

    public static void animateSlideDownFromTop(@NotNull View v) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_down_from_top);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), 100);
    }

    public static void animateSlideTopFromDown(@NotNull View v) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_top_from_down);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), 100);
    }

    public static void animateSlideTopFromDown(@NotNull View v, int delay) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_top_from_down);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), delay);
    }

    public static void animateZoomOut(@NotNull View v) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.view_zoom_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), 100);
    }

    public static void animateSlideFromLeft(@NotNull View v) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_from_left);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), 100);
    }

    public static void animateSlideFromLeft(@NotNull View v, int delay) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_from_left);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), delay);
    }

    public static void animateSlideFromRight(@NotNull View v) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_from_right);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), 100);
    }

    public static void animateSlideFromRight(@NotNull View v, int delay) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.slide_from_right);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.postDelayed(() -> v.startAnimation(animation), delay);
    }

    public static void animateClick(@NotNull View v) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.view_click);

        v.startAnimation(animation);
    }

    public static void animateScale(@NotNull final View view) {
        final ScaleAnimation fadeIn = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fadeIn.setDuration(750);
        fadeIn.setFillAfter(true);
        view.startAnimation(fadeIn);
    }

    public static void animateFade(@NotNull final View view, int delay) {
        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(
                App.getContext(), R.anim.fade_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.postDelayed(() -> view.startAnimation(animation), delay);
    }

    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void circleReveal(@NotNull Activity activity, @NotNull View view, int posFromRight, boolean containsOverflow, final boolean isShow) {

        int width = view.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * activity.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (activity.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= activity.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = view.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            view.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();
    }

    public static void animateLayoutChildren(Context context, @NotNull ViewGroup parentLayout, int animationResId, long startOffset, float delay) {

        final Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, animationResId);
        animation.setStartOffset(startOffset);

        final LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(delay);

        parentLayout.setLayoutAnimation(controller);
        parentLayout.startLayoutAnimation();
    }
}
