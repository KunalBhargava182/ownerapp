package com.HC.KB.KP.ownerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    Button next, skip;
    private LinearLayout dotsLayout;
    private int[] layouts;
    Intromanager intromanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_welcome);

        // Importing the intromanager class which will check if this is the first time the application has been launchedd.
        intromanager = new Intromanager(this);
        if (!intromanager.isFirstTimeLaunch()) {
            launchHomeScreen();
           /* intromanager.setKidsvak(false);
            Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(i);*/
            finish();
        }
        setContentView(R.layout.activity_welcome);
        //Objects
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        skip = findViewById(R.id.btn_skip);
        next = findViewById(R.id.btn_next);
        layouts = new int[]{R.layout.slider_1, R.layout.slider_2, R.layout.slider_3};
        addBottomDots(0);
        changeStatusBarColor();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* intromanager.Check();
                intromanager.setKidsvak(true);
                Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();*/
                launchHomeScreen();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem();
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if (position == layouts.length - 1) {
                next.setText("PROCEED");
                skip.setVisibility(View.GONE);
            } else {
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    //Giving the dots functionality
    private void addBottomDots(int position) {

        TextView[] dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[position].setTextColor(colorActive[position]);
    }

    private void launchHomeScreen() {
        intromanager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
        finish();
    }

    private class ViewPagerAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup myContainer, int mPosition) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[mPosition], myContainer, false);
            myContainer.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View mView, @NonNull Object mObject) {
            return mView == mObject;
        }

        @Override
        public void destroyItem(ViewGroup mContainer, int mPosition, @NonNull Object mObject) {
            View v = (View) mObject;
            mContainer.removeView(v);
        }
    }
}