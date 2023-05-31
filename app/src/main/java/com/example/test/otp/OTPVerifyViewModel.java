package com.example.test.otp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.helper_classes.Global;
import com.example.test.otp.model.OTPGenerateOTPResponseModel;
import com.example.test.otp.model.OTPValidateOTPResponseModel;
import com.example.test.user.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//for Validate OTP
public class OTPVerifyViewModel extends ViewModel {

    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.

    private final MutableLiveData<OTPValidateOTPResponseModel> mutValidateOTP_ResponseApi = new MutableLiveData<>();

    public MutableLiveData<OTPValidateOTPResponseModel> getMutValidateOTP_ResponseApi() {
        return mutValidateOTP_ResponseApi;
    }

    private final MutableLiveData<String> mutErrorResponse = new MutableLiveData<>();

    public MutableLiveData<String> getMutErrorResponse() {
        return mutErrorResponse;
    }


    public void callValidateOTP_Api(String userId,int otpCode){

        UserModel userModel_ValidateOTP = new UserModel(userId,otpCode);

        subscribtion = (Disposable) Global.apiService().otpValidateApi(userModel_ValidateOTP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        this::onHomeApiSuccess , this::onApiError
                );
    }

    private void onHomeApiSuccess(OTPValidateOTPResponseModel result) {
        mutValidateOTP_ResponseApi.setValue(result);
    }


    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }

}
