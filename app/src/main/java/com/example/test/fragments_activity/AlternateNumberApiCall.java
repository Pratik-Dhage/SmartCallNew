package com.example.test.fragments_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.test.helper_classes.Global;
import com.example.test.helper_classes.NetworkUtilities;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerViewModel;
import com.example.test.npa_flow.details_of_customer.adapter.DetailsOfCustomerAdapter;

public class AlternateNumberApiCall {

    public static void saveAlternateNumber(Activity activity, String alternateNumber,String dataSetId){
        if(!Global.isValidMobileNumber(activity,alternateNumber)){

        }

        //Global.isValidMobileNumber(activity,alternateNumber)
        //to save Alternate Number even if it is empty
        else if (true){

            if(null!= DetailsOfCustomerAdapter.dataSetId ){
                System.out.println("Here Alternate Number dataSetId:"+DetailsOfCustomerAdapter.dataSetId+" AlternateNumber:"+alternateNumber);

                if(NetworkUtilities.getConnectivityStatus(activity)){
                    DetailsOfCustomerViewModel detailsOfCustomerViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(DetailsOfCustomerViewModel.class);

                    // isSavingAlternateNumber will be true if user clicks on Yes in dialog
                       if(DetailsOfCustomerAdapter.isSavingAlternateNumber){
                           System.out.println("DetailsOfCustomerAdapter.isSavingAlternateNumber"+DetailsOfCustomerAdapter.isSavingAlternateNumber);
                           detailsOfCustomerViewModel.saveAlternateNumber_Data(dataSetId,alternateNumber);
                           initObserverSaveAlternateNumber(activity,detailsOfCustomerViewModel,dataSetId);

                           //After calling Save Alternate No. Api make isSavingAlternateNumber to false to prevent unnecessary Api calls
                           DetailsOfCustomerAdapter.isSavingAlternateNumber = false;



                       }

                }

            }

        }
    }

    private static void initObserverSaveAlternateNumber(Context context, DetailsOfCustomerViewModel detailsOfCustomerViewModel,String dataSetId){

        detailsOfCustomerViewModel.getMutSaveAlternateNumber_ResponseApi().observe((LifecycleOwner)context, result->{

            if(result!=null){
              //  Global.showToast(context,"Alternate No. Saved:"+result);
                System.out.println("Alternate No. Saved"+result);
                Global.hideKeyboard((Activity)context);

            }
            else {
                System.out.println("Result is null");
            }
        });

//        handle error response
        detailsOfCustomerViewModel .getMutErrorResponse().observe((LifecycleOwner)context, error -> {

            if (error != null && !error.isEmpty()) {
                // Global.showToast(context, error);
                System.out.println("Here Alternate no. Exception: " + error);
            } else {
                Global.showToast(context,"Check internet connection");
            }
        });


    }


}
