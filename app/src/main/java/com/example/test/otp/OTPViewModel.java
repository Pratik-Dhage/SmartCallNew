package com.example.test.otp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.helper_classes.Global;
import com.example.test.otp.model.OTPGenerateOTPResponseModel;
import com.example.test.user.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//for Generate OTP
public class OTPViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<OTPGenerateOTPResponseModel> mutGenerateOTP_ResponseApi = new MutableLiveData<>();

    public MutableLiveData<OTPGenerateOTPResponseModel> getMutGenerateOTP_ResponseApi() {
        return mutGenerateOTP_ResponseApi;
    }

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }

    //Coming from LoginActivity->SignUp
    public void callGenerateOTP_Api(String userId){

        UserModel userModel_GenerateOTP = new UserModel(userId);

        subscribtion = (Disposable) Global.apiService().otpGenerateApi(userModel_GenerateOTP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess , this::onApiError
                );
    }

    //Coming from LoginActivity->ForgotPassword
    //This Api call will generate OTP everytime even if the User is Already Registered
    public void callGenerateOTP_ApiEverytime(String userId){

        UserModel userModel_GenerateOTP = new UserModel(userId);

        subscribtion = (Disposable) Global.apiService().otpGenerateApiEverytime(userModel_GenerateOTP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess , this::onApiError
                );
    }

    private void onHomeApiSuccess(OTPGenerateOTPResponseModel result) {
        mutGenerateOTP_ResponseApi.setValue(result);
    }


    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
