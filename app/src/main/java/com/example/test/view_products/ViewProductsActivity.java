package com.example.test.view_products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityViewProductsBinding;

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
    }
}