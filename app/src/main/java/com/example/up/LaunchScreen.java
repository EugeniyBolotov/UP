package com.example.up;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import com.example.up.common.NetworkStats;
import com.example.up.common.AlertDialogs;
import com.example.up.common.DishesVersion;
import com.example.up.common.Dishs;
import com.example.up.common.GetQuery;
import com.example.up.common.DataBase;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.ImageView;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LaunchScreen extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView faceProgressBar;
    Timer timer = new Timer();
    MyTimerTask myTimerTask = new MyTimerTask();
    GetQuery getQuery = new GetQuery("https://food.madskill.ru/dishes/version");
    DishesVersion dishesVersion = new DishesVersion();
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    DataBase.DataBaseHelper dataBaseHelper  = new DataBase.DataBaseHelper(this);
    SQLiteDatabase database;
    Integer countVersionDownload = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        progressBar = findViewById(R.id.progressBar);
        faceProgressBar = findViewById(R.id.imageView1);

        database = dataBaseHelper.getWritableDatabase();

        if ( !NetworkStats.isOnline(this) ) {
            progressBar.setVisibility(View.GONE);
            faceProgressBar.setVisibility(View.VISIBLE);
            AlertDialogs.OpenAlertDialog(this, "Отсутствует интернет!");
            timer.schedule(myTimerTask, 3000);
        } else {
            GetQuery.GetQueryJsoup getDishesVersion = new GetQuery.GetQueryJsoup();
            getDishesVersion.execute(new GetQuery.Inter() {
                @Override
                public void returner(String result) {
                    dishesVersion = gson.fromJson(result, DishesVersion.class);
                    CheckDishVersion();
                }
            });
        }
    }

    public void CheckDishVersion() {
        Cursor cursorDataBase = database.query("db_dish_version",
                null,
                null,
                null,
                null,
                null, null);
        if(cursorDataBase.moveToFirst()) {
            do {
                String version = cursorDataBase.getString(1);
                if(dishesVersion.version.contains(version)) {
                    dishesVersion.version.remove(version);
                }
            } while (cursorDataBase.moveToNext());
        }

        if(dishesVersion.version.size() > 0) {
            database.execSQL("DELETE FROM db_dish_version");

            for(int i = 0; i < dishesVersion.version.size(); i ++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("version", dishesVersion.version.get(i));
                database.insert("db_dish_version", null, contentValues);
            }

            CheckDish();
        } else {
            Intent intent = new Intent(LaunchScreen.this, OnBoardingScreen.class);
            startActivity(intent);
            closeActivity();
        }
    }

    public void CheckDish() {
        database.execSQL("DELETE FROM db_dish");
        countVersionDownload = dishesVersion.version.size();
        for(int i = 0; i < dishesVersion.version.size(); i ++) {
            getQuery = new GetQuery("https://food.madskill.ru/dishes?version="+dishesVersion.version.get(i));
            GetQuery.GetQueryJsoup getDishesVersion = new GetQuery.GetQueryJsoup();
            getDishesVersion.execute(new GetQuery.Inter() {
                @Override
                public void returner(String result) {
                    List<Dishs> newDish = Arrays.asList(gson.fromJson(result, Dishs[].class));

                    for(int i = 0; i < newDish.size(); i ++) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("dishId", newDish.get(i).dishId);
                        contentValues.put("category", newDish.get(i).category);
                        contentValues.put("nameDish", newDish.get(i).nameDish);
                        contentValues.put("price", newDish.get(i).price);
                        contentValues.put("icon", newDish.get(i).icon);
                        contentValues.put("version", newDish.get(i).version);
                        database.insert("db_dish", null, contentValues);
                    }
                    countVersionDownload--;
                    if(countVersionDownload == 0) {
                        Intent intent = new Intent(LaunchScreen.this, OnBoardingScreen.class);
                        startActivity(intent);
                        closeActivity();
                    }
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LaunchScreen.this, OnBoardingScreen.class);
                    startActivity(intent);
                    closeActivity();
                }
            });
        }
    }
    private void closeActivity() {
        this.finish();
    }
}