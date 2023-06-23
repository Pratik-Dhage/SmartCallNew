package com.example.test.api_manager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.api_manager.WebServices;
import com.example.test.helper_classes.Global;
import com.example.test.lead.model.LeadModel;
import com.example.test.main_dashboard.model.DashBoardResponseModel;
import com.example.test.user.UserModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommonViewModel extends ViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();

    public final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();
    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }



    public void callApi(int apiType) {
        Disposable disposable;
        switch (apiType) {
            case 1: // call getLeadList API
                disposable = Global.apiService().getLeadList(WebServices.SmartCall_BaseURL5 + WebServices.find_Leads)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onLeadListApiSuccess, this::onApiError
                        );
                disposables.add(disposable);
                break;
            case 2: // call getDashBoardData API

                String userId = "admin"; //send User Id and password as Request Body in RestClient
                String password = "123456";

                //for making @POST request
                UserModel userModel = new UserModel(userId,password);

                disposable = Global.apiService().getDashBoardData(userModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                this::onDashBoardApiSuccess, this::onApiError
                        );
                disposables.add(disposable);
                break;
            default:
                break;
        }
    }

    public void onLeadListApiSuccess(List<LeadModel> leadList) {
        // handle lead list API success
        TestViewModel.getMutLeadListResponseApi().setValue(leadList);
    }

    public void onDashBoardApiSuccess(List<DashBoardResponseModel> dashboardData) {
        // handle dashboard data API success
        TestViewModel.getMutDashBoardResponseApi().setValue(dashboardData);
    }

    public void onApiError(Throwable error) {
        // handle API error
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
