package com.example.test.npa_flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.test.R;
import com.example.test.databinding.ActivityWebViewBinding;
import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;

public class WebViewActivity extends AppCompatActivity {

    ActivityWebViewBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_web_view);

        initializeFields();
        if(NetworkUtilities.getConnectivityStatus(this)){
            setUpGoogleMaps();
        }
        else{
            Global.showToast(this,getResources().getString(R.string.check_internet_connection));
        }

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web_view);
        view = binding.getRoot();
    }


   private void setUpGoogleMaps(){

       WebView webView = binding.webView;
       webView.getSettings().setJavaScriptEnabled(true); // enable JavaScript
       webView.setWebViewClient(new WebViewClient()); //  WebViewClient to handle page loading
       String location = "Navi Mumbai";
       String url = "https://www.google.com/maps?q=" + location;
       webView.loadUrl(url); // load the Google Maps URL with the location


   }
}