package com.application.sleepdebt;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zomadmin on 30/11/15.
 */
public class LogActivity extends AppCompatActivity {

    List<LogItem> loglist= new ArrayList<LogItem>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayout ll = (LinearLayout)findViewById(R.id.ll);







        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());

        DatabaseHandler db = new DatabaseHandler(this);
        loglist= db.getAllLogs();
        if(db.getLogsCount()==0){
            ll.setVisibility(View.VISIBLE);
        }



        // use your custom layout
        final RVAdapter adapter = new RVAdapter(loglist,LogActivity.this);
        rv.setAdapter(adapter);





    }


    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }


}
