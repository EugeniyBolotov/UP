package com.example.up;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.up.common.AlertDialogs;
import com.example.up.common.LoginData;
import com.example.up.common.NetworkStats;
import com.example.up.common.ReginData;
import com.example.up.common.RequestBodyQuery;
import com.example.up.common.UserToken;
import com.example.up.common.Validation;
import io.paperdb.Paper;

public class SingUpScreen extends AppCompatActivity {

    TextView emailTextView;
    TextView passwordTextView;
    TextView fullnameTextView;
    LoginData loginData = new LoginData();
    ReginData reginData = new ReginData();
    UserToken userToken = new UserToken();
    GsonBuilder builder = new GsonBuilder();
    public Gson gson = builder.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_screen);

        emailTextView = findViewById(R.id.login);
        passwordTextView = findViewById(R.id.password);
        fullnameTextView = findViewById(R.id.fullname);
        Paper.init(this);
    }

    public void RepeatPassword() {
        final EditText input = new EditText (this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Уведомление");
        alert.setMessage("Пожалуйста, повторите пароль");
        alert.setView(input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(String.valueOf(passwordTextView.getText()).equals(String.valueOf(input.getText()))) {
                    SingUp();
                } else
                    AlertDialogs.OpenAlertDialog(SingUpScreen.this, "Пароли не совпадают.");
            }
        });
        alert.show();
    }

    public void OnSingUp(View view) {
        if(emailTextView.getText().length() != 0) {
            if(passwordTextView.getText().length() != 0) {
                if(fullnameTextView.getText().length() != 0) {
                    if(Validation.mailValidation(String.valueOf(emailTextView.getText()))) {
                        if(NetworkStats.isOnline(this)) {
                            RepeatPassword();
                        } else
                            AlertDialogs.OpenAlertDialog(this, "Отсутствует интернет соединение.");
                    } else
                        AlertDialogs.OpenAlertDialog(this, "E-mail не соответствует заданным критериям.");
                } else
                    AlertDialogs.OpenAlertDialog(this, "Пожалуйста, введите имя пользователя");
            } else
                AlertDialogs.OpenAlertDialog(this, "Пожалуйста, введите пароль пользователя");
        } else
            AlertDialogs.OpenAlertDialog(this, "Пожалуйста, введите E-mail пользователя");
    }

    public void SingUp() {
        reginData.email = String.valueOf(emailTextView.getText());
        reginData.password = String.valueOf(passwordTextView.getText());
        reginData.login = String.valueOf(fullnameTextView.getText());

        RequestBodyQuery requestBodyQuery = new RequestBodyQuery("https://food.madskill.ru/auth/register",
                gson.toJson(reginData));
        RequestBodyQuery.PostQueryJsoup postQueryJsoup = new RequestBodyQuery.PostQueryJsoup();
        postQueryJsoup.execute(new RequestBodyQuery.RequestBodyQueryInter() {
            @Override
            public void RequestBodyQueryReturner(String str) {
                if(str.length() != 0) {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    loginData.email = reginData.email;
                    loginData.password = reginData.password;
                    SingIn();
                } else
                    AlertDialogs.OpenAlertDialog(SingUpScreen.this, "Пользователь не найден.");
            }
        });
    }
    public void SingIn() {
        RequestBodyQuery requestBodyQuery = new RequestBodyQuery("https://food.madskill.ru/auth/login",
                gson.toJson(loginData));
        RequestBodyQuery.PostQueryJsoup postQueryJsoup = new RequestBodyQuery.PostQueryJsoup();
        postQueryJsoup.execute(new RequestBodyQuery.RequestBodyQueryInter() {
            @Override
            public void RequestBodyQueryReturner(String str) {
                if (str.length() != 0) {
                    userToken = gson.fromJson(str, UserToken.class);
                    Paper.book().write("token", userToken.token);
                    Intent intent = new Intent(SingUpScreen.this, MainActivity.class);
                    startActivity(intent);
                    SingUpScreen.this.finish();
                } else
                    AlertDialogs.OpenAlertDialog(SingUpScreen.this, "Пользователь не найден.");
            }
        });
    }
    public void OnCancel(View view) {
        Intent intent = new Intent(SingUpScreen.this, SingInScreen.class);
        startActivity(intent);
        this.finish();
    }
}