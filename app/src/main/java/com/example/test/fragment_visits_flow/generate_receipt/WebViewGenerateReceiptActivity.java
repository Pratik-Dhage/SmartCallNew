package com.example.test.fragment_visits_flow.generate_receipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.test.R;
import com.example.test.databinding.ActivityWebViewGenerateReceiptBinding;
import com.example.test.helper_classes.Global;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;
import com.github.barteksc.pdfviewer.PDFView;


import org.w3c.dom.DocumentType;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
//import com.github.barteksc.pdfviewer.PDFView;

public class WebViewGenerateReceiptActivity extends AppCompatActivity {

    ActivityWebViewGenerateReceiptBinding binding;
    View view;

    /*
    Below is the url for the receipt in PDF form.
http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=238624&amtPaid=1645&userId=CA_01_001&userName=CallingAgent1

kindly note that you have to pass the following parameters

output -> default value is PDF

dataSetId -> is the dataSetId of the record

amtPaid -> is the value from the UI

userId -> as of now it should be CA_01_001

username -> as of now it should be CallingAgent1

as for userId and username we will get this from the ui once we implement login in the app
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_generate_receipt);

        initializeFields();
        onClickListener();
       // setWebViewForGeneratingReceipt();

        String dataSetId = getIntent().getStringExtra("dataSetId");
        String userId = "CA_01_001";
        String username = "CallingAgent1";
        String amount_paid = Global.getStringFromSharedPref(this,"Amount_Paid");

        //original URL
        String generateReceiptUrl = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=238624&amtPaid=1645&userId=CA_01_001&userName=CallingAgent1";

        String generateReceiptUrl2 = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=" + dataSetId + "&amtPaid=" + amount_paid + "&userId=" + userId + "&userName=" + username;

        new RetrievePDFfromUrl().execute(generateReceiptUrl2);
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_web_view_generate_receipt);
        view = binding.getRoot();

    }

    private void onClickListener(){
        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });
    }

    private void setWebViewForGeneratingReceipt() {

        String dataSetId = getIntent().getStringExtra("dataSetId");
        String userId = "CA_01_001";
        String username = "CallingAgent1";
        String amount_paid = Global.getStringFromSharedPref(this,"Amount_Paid");

        //original URL
        String generateReceiptUrl = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=238624&amtPaid=1645&userId=CA_01_001&userName=CallingAgent1";

        String generateReceiptUrl2 = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=" + dataSetId + "&amtPaid=" + amount_paid + "&userId=" + userId + "&userName=" + username;

        //Using GoogleDocsViewerUrl
        String pdfUrl = "http://43.239.52.151:8081/report/Receipt?output=PDF&dataSetId=238624&amtPaid=1645&userId=CA_01_001&userName=CallingAgent1";
        String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + pdfUrl;

      /*  WebView webView = findViewById(R.id.webViewGeneratereceipt);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(googleDocsUrl);
*/

        // for Navigating to PDF viewer app installed in Device
       /* Uri uri = Uri.parse(generateReceiptUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri,"application/pdf");
        startActivity(intent);
*/

        PDFView pdfView = binding.pdfView;
        pdfView.fromUri(Uri.parse(generateReceiptUrl))
                .load();



    }


    // create an async task class for loading pdf file from URL.
    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            binding.pdfView.fromStream(inputStream)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .pageSnap(true)
                    .autoSpacing(false)
                    .pageFling(false)
                    .nightMode(false)
                    .load();


        }
    }


}

