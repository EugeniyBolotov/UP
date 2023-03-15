package com.example.up;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.up.common.AlertDialogs;
import com.example.up.common.LoginData;
import com.example.up.common.NetworkStats;
import com.example.up.common.RequestBodyQuery;
import com.example.up.common.Validation;
import com.example.up.common.UserToken;
import io.paperdb.Paper;

public class SingInScreen extends AppCompatActivity {

    TextView loginTextView;
    TextView passwordTextView;

    GsonBuilder builder = new GsonBuilder();
    public Gson gson = builder.create();
    LoginData loginData = new LoginData();
    UserToken userToken = new UserToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_screen);
        loginTextView = findViewById(R.id.login);
        passwordTextView = findViewById(R.id.password);
        Paper.init(this);
    }
    public void OnSingIn(View view) {
        if(loginTextView.getText().length() != 0) {
            if(passwordTextView.getText().length() != 0) {
                if(Validation.mailValidation(String.valueOf(loginTextView.getText()))) {
                    if(NetworkStats.isOnline(this)) {
                        loginData.email = String.valueOf(loginTextView.getText());
                        loginData.password = String.valueOf(passwordTextView.getText());

                        RequestBodyQuery requestBodyQuery = new RequestBodyQuery("https://food.madskill.ru/auth/login",
                                gson.toJson(loginData));

                        RequestBodyQuery.PostQueryJsoup postQueryJsoup = new RequestBodyQuery.PostQueryJsoup();
                        postQueryJsoup.execute(new RequestBodyQuery.RequestBodyQueryInter() {
                            @Override
                            public void RequestBodyQueryReturner(String str) {
                                if(str.length() != 0) {
                                    userToken = gson.fromJson(str, UserToken.class);
                                    Paper.book().write("token", userToken.token);
                                    Intent intent = new Intent(SingInScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    SingInScreen.this.finish();
                                } else
                                    AlertDialogs.OpenAlertDialog(SingInScreen.this, "Пользователь не найден.");
                            }
                        });
                    } else
                        AlertDialogs.OpenAlertDialog(this, "Отсутствует интернет соединение.");
                } else
                    AlertDialogs.OpenAlertDialog(this, "E-mail не соответствует заданным критериям.");
            } else
                AlertDialogs.OpenAlertDialog(this, "Пожалуйста, введите пароль пользователя.");
        } else
            AlertDialogs.OpenAlertDialog(this, "Пожалуйста, введите E-mail пользователя.");
    }
}