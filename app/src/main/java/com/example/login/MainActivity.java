package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Session session;
    private UserService apiService;
    private AuthenticationListener  authenticationListener;

    EditText username,password;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        username=(EditText)findViewById(R.id.editUId);
        password=(EditText)findViewById(R.id.editPass);
        btnlogin=(Button)findViewById(R.id.Btn);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Username / Password Required", Toast.LENGTH_LONG).show();
                } else {
                    //proceed to login
                    login();
                }
            }
        });

    }

    private void login() {
        LoginRequest loginRequest= new LoginRequest();
        loginRequest.setUid(username.getText().toString());
        loginRequest.setPassword(password.getText().toString());
        loginRequest.setRole(0);

        Call<LoginResponse> loginResponseCall=ApiClient.getUserService().userLogin(loginRequest);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    Log.d("Data succes",response.toString());
                }
                else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                    Log.d("Data fail",response.toString());

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Throwable fail"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("Data fail",t.getLocalizedMessage());

            }
        });


    }


    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    public Session getSession() {
        if (session == null) {
            session = new Session() {
                @Override
                public boolean isLoggedIn() {
                    // check if token exist or not
                    // return true if exist otherwise false
                    // assuming that token exists
                    return true;
                }

                @Override
                public void saveToken(String token) {

                }

                @Override
                public String getToken() {
                    return null;
                }

                @Override
                public void saveEmail(String email) {

                }

                @Override
                public String getEmail() {
                    return null;
                }

                @Override
                public void savePassword(String password) {

                }

                @Override
                public String getPassword() {
                    return null;
                }


                @Override
                public void invalidate() {
                    // get called when user become logged out
                    // delete token and other user info
                    // (i.e: email, password)
                    // from the storage

                    // sending logged out event to it's listener
                    // i.e: Activity, Fragment, Service
                    if (authenticationListener != null) {
                        authenticationListener.onUserLoggedOut();
                    }
                }
            };
        }

        return session;
    }

    public interface AuthenticationListener {
        void onUserLoggedOut();
    }

    public void setAuthenticationListener(AuthenticationListener listener) {
        this.authenticationListener = listener;
    }

    public UserService getApiService() {
        if (apiService == null) {
            apiService = provideRetrofit(UserService.URL).create(UserService.class);
        }
        return apiService;
    }

    private Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        return okhttpClientBuilder.build();
    }

}