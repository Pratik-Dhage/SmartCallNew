package com.example.test.register_password;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.helper_classes.Global;
import com.example.test.login.model.LoginResponseModel;
import com.example.test.user.UserModel;
import com.example.test.user.UserResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPasswordViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final  MutableLiveData<UserResponseModel> mutUserResponseApi = new MutableLiveData<>(); //for Forgot /Reset Password

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();


    public MutableLiveData<UserResponseModel> getMutUserResponseApi() {
        return mutUserResponseApi;
    }

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }




    // Authenticate User // Register Password
    public void callRegisterApi(String userId, String userPassword) {


        UserResponseModel registerPasswordUserRequest = new UserResponseModel(userId , userPassword);
        // LoginModel loginUserRequest = new LoginModel(userId, userPassword);

        subscribtion = (Disposable) Global.apiService().registerPasswordUserApi(registerPasswordUserRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess , this::onApiError
                );
    }


    //coming from Login Activity Forgot Password / Reset Password
    public void callForgotResetPasswordApi(String userId, String userPassword){

        UserResponseModel resetPasswordUserRequest = new UserResponseModel(userId , userPassword);

        subscribtion = (Disposable) Global.apiService().resetPasswordUserApi(resetPasswordUserRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess , this::onApiError
                );
    }


    private void onHomeApiSuccess(UserResponseModel result){
        mutUserResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }



}
