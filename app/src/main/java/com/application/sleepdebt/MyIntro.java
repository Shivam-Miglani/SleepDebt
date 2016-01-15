package com.application.sleepdebt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by zomadmin on 06/12/15.
 */
public class MyIntro extends AppIntro2 {

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(first_fragment);
//        addSlide(second_fragment);
//        addSlide(third_fragment);
//        addSlide(fourth_fragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Welcome to\nSleep Debt Calculator", "Calculate the Right amount of sleep you should be getting.", R.drawable.intro, getResources().getColor(R.color.md_indigo_700)));

        addSlide(AppIntroFragment.newInstance("Keep track by\nSleep Logs", "\"Lannisters always pay their debts..\"", R.drawable.logs, getResources().getColor(R.color.md_indigo_700)));
        addSlide(AppIntroFragment.newInstance("Hour Settings &\nAlarms", "Adjust app according to your sleep cycle.", R.drawable.alarm, getResources().getColor(R.color.md_indigo_700)));


        // OPTIONAL METHODS
        // Override bar/separator color
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button
        //showSkipButton(false);
        //showDoneButton(false);

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        loadMainActivity();
    }

    @Override
    public void onSlideChanged(){
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}
