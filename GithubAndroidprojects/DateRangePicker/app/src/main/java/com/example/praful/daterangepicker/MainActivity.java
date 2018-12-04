package com.example.praful.daterangepicker;

import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private DuoMenuAdapter mMenuAdapter;
    DuoDrawerLayout mDuoDrawerLayout;
    DuoMenuView mDuoMenuView;
    Toolbar mToolbar;
    private ArrayList<String> mTitles = new ArrayList<>();


    private DateRangeCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
        mDuoMenuView.setOnMenuClickListener(this);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        mMenuAdapter = new DuoMenuAdapter(mTitles);
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mDuoDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        calendar = findViewById(R.id.calendar);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.fontj);
        calendar.setFonts(typeface);
        mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
        mDuoMenuView.setAdapter(mMenuAdapter);
        TextView noofemployess = mDuoMenuView.getHeaderView().findViewById(R.id.noofemployees);
        noofemployess.setText("Number of Employees BAE");
        final ArrayList<String> date = new ArrayList<>();
        if (date.size() > 0)
            date.clear();
        final ArrayList<String> dummytext = new ArrayList<>();
        final TextView text = (TextView) findViewById(R.id.text);
        final TextView text1 = new TextView(this);
        final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (int i = 0; i < 366; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            Date newDate = calendar.getTime();

            date.add((format.format(newDate)));

        }
        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {

            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                Calendar calendar = Calendar.getInstance();

                String startdate = startDate.getTime().toString();
                String enddate = endDate.getTime().toString();

                if (startDate.get(Calendar.DATE) >= calendar.get(Calendar.DATE)) {
                    String date1 = format.format(startDate.getTime());
                    String date2 = format.format(endDate.getTime());
                    String st = startdate.substring(0, 10) + " , " + startdate.substring(30, 34);
                    String end = enddate.substring(0, 10) + " , " + enddate.substring(30, 34);
                    if (st.equals(end))
                        text1.append("\n" + st);
                    else {

                        text1.append("\n" + st + " to " + end);
                    }
                    if (st.equals(end))

                        dummytext.add((date1));
                    else {
                        dummytext.add((date1));
                        dummytext.add((date2));
                    }
                    String result = "No";
                    int k = 0;
                    for (int i = 0; i < dummytext.size(); i++) {

                        for (int j = 0; j < 366; j++) {
                            if (date.get(j).equals(dummytext.get(i))) {
                                k++;
                            }
                        }

                    }
                    if (k == dummytext.size())
                        result = "Yes";

                    text.setText(text1.getText().toString() + "\n" + result);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please select valid Dates", Toast.LENGTH_SHORT).show();
                }
            }
           

        });

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, 0);
        Calendar later = (Calendar) now.clone();
        later.add(Calendar.MONTH, 50);

        calendar.setVisibleMonthRange(now, later);

        Calendar startSelectionDate = Calendar.getInstance();
        startSelectionDate.add(Calendar.DATE, 0);
        Calendar endSelectionDate = (Calendar) startSelectionDate.clone();
        endSelectionDate.add(Calendar.DATE, 40);

        // calendar.setSelectedDateRange(startSelectionDate, endSelectionDate);


    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {

        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            default:
                Toast.makeText(this, mTitles.get(position), Toast.LENGTH_SHORT).show();
                break;
        }

        // Close the drawer
        mDuoDrawerLayout.closeDrawer();


    }
}
