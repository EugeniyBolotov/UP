package com.example.up;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.up.common.Dishs;
import com.example.up.common.OrdersDish;
import com.permaviat.wsrfood.MainNavigation;
import com.permaviat.wsrfood.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

public class OneItemScreen extends AppCompatActivity {

    TextView name, price;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_item_screen);
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        Bundle arguments = getIntent().getExtras();
        name.setText(String.valueOf(arguments.get("name")));
        price.setText("N" + String.valueOf(arguments.get("price")));
        Picasso.with(this)
                .load("https://food.madskill.ru/up/images/" + arguments.get("logo"))
                .into(logo);
    }
    public void onClose(View view) {
        this.finish();
    }
    public void AddDish(View view) {
        Dishs addOrder = null;
        for(int i = 0; i < MainNavigation.dishes.size(); i ++) {
            if(MainNavigation.dishes.get(i).nameDish.equals(name.getText())) {
                addOrder = MainNavigation.dishes.get(i);
                break;
            }
        }
        if(addOrder != null) {
            OrdersDish newOrderDish = new OrdersDish();
            newOrderDish.dishId = addOrder.dishId;
            newOrderDish.category = addOrder.category;
            newOrderDish.nameDish = addOrder.nameDish;
            newOrderDish.price = addOrder.price;
            newOrderDish.icon = addOrder.icon;
            newOrderDish.version = addOrder.version;
            newOrderDish.count = "1";
            MainNavigation.dishes_order.add(newOrderDish);
            HomeFragment.order.setVisibility(View.GONE);
            HomeFragment.basket.setVisibility(View.VISIBLE);
            this.finish();
        }
    }
}