package com.example.test.view_products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityDocumentEligibilityProductInfoBinding;
import com.example.test.main_dashboard.MainActivity3API;
import com.example.test.view_products.fragments.DocumentCheckListFragment;
import com.example.test.view_products.fragments.EligibilityFragment;
import com.example.test.view_products.fragments.ProductInfoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class Document_Eligibility_ProductInfoActivity extends AppCompatActivity {

    ActivityDocumentEligibilityProductInfoBinding binding;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_eligibility_product_info);

    initializeFields();
    setFragments();
    onClickListener();

    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_document_eligibility_product_info);
        view = binding.getRoot();

    }

    private void setFragments(){
        // List of Fragment objects to be displayed in the ViewPager2
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add( new DocumentCheckListFragment());
        fragmentList.add( new EligibilityFragment());
        fragmentList.add( new ProductInfoFragment());


        //  FragmentStateAdapter to handle the fragments in the ViewPager2
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };


        // Set the adapter on the ViewPager2
        binding.viewPager2.setAdapter(fragmentStateAdapter);


        // Connect the TabLayout to the ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Document Checklist");
                        break;
                    case 1:
                        tab.setText("Eligibility");
                        break;
                    case 2:
                        tab.setText("Product Info");

                    default:
                        break;
                }
            }
        }).attach();


    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivHome.setOnClickListener(v->{
            startActivity(new Intent(this, MainActivity3API.class));
        });

        binding.btnOK.setOnClickListener(v->{
           onBackPressed();
        });
    }
}