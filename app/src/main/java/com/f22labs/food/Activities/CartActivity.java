package com.f22labs.food.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.f22labs.food.Adapters.CartItemAdapter;
import com.f22labs.food.Models.FoodItemModel;
import com.f22labs.food.R;
import com.f22labs.food.Utils.AppConstants;
import com.f22labs.food.Utils.DatabaseHandler;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView itemRecycler;
    private TextView totAmount;
    private TextView grandTotal;
    private TextView applyButton;
    private EditText firstCoupon, secCoupon;
    private float total = 0f;
    private float grand = 0f;
    private DatabaseHandler databaseHandler;
    private LinearLayout couponLayout, firstLayout, secLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_cart);
        initializeViews();
        getDataFromDb();

    }

    private void getDataFromDb() {
        List<FoodItemModel> foodItemModelList = databaseHandler.getAllFoodItems();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        itemRecycler.setLayoutManager(linearLayoutManager);
        CartItemAdapter cartItemAdapter = new CartItemAdapter(this, foodItemModelList);
        itemRecycler.setAdapter(cartItemAdapter);

        for (int i = 0; i < foodItemModelList.size(); i++) {
            total += (foodItemModelList.get(i).getQuantity() * foodItemModelList.get(i).getItemPrice());
        }
        if (total > 0) {
            totAmount.setText(AppConstants.RS + String.valueOf(total));

            grand = total + 25;
            grandTotal.setText(AppConstants.RS + String.valueOf(grand));
        }

        if (total > 400) {
            couponLayout.setAlpha(1.0f);
            firstCoupon.setEnabled(true);
            secCoupon.setEnabled(true);
            firstLayout.setAlpha(1.0f);
            secLayout.setAlpha(1.0f);
        } else if (total > 100) {
            couponLayout.setAlpha(1.0f);
            secCoupon.setEnabled(true);
            firstCoupon.setEnabled(false);
            firstLayout.setAlpha(0.5f);
            secLayout.setAlpha(1.0f);
        } else {
            firstCoupon.setEnabled(false);
            secCoupon.setEnabled(false);
            couponLayout.setAlpha(0.5f);

        }

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstCoupon.getText().toString().equals(getString(R.string.F22LABS))) {
                    grandTotal.setText(AppConstants.RS + String.valueOf(grand - (float) (grand * (0.2))));
                    Toast.makeText(CartActivity.this, getString(R.string.coupon_applied), Toast.LENGTH_SHORT).show();
                    firstCoupon.setText("");
                    firstCoupon.setEnabled(false);
                    secCoupon.setEnabled(false);
                    couponLayout.setAlpha(0.5f);


                }

                if (secCoupon.getText().toString().equals(getString(R.string.FREEDEL))) {
                    Toast.makeText(CartActivity.this, getString(R.string.coupon_applied), Toast.LENGTH_SHORT).show();
                    grandTotal.setText(AppConstants.RS + String.valueOf(grand - 25));
                    secCoupon.setText("");
                    firstCoupon.setEnabled(false);
                    secCoupon.setEnabled(false);
                    couponLayout.setAlpha(0.5f);

                }


            }
        });


    }

    private void initializeViews() {
        couponLayout = findViewById(R.id.couponLayout);
        itemRecycler = findViewById(R.id.itemRecycler);
        totAmount = findViewById(R.id.total_amount);
        grandTotal = findViewById(R.id.grand_total);
        applyButton = findViewById(R.id.applyButton);
        TextView continueButton = findViewById(R.id.continueButton);
        firstCoupon = findViewById(R.id.firstCoupon);
        firstLayout = findViewById(R.id.firstLayout);
        secLayout = findViewById(R.id.secLayout);
        secCoupon = findViewById(R.id.secCoupon);
    }

}
