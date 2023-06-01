package com.example.test.register_password;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.helper_classes.Global;
import com.example.test.login.model.LoginResponseModel;
import com.example.test.user.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPasswordViewModel extends ViewModel {

        //Call Same API used for Login in LoginViewModel and same LoginResponseModel that returns UserModel Object

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<LoginResponseModel> mutLoginResponseApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<LoginResponseModel> getMutLoginResponseApi() {
        return mutLoginResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }




    //Login API / Authenticate User
    public void callRegisterApi(String userId, String userPassword) {


        UserModel registerUserRequest = new UserModel(userId , userPassword);
        // LoginModel loginUserRequest = new LoginModel(userId, userPassword);

        subscribtion = (Disposable) Global.apiService().loginUserApi(registerUserRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess , this::onApiError
                );
    }

    private void onHomeApiSuccess(LoginResponseModel result) {
        mutLoginResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }



}
