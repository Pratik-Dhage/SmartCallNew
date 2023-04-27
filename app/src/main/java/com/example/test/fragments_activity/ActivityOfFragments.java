package com.example.test.fragments_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;
import com.example.test.databinding.ActivityOfFragmentsBinding;
import com.example.test.fragments_activity.fragments.LoanCollectionFragment;
import com.example.test.fragments_activity.fragments.RenewalFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ActivityOfFragments extends AppCompatActivity {

    ActivityOfFragmentsBinding binding;
    View view;
    // private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_of_fragments);

        initializeFields();
        setFragments();
        onClickListener();
    }

    private void onClickListener() {

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
    }

    private void initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_of_fragments);
        view = binding.getRoot();
        // navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    }



    private void setFragments() {
        // List of Fragment objects to be displayed in the ViewPager2
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new LoanCollectionFragment());  //NPA Fragment
        fragmentList.add(new RenewalFragment());

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
                        tab.setText("NPA");  //Loan Collection
                        break;
                    case 1:
                        tab.setText("Renewal");
                        break;
                    default:
                        break;
                }
            }
        }).attach();


    }
}
