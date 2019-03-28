package com.f22labs.food.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.f22labs.food.R;
import com.f22labs.food.databinding.CartBinding;
import com.f22labs.food.viewmodel.CartMainViewModel;

public class CartActivity extends AppCompatActivity {

    CartMainViewModel cartMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        cartMainViewModel = new CartMainViewModel(this);
        binding.setCart(cartMainViewModel);
        RecyclerView itemRecycler = binding.itemRecycler;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        itemRecycler.setLayoutManager(linearLayoutManager);
        CartItemAdapter cartItemAdapter = new CartItemAdapter();
        itemRecycler.setAdapter(cartItemAdapter);
    }

    @Override
    protected void onStart() {
        cartMainViewModel.getFoodDetails();
        super.onStart();
    }


}
