package com.example.armadillianonimi.emergencyphonenumbers;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisclaimerActivity extends AppCompatActivity {

    LinearLayout faqContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        Toolbar disclaimerToolbar = (Toolbar) findViewById(R.id.disclaimer_toolbar);
        disclaimerToolbar.setTitle("FAQ");
        setSupportActionBar(disclaimerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimarySettingsDark));
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimarySettingsDark));
        }

        disclaimerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
            }
        });

        // Get container LinearLayout and load titles and contents from resources
        faqContainer = (LinearLayout) findViewById(R.id.faq_scroll);
        String[] titles = getResources().getStringArray(R.array.faq_titles);
        String[] contents = getResources().getStringArray(R.array.faq_contents);

        // Add each title followed by its content to the container
        for (int i = 0; i < titles.length; i++) {
            TextView t = setFaqTitle(titles[i]);
            TextView c = setFaqContent(contents[i]);
            System.out.println("ADDED " + t.getText() + " and its content.");
            faqContainer.addView(t);
            faqContainer.addView(c);
        }
    }

    private TextView setFaqTitle(String title) {
        TextView t = new TextView(this);
        t.setText(title);
        t.setTextSize(getResources().getDimension(R.dimen.faq_part_title));
        t.setTextColor(getResources().getColor(R.color.colorEmergencyCardsText));
        t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    private TextView setFaqContent(String content) {
        TextView c = new TextView(this);
        c.setText(content);
        c.setTextSize(getResources().getDimension(R.dimen.faq_part_content));
        c.setTextColor(getResources().getColor(R.color.colorEmergencyCardsText));
        c.setPadding(0, 0, 0, (int)getResources().getDimension(R.dimen.faq_content_padding_bottom));
        return c;
    }
}
