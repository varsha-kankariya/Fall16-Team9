package prachihada.eatfoodv2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/*
    Activity where the app starts
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private AlarmManager alarmMgr;
    private static final int ALARM_REQ_ID = 1;
    //12 hr interval after which the alarm should fire again
    private static final int REPEAT_TIME = 43200000;
    //Repeat interval of 5 minutes
    //private static final int REPEAT_TIME = 300000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        System.out.println("Main Activity on create called!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Inventory(), "Food Inventory");
        viewPagerAdapter.addFragments(new FoodExpired(), "Food Expired");
        viewPagerAdapter.addFragments(new FoodWasted(), "Food Wasted");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        int color = Color.parseColor("#AE6118"); //The color u want
        fab.setColorFilter(45646);

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        createAlarmForApp();

        //cancelAlarm();

    }

    private void createAlarmForApp() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,9);
        //calendar.set(Calendar.HOUR_OF_DAY,21);
        //calendar.set(Calendar.MINUTE,35);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQ_ID, new Intent(this,AlarmEventReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //Create alarm manager to run on a specific day and specific time and repeat after 3 minutes
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),REPEAT_TIME, pendingIntent);


    }

    public void pageAddItem(View view) {
        Intent intent = new Intent(this, AddFoodItemsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void cancelAlarm(){

        Intent notificationIntent = new Intent(this, AlarmEventReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQ_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        System.out.println("Cancelled alarm");
        alarmMgr.cancel(pendingIntent);
    }

}
