package com.example.test.view_products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.call_status.CallStatusWithLeadInteractionActivity;
import com.example.test.call_status.CallStatusWithProductsActivity;
import com.example.test.databinding.ActivityViewProductsBinding;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.view_products.fragments.DocumentCheckListFragment;

public class ViewProductsActivity extends AppCompatActivity {

    ActivityViewProductsBinding binding;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_view_products);

        initializeFields();
        onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_products);
        view = binding.getRoot();
    }

    private void onClickListener() {
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnSkipAndProceed.setOnClickListener(v->{

            String firstName = getIntent().getStringExtra("firstName");
            String phoneNumber = getIntent().getStringExtra("phoneNumber");


            Intent i = new Intent(ViewProductsActivity.this, Document_Eligibility_ProductInfoActivity.class) ;
            i.putExtra("firstName",firstName);
            i.putExtra("phoneNumber",phoneNumber);
            startActivity(i);
        });
    }
}