package com.example.test.api_manager;


//import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;



// TO Authenticate the credentials before calling the API
public class AuthInterceptor implements Interceptor {
    private String authToken;

    public AuthInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer " + authToken)
                .build();
        return chain.proceed(authenticatedRequest);
    }
}
