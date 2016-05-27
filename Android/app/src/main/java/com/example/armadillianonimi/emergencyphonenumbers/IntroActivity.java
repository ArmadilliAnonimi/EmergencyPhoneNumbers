package com.example.armadillianonimi.emergencyphonenumbers;

import android.os.Bundle;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends OnboarderActivity {

    List<OnboarderPage> onboarderPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onboarderPages = new ArrayList<OnboarderPage>();

        // Each page of the tutorial is created here
        OnboarderPage onboarderPage1 = new OnboarderPage(R.string.welcome, R.string.thanks,R.mipmap.ic_launcher);
        OnboarderPage onboarderPage2 = new OnboarderPage(R.string.call_title, R.string.call_description, R.drawable.call);
        OnboarderPage onboarderPage3 = new OnboarderPage(R.string.country_title, R.string.country_description, R.drawable.country);
        OnboarderPage onboarderPage4 = new OnboarderPage(R.string.map_title, R.string.map_description, R.drawable.map);

        // Background colors
        onboarderPage1.setTitleColor(R.color.colorPrimarySettings);
        onboarderPage1.setDescriptionColor(R.color.colorPrimarySettings);
        onboarderPage1.setBackgroundColor(R.color.white);
        onboarderPage2.setTitleColor(R.color.white);
        onboarderPage2.setDescriptionColor(R.color.white);
        onboarderPage2.setBackgroundColor(R.color.colorPrimary);
        onboarderPage3.setTitleColor(R.color.colorPrimarySettings);
        onboarderPage3.setDescriptionColor(R.color.colorPrimarySettings);
        onboarderPage3.setBackgroundColor(R.color.white);
        onboarderPage4.setTitleColor(R.color.white);
        onboarderPage4.setDescriptionColor(R.color.white);
        onboarderPage4.setBackgroundColor(R.color.colorPrimary);



        // Each page is added to an ArrayList of pages.
        onboarderPages.add(onboarderPage1);
        onboarderPages.add(onboarderPage2);
        onboarderPages.add(onboarderPage3);
        onboarderPages.add(onboarderPage4);


        setOnboardPagesReady(onboarderPages);

    }

    @Override
    public void onSkipButtonPressed() {
        // Optional: by default it skips onboarder to the end
        super.onSkipButtonPressed();
        finish();
    }

    @Override
    public void onFinishButtonPressed() {
        finish();
    }

}