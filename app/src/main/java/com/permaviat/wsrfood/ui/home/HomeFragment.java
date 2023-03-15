package com.permaviat.wsrfood.ui.home;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.up.MainActivity;
import com.example.up.OneItemScreen;
import com.example.up.R;
import com.example.up.common.OrdersDish;
import com.example.up.common.onClickInterface;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.example.up.common.DishsAdapter;
import com.example.up.common.AlertDialogs;
import com.example.up.common.DataBase;
import com.example.up.common.Dishs;
import com.example.up.databinding.FragmentHomeBinding;
import com.permaviat.wsrfood.MainNavigation;

import java.util.ArrayList;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    LinearLayout toolbar, searchbar;
    TabLayout tab_layout;
    RecyclerView recyclerView;
    EditText delivery_tv, text_search;
    TextView result;
    AdView mAdView;
    LinearLayout item;
    ImageView back, logo;
    TextView more, item_name, item_price, item_count;
    LinearLayout minus, plus, add;
    public static LinearLayout order, basket;
    Button _continue, cart;

    DataBase.DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;
    ArrayList<String> category = new ArrayList<>();
    View root;
    float deltaY = 0;
    Boolean scroll = false;
    private onClickInterface onclickInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        tab_layout = root.findViewById(R.id.tab_layout);
        result = root.findViewById(R.id.result);
        ImageView search = root.findViewById(R.id.search_toolbar);
        ImageView close_search = root.findViewById(R.id.close_search);
        ImageView delivery = root.findViewById(R.id.delivery);
        delivery_tv = root.findViewById(R.id.delivery_tv);
        text_search = root.findViewById(R.id.text_search);
        toolbar = root.findViewById(R.id.toolbar);
        searchbar = root.findViewById(R.id.searchbar);
        recyclerView = root.findViewById(R.id.listDishs);
        item = root.findViewById(R.id.item);
        back = root.findViewById(R.id.back);
        more = root.findViewById(R.id.more);
        logo = root.findViewById(R.id.logo);
        item_name = root.findViewById(R.id.item_name);
        item_price = root.findViewById(R.id.item_price);
        item_count = root.findViewById(R.id.item_count);
        minus = root.findViewById(R.id.minus);
        plus = root.findViewById(R.id.plus);
        add = root.findViewById(R.id.add);
        order = root.findViewById(R.id.order);
        basket = root.findViewById(R.id.basket);
        _continue = root.findViewById(R.id._continue);
        cart = root.findViewById(R.id.cart);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                item.setVisibility(View.INVISIBLE);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dishs addOrder = null;
                for(int i = 0; i < MainNavigation.dishes.size(); i ++) {
                    if(MainNavigation.dishes.get(i).nameDish == item_name.getText()) {
                        addOrder = MainNavigation.dishes.get(i);
                        break;
                    }
                }
                Intent intent = new Intent(root.getContext(), OneItemScreen.class);
                intent.putExtra("name", addOrder.nameDish);
                intent.putExtra("price", addOrder.price);
                intent.putExtra("logo", addOrder.icon);
                startActivity(intent);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer count = Integer.parseInt(String.valueOf(item_count.getText()));
                if(count > 1)
                    count --;
                item_count.setText(String.valueOf(count));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer count = Integer.parseInt(String.valueOf(item_count.getText()));
                count ++;
                item_count.setText(String.valueOf(count));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dishs addOrder = null;
                for(int i = 0; i < MainNavigation.dishes.size(); i ++) {
                    if(MainNavigation.dishes.get(i).nameDish == item_name.getText()) {
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
                    newOrderDish.count = String.valueOf(item_count.getText());

                    MainNavigation.dishes_order.add(newOrderDish);

                    order.setVisibility(View.GONE);
                    basket.setVisibility(View.VISIBLE);
                }
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        _continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                item.setVisibility(View.INVISIBLE);
            }
        });
        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(View view) {
                recyclerView.setVisibility(View.INVISIBLE);
                item.setVisibility(View.VISIBLE);

                ConstraintLayout item_select = (ConstraintLayout) view;
                CardView item_card = (CardView) item_select.getChildAt(1);
                ImageView item_image = (ImageView) item_card.getChildAt(0);
                BitmapDrawable item_image_drawable = (BitmapDrawable) item_image.getDrawable();
                LinearLayout item_layout = (LinearLayout) item_select.getChildAt(0);
                TextView name = (TextView) item_layout.getChildAt(0);
                TextView price = (TextView) item_layout.getChildAt(1);
                logo.setImageBitmap(item_image_drawable.getBitmap());
                item_name.setText(name.getText());
                item_price.setText(price.getText());
                item_count.setText("1");
                order.setVisibility(View.VISIBLE);
                basket.setVisibility(View.GONE);
            }
        };


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSearchEnable();
            }
        });
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSearchEnable();
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDelivery();
            }
        });
        text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(String.valueOf(text_search.getText()).length() != 0) {
                    tab_layout.setVisibility(View.INVISIBLE);
                    result.setVisibility(View.VISIBLE);
                    LoadDishsSearch(String.valueOf(text_search.getText()));
                } else {
                    tab_layout.setVisibility(View.VISIBLE);
                    result.setVisibility(View.INVISIBLE);
                    tab_layout.getTabAt(0).select();
                    LoadDishsAdapter(0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });

        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        dataBaseHelper = new DataBase.DataBaseHelper(root.getContext());

        database = dataBaseHelper.getWritableDatabase();
        if(tab_layout != null) {
            GetDishs();
            GetCategory();

            tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    LoadDishsAdapter(position);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });

            for(Integer i = 0; i < category.size(); i ++) {
                tab_layout.addTab(tab_layout.newTab().setText(category.get(i)));
            }
        }

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float scale = getContext().getResources().getDisplayMetrics().density;
                ConstraintLayout.LayoutParams tab_param = (ConstraintLayout.LayoutParams) tab_layout.getLayoutParams();
                ConstraintLayout.LayoutParams result_param = (ConstraintLayout.LayoutParams) result.getLayoutParams();

                if(scroll == false) {
                    deltaY = event.getY();
                    scroll = true;
                }
                if(scroll == true) {
                    if(event.getAction() == MotionEvent.ACTION_MOVE) {
                        if(event.getY() < deltaY) {
                            if(recyclerView.computeVerticalScrollOffset() > 0) {
                                mAdView.setVisibility(View.INVISIBLE);
                                tab_param.topMargin = (int) (96*scale);
                                result_param.topMargin = (int) (96*scale);
                                tab_layout.setLayoutParams(tab_param);
                                result.setLayoutParams(result_param);
                                scroll = false;
                            }
                        } else {
                            if(recyclerView.computeVerticalScrollOffset() == 0) {
                                mAdView.setVisibility(View.VISIBLE);
                                tab_param.topMargin = (int) (305*scale);
                                result_param.topMargin = (int) (305*scale);
                                tab_layout.setLayoutParams(tab_param);
                                result.setLayoutParams(result_param);
                                scroll = false;
                            }
                        }
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    scroll = false;
                }
                return false;
            }
        });


        return root;
    }

    public void GetDelivery() {
        String[] address = String.valueOf(delivery_tv.getText()).split(", ");
        if(address.length != 3)
            AlertDialogs.OpenAlertDialog(root.getContext(), "Укажите адрес в следующем порядке: Город, Улица, Дом");
    }

    public void LoadDishsSearch(String name) {
        ArrayList<Dishs> selectDishs = new ArrayList<>();
        for(int i = 0; i < MainNavigation.dishes.size(); i++) {
            if(MainNavigation.dishes.get(i).nameDish.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                selectDishs.add(MainNavigation.dishes.get(i));
            }
        }
        DishsAdapter adapter = new DishsAdapter(root.getContext(), selectDishs, onclickInterface);
        recyclerView.setAdapter(adapter);
    }
    public void LoadDishsAdapter(int id) {
        ArrayList<Dishs> selectDishs = new ArrayList<>();
        for(int i = 0; i < MainNavigation.dishes.size(); i++) {
            if(MainNavigation.dishes.get(i).category.equals(category.get(id))) {
                selectDishs.add(MainNavigation.dishes.get(i));
            }
        }
        DishsAdapter adapter = new DishsAdapter(root.getContext(), selectDishs, onclickInterface);
        recyclerView.setAdapter(adapter);
    }

    public void GetDishs() {
        Cursor cursorDataBase = database.query("db_dish",
                null,
                null,
                null,
                null,
                null, null);
        if(cursorDataBase.moveToFirst()) {
            do {
                Dishs newDish = new Dishs();
                Integer id = cursorDataBase.getInt(0);
                newDish.dishId = cursorDataBase.getString(1);
                newDish.category = cursorDataBase.getString(2);
                newDish.nameDish = cursorDataBase.getString(3);
                newDish.price = cursorDataBase.getString(4);
                newDish.icon = cursorDataBase.getString(5);
                newDish.version = cursorDataBase.getString(6);
                MainNavigation.dishes.add(newDish);
            } while (cursorDataBase.moveToNext());
        }
    }
    public void GetCategory() {
        for(Integer i = 0; i < MainNavigation.dishes.size(); i ++) {
            boolean add = true;
            for(Integer j = 0; j < category.size(); j++) {
                if(MainNavigation.dishes.get(i).category.equals(category.get(j))) {
                    add = false;
                    break;
                }
            }
            if(add) {
                category.add(MainNavigation.dishes.get(i).category);
            }
        }
    }

    public void OnSearchEnable() {
        if(toolbar.getVisibility() == View.INVISIBLE) {
            toolbar.setVisibility(View.VISIBLE);
            searchbar.setVisibility(View.INVISIBLE);

            tab_layout.setVisibility(View.VISIBLE);
            result.setVisibility(View.INVISIBLE);
            tab_layout.getTabAt(0).select();
            LoadDishsAdapter(0);
        } else {
            toolbar.setVisibility(View.INVISIBLE);
            searchbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}