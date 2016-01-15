package com.application.sleepdebt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


public class HomeActivity extends AppCompatActivity {

    TextView mHours;
    TextView mMinutes;
    TextView sub;
    int mOptimalHours = 8;
    AlertDialog levelDialog;
    private RangeBar hrRangebar;
    private RangeBar minRangebar;
    private int hourIndex;
    private int minuteIndex;
    SharedPreferences sharedpreferences;
    public static final String hours = "hourKey";
    public static final String minutes = "minutesKey";
    public static final String optimalhours = "optimalHoursKey";
    public static final String MyPREFERENCES = "MyPrefs";
    DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    FloatingActionsMenu fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(HomeActivity.this, MyIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupNavigationView();
        //Calculator logic
        mHours = (TextView) findViewById(R.id.hours);
        mMinutes = (TextView) findViewById(R.id.minutes);

        //Font
        sub = (TextView) findViewById(R.id.subcaption);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        sub.setTypeface(font);

        //sharedpref
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //Range bars
        hrRangebar = (RangeBar) findViewById(R.id.hourbar);
        hrRangebar.setSeekPinByIndex(6);

        hrRangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {

            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {

                String frightPinValue = String.format("%02d", rightPinIndex);
                mHours.setText(frightPinValue);
                hourIndex = rightPinIndex;

            }
        });

        minRangebar = (RangeBar) findViewById(R.id.minutebar);
        minRangebar.setSeekPinByIndex(9);
        minRangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                Integer value = rightPinIndex * 5;
                String frightPinValue = String.format("%02d", value);

                mMinutes.setText(frightPinValue);
                minuteIndex = rightPinIndex;


            }
        });

        //optimal hours
        mOptimalHours = sharedpreferences.getInt(optimalhours, 8);
        sub = (TextView) findViewById(R.id.subcaption);
        if (mOptimalHours == 7) {
            sub.setText(R.string.sub_caption1);
        } else if (mOptimalHours == 8) {
            sub.setText(R.string.sub_caption);
        } else if (mOptimalHours == 9) {
            sub.setText(R.string.sub_caption2);
        } else {
            mOptimalHours = 8;

        }


        //fam
        fam = (FloatingActionsMenu)findViewById(R.id.add_fab);
        final FrameLayout dimmedBackground = (FrameLayout) findViewById(R.id.shadowView);
        dimmedBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(fam.isExpanded()) {
                    fam.collapse();
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        dimmedBackground.getBackground().setAlpha(0);
        fam.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                dimmedBackground.getBackground().setAlpha(240);
            }

            @Override
            public void onMenuCollapsed() {
                dimmedBackground.getBackground().setAlpha(0);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(hours, hourIndex);
        editor.putInt(minutes, minuteIndex);
        editor.putInt(optimalhours, mOptimalHours);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        hrRangebar.setSeekPinByIndex(sharedpreferences.getInt(hours, 6));
        minRangebar.setSeekPinByIndex(sharedpreferences.getInt(minutes, 9));

        mOptimalHours = sharedpreferences.getInt(optimalhours, 8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void helpFunction(View view) {
        FloatingActionsMenu fam = (FloatingActionsMenu) findViewById(R.id.add_fab);
        fam.collapse();
        Intent helpIntent = new Intent(view.getContext(), HelpActivity.class);
        startActivity(helpIntent);
    }


    public void sleepdebt(View view) {

        String hours = mHours.getText().toString();
        String minutes = mMinutes.getText().toString();


        try {
            int h = Integer.parseInt(hours);
            int m = Integer.parseInt(minutes);
            if (h > 24 || (h == 24 && m >= 0)) {
                Toast toast = Toast.makeText(view.getContext(), "Sorry, can't have more than 24 hours in a day", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else {
                int totalmin = h * 60 + m;
                int debt = totalmin - ((24 * 60 - totalmin) * mOptimalHours) / (24 - mOptimalHours);


                int debthours = debt / 60;
                int debtminutes = debt % 60;
                String time;
                if (debt <= 0) {
                    time = "" + Math.abs(debthours) + " Hr : " + Math.abs(debtminutes) + " Min";
                } else {
                    time = "-" + Math.abs(debthours) + " Hr : " + Math.abs(debtminutes) + " Min";
                }


                Intent myIntent = new Intent(view.getContext(), DetailActivity.class);
                myIntent.putExtra("mytime", time);
                //myIntent.putExtra("mypayback", payback);
                myIntent.putExtra("hr", debthours);
                myIntent.putExtra("min", debtminutes);
                myIntent.putExtra("default", mOptimalHours);
                startActivity(myIntent);
            }
        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(view.getContext(), "Please enter a valid integer input", Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }


    }


    public void settingsFunction(View view) {

        FloatingActionsMenu fam = (FloatingActionsMenu) findViewById(R.id.add_fab);
        fam.collapse();
        final CharSequence[] items = {" 7 Hours ", " 8 Hours ", " 9 Hours "};
        AlertDialog.Builder builder =
                new AlertDialog.Builder(HomeActivity.this, R.style.StyledDialog);
        builder.setTitle("Choose hours to get fully rested:");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, -1 , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch (item) {
                    case 0:
                        mOptimalHours = 7;
                        break;
                    case 1:
                        mOptimalHours = 8;
                        break;
                    case 2:
                        mOptimalHours = 9;
                        break;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(optimalhours, mOptimalHours);
                editor.apply();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mOptimalHours == 7) {
                    Toast.makeText(HomeActivity.this, "Success: Standard changed to 7 hours", Toast.LENGTH_SHORT)
                            .show();
                    sub = (TextView) findViewById(R.id.subcaption);
                    sub.setText(R.string.sub_caption1);
                } else if (mOptimalHours == 9) {
                    Toast.makeText(HomeActivity.this, "Success: Standard changed to 9 hours", Toast.LENGTH_SHORT)
                            .show();
                    sub = (TextView) findViewById(R.id.subcaption);
                    sub.setText(R.string.sub_caption2);
                } else if (mOptimalHours == 8) {
                    Toast.makeText(HomeActivity.this, "Success: Standard set to 8 hours", Toast.LENGTH_SHORT)
                            .show();
                    sub = (TextView) findViewById(R.id.subcaption);
                    sub.setText(R.string.sub_caption);

                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        levelDialog = builder.create();
        levelDialog.show();

    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();

        assert getSupportActionBar() != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.navigation_item_2:
                        Intent logIntent = new Intent(HomeActivity.this, LogActivity.class);
                        startActivity(logIntent);
                        return true;
                    case R.id.navigation_item_3:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        final CharSequence[] items = {" 7 Hours ", " 8 Hours ", " 9 Hours "};
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(HomeActivity.this, R.style.StyledDialog);
                        builder.setTitle("Choose hours to get fully rested:");
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        mOptimalHours = 7;
                                        break;
                                    case 1:
                                        mOptimalHours = 8;
                                        break;
                                    case 2:
                                        mOptimalHours = 9;
                                        break;


                                }

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt(optimalhours, mOptimalHours);
                                editor.apply();
                            }
                        });
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mOptimalHours == 7) {
                                    Toast.makeText(HomeActivity.this, "Success: Standard changed to 7 hours", Toast.LENGTH_SHORT)
                                            .show();
                                    sub = (TextView) findViewById(R.id.subcaption);
                                    sub.setText(R.string.sub_caption1);
                                } else if (mOptimalHours == 9) {
                                    Toast.makeText(HomeActivity.this, "Success: Standard changed to 9 hours", Toast.LENGTH_SHORT)
                                            .show();
                                    sub = (TextView) findViewById(R.id.subcaption);
                                    sub.setText(R.string.sub_caption2);
                                } else if (mOptimalHours == 8) {
                                    Toast.makeText(HomeActivity.this, "Success: Standard changed back to 8 hours", Toast.LENGTH_SHORT)
                                            .show();
                                    sub = (TextView) findViewById(R.id.subcaption);
                                    sub.setText(R.string.sub_caption);

                                }
                                mNavigationView.setCheckedItem(R.id.navigation_item_1);
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        levelDialog = builder.create();
                        levelDialog.show();
                        return true;
                    case R.id.navigation_item_4:
                        Intent helpIntent = new Intent(HomeActivity.this, HelpActivity.class);
                        startActivity(helpIntent);
                        return true;
                    case R.id.navigation_item_5:
                        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                        }
                    default:
                        return true;
                }
            }
        });
    }




    @Override
    public void onStop() {
        super.onStop();
        hrRangebar.setSeekPinByIndex(sharedpreferences.getInt(hours, 6));
        hrRangebar.setTemporaryPins(true);
        minRangebar.setSeekPinByIndex(sharedpreferences.getInt(minutes, 9));
        minRangebar.setTemporaryPins(true);
        mOptimalHours = sharedpreferences.getInt(optimalhours, 8);
    }

}
