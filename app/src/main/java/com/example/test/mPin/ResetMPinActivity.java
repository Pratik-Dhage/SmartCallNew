package com.example.test.mPin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityResetMpinBinding;
import com.example.test.fragments_activity.fragments.LoanCollectionFragment;
import com.example.test.fragments_activity.fragments.RenewalFragment;
import com.example.test.mPin.fragments.LoginWithOTPFragment;
import com.example.test.mPin.fragments.LoginWithUserCredentialsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ResetMPinActivity extends AppCompatActivity {

    ActivityResetMpinBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_mpin);

        initializeFields();
        setFragments();
        onClickListener();


    }

    private void initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_reset_mpin);
        view = binding.getRoot();
    }

    private void setFragments() {
        // List of Fragment objects to be displayed in the ViewPager2
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new LoginWithOTPFragment());
        fragmentList.add(new LoginWithUserCredentialsFragment());

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
                        tab.setText("OTP");
                        break;
                    case 1:
                        tab.setText("User Credentials");
                        break;
                    default:
                        break;
                }
            }
        }).attach();


    }



    private void onClickListener() {
    }


}