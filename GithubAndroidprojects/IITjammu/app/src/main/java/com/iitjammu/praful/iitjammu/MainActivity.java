package com.iitjammu.praful.iitjammu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener, AAH_FabulousFragment.Callbacks {


    DuoDrawerLayout mDuoDrawerLayout;
    DuoMenuView mDuoMenuView;
    private DuoMenuAdapter mMenuAdapter;
    Toolbar mToolbar;
    ArrayList<String> mTitles = new ArrayList<>();
    LoopingViewPager viewPager;
    LoopInfiniteAdapter adapter;


    @Override
    protected void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    protected void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }

    private ArrayList<Integer> createDummyItems() {
        ArrayList<Integer> items = new ArrayList<>();
        items.add(0, 1);
        items.add(1, 2);
        items.add(2, 3);
        return items;
    }

    FabFragment dialogFrag;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    List<Eventitem> eventitems = new ArrayList<>();
    List<Publicationitem> publicationitems = new ArrayList<>();
    RecyclerView eventlist;
    RecyclerView publicationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        mMenuAdapter = new DuoMenuAdapter(mTitles);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDuoMenuView.setOnMenuClickListener(this);
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mDuoDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        LinearLayout home = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.home);
        LinearLayout about = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.about);
        LinearLayout campus = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.campus);

        TextView Home = (TextView) home.findViewById(R.id.Home);
        Home.setTextColor(Color.GRAY);
        viewPager = findViewById(R.id.viewpager);
        adapter = new LoopInfiniteAdapter(this, createDummyItems(), true);
        viewPager.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

        duoDrawerToggle.setDrawerIndicatorEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter();

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(MainActivity.this, About.class);
                        startActivity(intent);

                    }
                }.start();
            }
        });
        campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(MainActivity.this, Campus.class);
                        startActivity(intent);

                    }
                }.start();
            }
        });
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        dialogFrag = FabFragment.newInstance();
        dialogFrag.setParentFab(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogFrag.setCallbacks(MainActivity.this);
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });


        eventlist = (RecyclerView) findViewById(R.id.events);
        eventlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        publicationlist = (RecyclerView) findViewById(R.id.publications);
        publicationlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Eventitem eventitem = new Eventitem("https://cdn-30-skcir4i63ajp.netdna-ssl.com/wp-content/uploads/2018/07/swachh2.jpg", "Event Sach Bharat");
        eventitems.add(eventitem);
        Eventitem eventitem1 = new Eventitem("https://www.hindustantimes.com/rf/image_size_640x362/HT/p2/2016/06/11/Pictures/chandigarh-wednesday-college-hindustan-students-college-others_1eced6c6-2f45-11e6-85eb-521f5a9851b5.JPG", "Event Yoga Day");
        eventitems.add(eventitem1);

        Publicationitem publicationitem = new Publicationitem("https://www.itsnicethat.com/system/files/092017/59ae5ccb7fa44c9e4d0077a7/images_slice_large/Studio-Fnt-Highlights-graphic-design-itsnicethat-8.png?1504599642", "Jagti");
        Publicationitem publicationitem1 = new Publicationitem("https://i.pinimg.com/originals/f1/36/54/f136549a792c6c03e2709c4c4b44b6dc.jpg", "Muse");
        publicationitems.add(publicationitem);
        publicationitems.add(publicationitem1);

        Eventadapter eventadapter = new Eventadapter(eventitems, MainActivity.this);
        eventlist.setAdapter(eventadapter);
        PublicationAdapter publicationAdapter = new PublicationAdapter(publicationitems, MainActivity.this);
        publicationlist.setAdapter(publicationAdapter);




    }


    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {

    }

    public class SectionsPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.contentview, collection, false);
            FlexboxLayout fbl = (FlexboxLayout) layout.findViewById(R.id.fbl);
//            LinearLayout ll_scroll = (LinearLayout) layout.findViewById(R.id.ll_scroll);
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels-(104*metrics.density)));
//            ll_scroll.setLayoutParams(lp);

            switch (position) {
                case 0:
                    View view = inflater.inflate(R.layout.latestnews, null);
                    fbl.addView(view);
                    break;
                case 1:
                    View view1 = inflater.inflate(R.layout.tenders, null);
                    fbl.addView(view1);
                    break;
                case 2:
                    View view2 = inflater.inflate(R.layout.academics, null);
                    fbl.addView(view2);
                    break;
                case 3:
                    View view3 = inflater.inflate(R.layout.quicklinks, null);
                    fbl.addView(view3);
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
                    return "Latest News";
                case 1:
                    return "Tenders";
                case 2:
                    return "Academics";
                case 3:
                    return "Quick Links";
            }
            return "";
        }


        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

            return view == o;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialogFrag.isAdded()) {
            dialogFrag.dismiss();
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
        }

    }

    @Override
    public void onResult(Object result) {

    }
}
