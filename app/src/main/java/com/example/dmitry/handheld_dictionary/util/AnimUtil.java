package com.example.dmitry.handheld_dictionary.util;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.ui.anim.Anchor;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public final class AnimUtil {

    private AnimUtil() {
    }

    public static void showWithAlphaAnim(@NonNull final Context context, @NonNull final View view) {
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.show_alpha);
        view.startAnimation(animation);
    }

    public static void hideWithAlphaAnim(@NonNull final Context context, @NonNull final View view) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.hide_alpha);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override public void onAnimationStart(Animation animation) {
            }
            @Override public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
            @Override public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(animation);
    }

    public static void showWithRippleAnimation(@NonNull final Context context, @NonNull final View view) {
        ViewUtil.setVisibility(view, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    view.getWidth() / 2,
                    view.getTop(),
                    0,
                    Math.max(view.getWidth(), view.getHeight()));

            animator.start();
        } else {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.hide_alpha);
            view.startAnimation(animation);
        }
    }

    public static void hideWithRippleAnimation(
            @NonNull final Context context,
            @NonNull final View view,
            @NonNull final Anchor anchor) {
        hideWithRippleAnimation(context, view, anchor, null);
    }

    public static void hideWithRippleAnimation(
            @NonNull final Context context,
            @NonNull final View view,
            @NonNull final Anchor anchor,
            @Nullable final Animator.AnimatorListener animatorListener) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            final Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    anchor.getX(view),
                    anchor.getY(view),
                    Math.max(view.getWidth(), view.getHeight()),
                    0);

            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animation) {
                    if (animatorListener != null) {
                        animatorListener.onAnimationStart(animation);
                    }
                }
                @Override public void onAnimationEnd(@NonNull Animator animation) {
                    if (animatorListener != null) {
                        animatorListener.onAnimationEnd(animation);
                    }
                    ViewUtil.setVisibility(view, false);
                }
                @Override public void onAnimationCancel(Animator animation) {
                    if (animatorListener != null) {
                        animatorListener.onAnimationCancel(animation);
                    }
                }
                @Override public void onAnimationRepeat(Animator animation) {
                    if (animatorListener != null) {
                        animatorListener.onAnimationRepeat(animation);
                    }
                }
            });

        } else {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.hide_alpha);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {
                }

                @Override public void onAnimationEnd(Animation animation) {
                    ViewUtil.setVisibility(view, false);
                }

                @Override public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(animation);
        }
    }
}
