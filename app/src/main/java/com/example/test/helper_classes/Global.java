package com.example.test.helper_classes;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.example.test.api_manager.RestClient;
import com.example.test.api_manager.WebServices;
import com.example.test.api_manager.RestClient;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.disposables.Disposable;

public class Global {

    public static RestClient apiService(){
       return WebServices.create();
        //RestClient.create()
    }

    public static void showToast(Context context, String str){
        Toast.makeText(context,str, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidCellPhone(String number) {
      return Patterns.PHONE.matcher(number).matches();
    }

    public static void showSnackBar(View view, String str){
        Snackbar snackBar = Snackbar.make(view,str,Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus().getRootView();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveStringInSharedPref( Context context, String key, String value) {
        SharedPreferenceHelper.writeString(context, key, value);
    }

    public static String getStringFromSharedPref( Context context,String key) {
        return SharedPreferenceHelper.getString(context, key, "");
    }

    public static void removeStringInSharedPref( Context context, String  key) {
        SharedPreferenceHelper.writeString(context, key, "");
    }


}
