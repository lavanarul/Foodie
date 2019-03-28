package com.f22labs.food.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.f22labs.food.R;
import com.f22labs.food.databinding.CartItemBinding;
import com.f22labs.food.model.Food;
import com.f22labs.food.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;


public class CartItemAdapter extends Adapter<CartItemAdapter.ViewHolder> {
    private List<Food> foodItemList;
    private LayoutInflater layoutInflater;

    public CartItemAdapter() {
        this.foodItemList = new ArrayList<>();
    }

    public void updateData(@Nullable List<Food> data) {
        if (data == null || data.isEmpty()) {
            this.foodItemList.clear();
        } else {
            this.foodItemList.addAll(data);
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        CartItemBinding binding;

        ViewHolder(CartItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }

        void bindFood(Food food) {
            if (binding.getCart() == null) {
                binding.setCart(
                        new CartViewModel(itemView.getContext(), food));
            } else {
                binding.getCart().setFood(food);
            }
        }
    }


    CartItemAdapter(List<Food> categoriesModels) {
        this.foodItemList = categoriesModels;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CartItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.adapter_cart_item, parent, false
        );
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bindFood(foodItemList.get(position));

    }


    public int getItemCount() {
        return foodItemList.size();
    }


}
