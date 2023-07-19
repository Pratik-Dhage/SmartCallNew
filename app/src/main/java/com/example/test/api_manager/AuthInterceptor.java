package com.example.test.api_manager;



//import com.squareup.okhttp.Interceptor;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


// TO Authenticate the credentials before calling the API
public class AuthInterceptor implements Interceptor {
    private final String userName;
    private final String password;

    public AuthInterceptor(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", Credentials.basic(userName, password))
                .build();
        return chain.proceed(authenticatedRequest);
    }
}