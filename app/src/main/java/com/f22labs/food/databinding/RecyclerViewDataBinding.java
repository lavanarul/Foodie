package com.f22labs.food.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.f22labs.food.model.Food;
import com.f22labs.food.view.CartItemAdapter;
import com.f22labs.food.view.HomeItemAdapter;

import java.util.List;

public class RecyclerViewDataBinding {

    @BindingAdapter({"app:homeAdapter", "app:dataList"})
    public void bind(RecyclerView recyclerView, HomeItemAdapter adapter, List<Food> data) {
        recyclerView.setAdapter(adapter);
        adapter.updateData(data);
    }

    @BindingAdapter({"app:adapter", "app:data"})
    public void bind(RecyclerView recyclerView, CartItemAdapter adapter, List<Food> data) {
        recyclerView.setAdapter(adapter);
        adapter.updateData(data);
    }
}
