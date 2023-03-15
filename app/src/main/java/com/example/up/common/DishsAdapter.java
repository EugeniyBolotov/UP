package com.example.up.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.up.R;
//import com.permaviat.wsrfood.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class DishsAdapter extends RecyclerView.Adapter<DishsAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Dishs> dishs;
    private final Context context;
    private onClickInterface onClickInterface;

    public DishsAdapter(Context context, ArrayList<Dishs> states, onClickInterface onClickInterface) {
        this.context = context;
        this.dishs = states;
        this.inflater = LayoutInflater.from(context);
        this.onClickInterface = onClickInterface;
    }

    @Override
    public DishsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dish_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishsAdapter.ViewHolder holder, int position) {
        Dishs dish = dishs.get(position);
        holder.name.setText(dish.nameDish);
        holder.price.setText("N" + dish.price);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.setClick(holder.item);
            }
        });
        Picasso.with(context)
                .load("https://food.madskill.ru/up/images/" + dish.icon)
                .into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return dishs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, price;
        final ImageView logo;
        final ConstraintLayout item;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            logo = (ImageView) view.findViewById(R.id.logo);
            item = view.findViewById(R.id.item);
        }
    }
}
