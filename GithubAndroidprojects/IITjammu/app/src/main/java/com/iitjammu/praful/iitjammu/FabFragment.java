package com.iitjammu.praful.iitjammu;

import android.app.Dialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.flexbox.FlexboxLayout;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class FabFragment extends AAH_FabulousFragment {


    public static FabFragment newInstance() {
        FabFragment f = new FabFragment();
        return f;
    }

    SpeedDialView speedDialView;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override

    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fabbottomview, null);
        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);
        contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) contentView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        speedDialView = (SpeedDialView) contentView.findViewById(R.id.more);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_linkedin, R.drawable.linkedin)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Linkedin")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_google, R.drawable.google)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Google")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_youtube, R.drawable.youtube)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Youtube")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_twitter, R.drawable.twitter)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Twitter")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.setExpansionMode(SpeedDialView.ExpansionMode.RIGHT);
        speedDialView.setOrientation(LinearLayout.HORIZONTAL);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //params to set
        setAnimationDuration(400); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        setCallbacks((Callbacks) getActivity()); //optional; to get back result
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewPager(mViewPager); //optional; if you use viewpager that has scrollview
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

    public class SectionsPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fabbottomcontent, collection, false);
            FlexboxLayout fbl = (FlexboxLayout) layout.findViewById(R.id.fbl);
//            LinearLayout ll_scroll = (LinearLayout) layout.findViewById(R.id.ll_scroll);
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels-(104*metrics.density)));
//            ll_scroll.setLayoutParams(lp);

            switch (position) {
                case 0:
                    View view = inflater.inflate(R.layout.faculty, null);
                    fbl.addView(view);
                    break;
                case 1:
                    View view1 = inflater.inflate(R.layout.videogallery, null);
                    fbl.addView(view1);
                    break;
                case 2:
                    View view2 = inflater.inflate(R.layout.gallery, null);
                    fbl.addView(view2);
                    break;

            }
            collection.addView(layout);
            return layout;


        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Faculty";
                case 1:
                    return "Video Gallery";
                case 2:
                    return "Gallery";
            }
            return "";
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

            return view == o;
        }
    }

}
