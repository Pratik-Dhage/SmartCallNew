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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //for password purpose
    // Define the regular expression for the password policy
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,12}$";

    // Compile the regular expression into a pattern
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidPassword(String password) {
        Matcher matcher = PATTERN.matcher(password);
        return !matcher.matches();
    }


/*

    For Password to be 8-12 character long, alphanumeric,with at least 1 special character:

           1) ^ and $ - Anchors that assert the position at the start and end of the string, respectively.
           2) (?=.*[A-Za-z]) - A positive lookahead that asserts that at least one alphabetical character must be present in the password.
           3) (?=.*\d) - A positive lookahead that asserts that at least one numeric digit must be present in the password.
           4) (?=.*[@$!%*#?&]) - A positive lookahead that asserts that at least one special character (@,$,!,%,*,#,?,&) must be present in the password.
          5)  [A-Za-z\d@$!%*#?&]{8,12} - Character set and the quantifier that limit the length to 8-12

*/

}
