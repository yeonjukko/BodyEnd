package net.yeonjukko.bodyend.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.roomorama.caldroid.CaldroidFragment;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.BodyEndCalendarFragment;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    CaldroidFragment fg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        fg = new BodyEndCalendarFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.BodyEndCaldroid);


        fg.setArguments(args);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layoutCalendar, fg);
        t.commit();


    }

}
