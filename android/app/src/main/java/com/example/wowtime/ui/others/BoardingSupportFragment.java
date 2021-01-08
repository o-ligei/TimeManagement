package com.example.wowtime.ui.others;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.OnboardingSupportFragment;
import androidx.preference.PreferenceManager;
import com.example.wowtime.R;

public class BoardingSupportFragment extends OnboardingSupportFragment {
    public static final String COMPLETED_ONBOARDING_PREF_NAME = "COMPLETED_ONBOARDING_PREF_NAME";
    private static final long ANIMATION_DURATION = 2000;

    private String[] titles;
    private String[] descs;
    private int[] contentImags={R.drawable.alarm_page,R.drawable.away_from_phone_page,R.drawable.sun_flower_page};
    ImageView contentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加初始徽标屏幕，会在显示第一页之前显示ic_launcher
        String title1=getResources().getString(R.string.on_board_title_1);
        String title2=getResources().getString(R.string.on_board_title_2);
        String title3=getResources().getString(R.string.on_board_title_3);
        titles= new String[]{title1, title2, title3};
        descs= new String[]{getResources().getString(R.string.on_board_des_1),
                getResources().getString(R.string.on_board_des_2),
                getResources().getString(R.string.on_board_des_3)};
        setLogoResourceId(R.mipmap.ic_launcher);
    }

    @Override
    public int onProvideTheme() {
//        这里设置主题，或者直接设置该Fragment对应的Activity的主题为R.style.Theme_Leanback_Onboarding;
        return R.style.Theme_Leanback_Onboarding;
    }

    @Nullable
    @Override
    protected Animator onCreateEnterAnimation() {
        //第一个页面上显示的动画
        return ObjectAnimator.ofFloat(contentView,
                                      View.SCALE_X, 0.2f, 1.0f).setDuration(ANIMATION_DURATION);
    }

    @Override
    protected int getPageCount() {
        return titles.length;
    }

    @Override
    protected CharSequence getPageTitle(int pageIndex) {
        return titles[pageIndex];
    }

    @Override
    protected CharSequence getPageDescription(int pageIndex) {
        return descs[pageIndex];
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        //背景View是一个全屏的View
        return null;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        //这里需要将contentView作为全局变量，onPageChanged方法中更新图片，背景和前景图片类似
        contentView = new ImageView(getContext());
        contentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int pageIndex = getCurrentPageIndex();
        contentView.setImageResource(contentImags[pageIndex]);
        contentView.setPadding(0, 32, 0, 32);
        return contentView;
    }

    @Override
    protected void onPageChanged(final int newPage, int previousPage) {
        super.onPageChanged(newPage, previousPage);
//        //当界面发生变化时，contentView设置对应位置的图片，背景和前景图片类似
//        contentView.setImageResource(contentImags[newPage]);

        // Create a fade-out animation used to fade out previousPage and, once
        // done, swaps the contentView image with the next page's image.
        Animator fadeOut = ObjectAnimator.ofFloat(contentView,
                                                  View.ALPHA, 1.0f, 0.0f).setDuration(ANIMATION_DURATION);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //当界面发生变化时，contentView设置对应位置的图片，背景和前景图片类似
                contentView.setImageResource(contentImags[newPage]);
            }
        });
        // Create a fade-in animation used to fade in nextPage
        Animator fadeIn = ObjectAnimator.ofFloat(contentView,
                                                 View.ALPHA, 0.0f, 1.0f).setDuration(ANIMATION_DURATION);
        // Create AnimatorSet with our fade-out and fade-in animators, and start it
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(fadeOut, fadeIn);
        set.start();

    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        //前景View是一个全屏的View，因此添加View的时候需要考虑到子View对应的位置
        return null;
    }

    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        // User has seen OnboardingSupportFragment, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingSupportFragment
        // the next time the user launches the app.
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        sharedPreferencesEditor.putBoolean(
                COMPLETED_ONBOARDING_PREF_NAME, true);
        sharedPreferencesEditor.apply();
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            fragmentActivity.finish();
        }
    }
}
