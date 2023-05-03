package com.example.test.view_products.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;
import com.example.test.databinding.FragmentDocumentCheckListBinding;
import com.example.test.view_products.ProductInterestStatusActivity;
import com.example.test.view_products.ViewProductsActivity;


public class DocumentCheckListFragment extends Fragment {

    FragmentDocumentCheckListBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_document_check_list, container, false);
         onClickListener();

        return binding.getRoot();

    }

    private void onClickListener(){


    }
}