package com.f22labs.food.viewmodel;

import android.content.Context;
import android.database.Observable;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.f22labs.food.R;
import com.f22labs.food.model.Food;
import com.f22labs.food.room.FoodDatabase;
import com.f22labs.food.utils.AppConstants;
import com.f22labs.food.view.CartItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartMainViewModel extends BaseObservable {


    private List<Food> foodList;
    private Context context;
    private float total = 0;

    @Bindable
    public ObservableField<String> getFirstCoupon() {
        return firstCoupon;
    }

    @Bindable
    public ObservableField<String> getSecCoupon() {
        return secCoupon;
    }

    private ObservableField<String> firstCoupon = new ObservableField<>("");

    private ObservableField<String> secCoupon =new ObservableField<>("");

    @Bindable
    public float getGrand() {
        return grand;
    }

    private float grand = 0;

    @Bindable
    public List<Food> getFoodList() {
        return foodList;
    }


    public CartMainViewModel(Context context) {
        this.context = context;
        foodList = new ArrayList<>();
        cartItemAdapter = new CartItemAdapter();

    }


    @Bindable
    public CartItemAdapter getCartItemAdapter() {
        return cartItemAdapter;
    }


    private CartItemAdapter cartItemAdapter;

    public void getFoodDetails() {
        if (foodList.isEmpty()) {
            foodList = new ArrayList<>();
            loadFoodItems();
        }
    }


    @Bindable
    public float getFirstAlpha() {
        return total > 100 ? 1.0f : 0.5f;

    }

    @Bindable
    public boolean getFirstEnabled() {
        return total > 100;

    }

    @Bindable
    public float getSecAlpha() {
        return total > 400 ? 1.0f : 0.5f;

    }

    @Bindable
    public boolean getSectEnabled() {
        return total > 400;

    }

    @Bindable
    public String getTotalPrice() {

        return AppConstants.RS + String.valueOf(getTotal());

    }

    public float getTotal() {
        total=0;
        for (int i = 0; i < foodList.size(); i++) {
            total += (foodList.get(i).getQuantity() * foodList.get(i).getItemPrice());
        }
        return total;
    }

    public void onItemClick(View view) {
        notifyPropertyChanged(BR.grandTotalPrice);
        notifyPropertyChanged(BR.firstAlpha);
        notifyPropertyChanged(BR.firstEnabled);
        notifyPropertyChanged(BR.secAlpha);
        notifyPropertyChanged(BR.sectEnabled);
    }

    @Bindable
    public String getGrandTotalPrice() {
        if (getFirstCoupon().get().equals(context.getString(R.string.F22LABS))) {
            grand = grand - (float) (grand * (0.2));
            Toast.makeText(context, context.getString(R.string.coupon_applied), Toast.LENGTH_SHORT).show();
            firstCoupon = new ObservableField<>();
            notifyPropertyChanged(BR.firstCoupon);

        } else if (getSecCoupon().get().equals(context.getString(R.string.FREEDEL))) {
            grand = (grand - 25);
            Toast.makeText(context, context.getString(R.string.coupon_applied), Toast.LENGTH_SHORT).show();
            secCoupon = new ObservableField<>();
            notifyPropertyChanged(BR.secCoupon);
        } else {
            grand = getTotal() + 25;
        }

        notifyPropertyChanged(BR.firstAlpha);
        notifyPropertyChanged(BR.firstEnabled);
        notifyPropertyChanged(BR.secAlpha);
        notifyPropertyChanged(BR.sectEnabled);
        return AppConstants.RS + String.valueOf(grand);
    }

    private void loadFoodItems() {
        FoodDatabase foodDatabase = FoodDatabase.getInstance(context);
        foodList.addAll(foodDatabase.getFoodDao().getAll());
    }

}