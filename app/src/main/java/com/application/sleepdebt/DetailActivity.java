package com.application.sleepdebt;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;


public class DetailActivity extends AppCompatActivity {

    String secondLine = null;
    String numberOfNights = null;
    FloatingActionsMenu fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView mdebt = (TextView) findViewById(R.id.debt);
        mdebt.setText(getIntent().getStringExtra("mytime"));
        final TextView payback = (TextView) findViewById(R.id.night);

        Spinner spinner = (Spinner) findViewById(R.id.night_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        final int debthours = getIntent().getIntExtra("hr", 0);
        final int debtminutes = getIntent().getIntExtra("min", 0);
        final int mOptimalHours = getIntent().getIntExtra("default", 8);

        if (debthours == 0 && debtminutes == 0) {
            TextView tv = (TextView) findViewById(R.id.pay_back);
            tv.setVisibility(View.GONE);
            FrameLayout fl = (FrameLayout) findViewById(R.id.result);
            fl.setBackgroundColor(getResources().getColor(R.color.md_light_green_500));
            spinner.setVisibility(View.GONE);
            payback.setText(R.string.success);
            secondLine = "No sleep debts.\nYou're right on time!";
        } else if (debthours > 0 || debtminutes > 0) {
            TextView tv = (TextView) findViewById(R.id.pay_back);
            FrameLayout fl = (FrameLayout) findViewById(R.id.result);
            fl.setBackgroundColor(getResources().getColor(R.color.md_deep_purple_300));
            tv.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            payback.setText(R.string.overslept);
            secondLine = "You overslept.\nRise & shine buddy!";
        } else {
            FrameLayout fl = (FrameLayout) findViewById(R.id.result);
            fl.setBackgroundColor(getResources().getColor(R.color.md_indigo_500));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int indexValue, long arg3) {

                    if (indexValue == 3) {
                        String str3 = getString(R.string.night, (Math.abs(debthours) + mOptimalHours), Math.abs(debtminutes));
                        CharSequence styledstr3 = Html.fromHtml(str3);
                        payback.setText(styledstr3);
                        numberOfNights = "";

                    } else if (indexValue == 2) {
                        String str2 = getString(R.string.nights, (Math.abs(debthours) / 2 + mOptimalHours), ((Math.abs(debthours) * 60 + Math.abs(debtminutes)) / 2) % 60);
                        CharSequence styledstr2 = Html.fromHtml(str2);
                        payback.setText(styledstr2);
                        numberOfNights = " for next two nights.";

                    } else if (indexValue == 1) {
                        String str1 = getString(R.string.nights, (Math.abs(debthours) / 3 + mOptimalHours), ((Math.abs(debthours) * 60 + Math.abs(debtminutes)) / 3) % 60);
                        CharSequence styledstr1 = Html.fromHtml(str1);
                        payback.setText(styledstr1);
                        numberOfNights = " for three nights.";

                    } else if (indexValue == 0) {
                        String str0 = getString(R.string.nights, (Math.abs(debthours) / 4 + mOptimalHours), ((Math.abs(debthours) * 60 + Math.abs(debtminutes)) / 4) % 60);
                        CharSequence styledstr0 = Html.fromHtml(str0);
                        payback.setText(styledstr0);
                        numberOfNights = " for four nights.";

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });

        }


        //fam
        fam = (FloatingActionsMenu) findViewById(R.id.add_fab);
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
                dimmedBackground.getBackground().setAlpha(200);
            }

            @Override
            public void onMenuCollapsed() {
                dimmedBackground.getBackground().setAlpha(0);
            }
        });

    }


    public void setAlarm(View view) {

        FloatingActionsMenu fam = (FloatingActionsMenu) findViewById(R.id.add_fab);
        fam.collapse();
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Sleep Debt Alarm");
        startActivity(i);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        }
    }

    public void saveLog(View view) {

        FloatingActionsMenu fam = (FloatingActionsMenu) findViewById(R.id.add_fab);
        fam.collapse();

        TextView debt = (TextView) findViewById(R.id.debt);
        TextView night = (TextView) findViewById(R.id.night);

        String firstLine = "Sleep Debt: " + debt.getText().toString();
        if (secondLine == null) {
            secondLine = "Repay " + night.getText().toString() + numberOfNights;
        }


        DatabaseHandler db = new DatabaseHandler(this);
        db.addLog(new LogItem(firstLine, secondLine));
        Intent myIntent = new Intent(view.getContext(), LogActivity.class);
        startActivity(myIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
