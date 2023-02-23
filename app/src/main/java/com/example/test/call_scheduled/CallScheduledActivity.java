package com.example.test.call_scheduled;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.example.test.R;

public class CallScheduledActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_scheduled);


        //initializeFields();
    }

    private void initializeFields() {

        // Get the window of the activity
        Window window = getWindow();

// for testing to Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_grey));
        }

    }
}