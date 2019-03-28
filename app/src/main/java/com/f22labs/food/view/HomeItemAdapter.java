package com.f22labs.food.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.f22labs.food.R;
import com.f22labs.food.databinding.ItemBinding;
import com.f22labs.food.model.Food;
import com.f22labs.food.viewmodel.FoodViewModel;
import com.f22labs.food.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeItemAdapter extends Adapter<HomeItemAdapter.ViewHolder> {

    private List<Food> foodItemList;
    private LayoutInflater layoutInflater;
    MainViewModel mainViewModel;

    public HomeItemAdapter(MainViewModel mainViewModel) {
        this.mainViewModel=mainViewModel;
        this.foodItemList = new ArrayList<>();
    }


    public void updateData(@Nullable List<Food> data) {
        this.foodItemList.clear();
        this.foodItemList.addAll(data);

        notifyDataSetChanged();
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        ItemBinding binding;

        ViewHolder(ItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }

        void bindFood(Food food) {
            if (binding.getFoodBinding() == null) {
                binding.setFoodBinding(
                        new FoodViewModel(itemView.getContext(), food,mainViewModel,getAdapterPosition()));
            } else {
                binding.getFoodBinding().setFood(food);
            }
        }
    }


    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.adapter_item, parent, false
        );
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bindFood(foodItemList.get(position));
        holder.binding.executePendingBindings();

    }


    public int getItemCount() {
        return foodItemList.size();
    }


}
