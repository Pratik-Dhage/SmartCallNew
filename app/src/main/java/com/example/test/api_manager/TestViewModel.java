package com.example.test.api_manager;

import androidx.lifecycle.MutableLiveData;

import com.example.test.lead.model.LeadModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;

import java.lang.reflect.Method;
import java.util.List;

//Test for LeadViewModel
public class TestViewModel extends CommonViewModel{


    //for Lead Response
    public static final MutableLiveData<List<LeadModel>> mutLeadListResponseApi = new MutableLiveData<>();

    public static MutableLiveData<List<LeadModel>> getMutLeadListResponseApi() {
        return mutLeadListResponseApi;
    }

      // for DashBoard Response
    public static final MutableLiveData<List<DashBoardResponseModel>> mutDashBoardResponseApi = new MutableLiveData<>();

    public static MutableLiveData<List<DashBoardResponseModel>> getMutDashBoardResponseApi() {
        return mutDashBoardResponseApi;
    }


    }

