package com.example.login;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthorizationIncterceptor implements Interceptor {
    private UserService apiService;
    private Session session;

    public AuthorizationIncterceptor(UserService apiService, Session session) {
        this.apiService = apiService;
        this.session = session;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response mainResponse = chain.proceed(chain.request());
        if (mainResponse.code() == 401 || mainResponse.code() == 403) {
            session.invalidate();
        }
        return mainResponse;
    }
}
