package com.example.armadillianonimi.emergencyphonenumbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class DisclaimerActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        Toolbar disclaimerToolbar = (Toolbar) findViewById(R.id.disclaimer_toolbar);
        disclaimerToolbar.setTitle("FAQ");
        setSupportActionBar(disclaimerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimarySettingsDark));

        disclaimerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
            }
        });

        textView = (TextView)findViewById(R.id.textView);
        textView.setText(R.string.faq);

    }
}
