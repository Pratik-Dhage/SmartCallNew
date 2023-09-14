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
    private int requestCount = 0;
    private static final int MAX_REQUEST_COUNT = 20; //to Set the maximum allowed request count

    public AuthInterceptor(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        requestCount++;
        System.out.println("Api Call Request Count :"+requestCount);
        if (requestCount > MAX_REQUEST_COUNT) {
            // Return an error response if the request count exceeds the limit
            return new Response.Builder()
                    .code(429) // 429 Too Many Requests HTTP status code
                    .message("Please check your Internet connection and try again")
                    .build();
        }

        Request request = chain.request();

        Request authenticatedRequest = request.newBuilder()
                .addHeader("Authorization", Credentials.basic(userName, password))
                .build();

        return chain.proceed(authenticatedRequest);
    }
}