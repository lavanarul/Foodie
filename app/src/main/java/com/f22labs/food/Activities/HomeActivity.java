package com.f22labs.food.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.f22labs.food.Adapters.HomeItemAdapter;
import com.f22labs.food.Listeners.LoadTotal;
import com.f22labs.food.Models.FoodItemModel;
import com.f22labs.food.Network.ApiCallInterface;
import com.f22labs.food.Network.RetrofitApiConfig;
import com.f22labs.food.R;
import com.f22labs.food.Utils.ApiUtils;
import com.f22labs.food.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private List<FoodItemModel> foodItemModelList;
    private ProgressDialog progressDialog;
    private ApiCallInterface apiInterface;
    private HomeItemAdapter homeItemAdapter;
    private RelativeLayout addCart;
    private TextView totalItem;
    private DatabaseHandler db;
    private TextView dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();
        dataFromDB();

    }

    private void dataFromDB() {
        db = new DatabaseHandler(this);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalItem.getText().toString() != null && !totalItem.getText().toString().isEmpty()) {
                    if (Integer.parseInt(totalItem.getText().toString()) > 0) {
                        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(HomeActivity.this, getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(HomeActivity.this, getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initializeViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        RecyclerView itemRecycler = findViewById(R.id.itemRecycler);
        addCart = findViewById(R.id.add_cart);
        totalItem = findViewById(R.id.total_item);
        dot = findViewById(R.id.dot);
        dot.setVisibility(View.GONE);
        apiInterface = RetrofitApiConfig.getClient().create(ApiCallInterface.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        foodItemModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        itemRecycler.setLayoutManager(linearLayoutManager);
        homeItemAdapter = new HomeItemAdapter(this, foodItemModelList);
        itemRecycler.setAdapter(homeItemAdapter);
    }

    @Override
    protected void onStart() {
        if (foodItemModelList.size() > 0) {
            dbSetFoodItems();
        } else {
            getFoodItemList();
        }
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                showRadioDialog();
                break;
            default:
                break;
        }

        return true;
    }

    private void showRadioDialog() {
        final CharSequence[] items = {getString(R.string.low), getString(R.string.rating)};
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(getString(R.string.filter));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Collections.sort(foodItemModelList, new Comparator<FoodItemModel>() {
                        @Override
                        public int compare(FoodItemModel p1, FoodItemModel p2) {
                            if (p1.getItemPrice() > p2.getItemPrice()) return 1;
                            if (p1.getItemPrice() < p2.getItemPrice()) return -1;
                            return 0;
                        }
                    });
                    dialog.dismiss();
                } else {
                    Collections.sort(foodItemModelList, new Comparator<FoodItemModel>() {
                        @Override
                        public int compare(FoodItemModel p1, FoodItemModel p2) {
                            if (p1.getAverageRating() < p2.getAverageRating()) return 1;
                            if (p1.getAverageRating() > p2.getAverageRating()) return -1;
                            return 0;
                        }
                    });
                    dialog.dismiss();

                }
                homeItemAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getFoodItemList() {
        if (ApiUtils.isNetworkAvailable(HomeActivity.this)) {
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
            Call<List<FoodItemModel>> callLogin = apiInterface.getFoodItemList();
            callLogin.enqueue(new Callback<List<FoodItemModel>>() {
                @Override
                public void onResponse(Call<List<FoodItemModel>> call, Response<List<FoodItemModel>> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        foodItemModelList.addAll(response.body());
                        dbSetFoodItems();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<List<FoodItemModel>> call, Throwable t) {
                    call.cancel();
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.check_network),
                    Toast.LENGTH_SHORT).show();
        }


        homeItemAdapter.setOnLoadTotal(new LoadTotal() {
            @Override
            public void Loading(List<FoodItemModel> foodItemModel) {
                int itemTotal = 0;
                for (int i = 0; i < foodItemModel.size(); i++) {
                    itemTotal += foodItemModel.get(i).getQuantity();
                }
                totalItem.setText(String.valueOf(itemTotal));
                dot.setVisibility(View.VISIBLE);
            }
        });

    }

    private void dbSetFoodItems() {
        List<FoodItemModel> dbFoodItemList = db.getAllFoodItems();
        for (int i = 0; i < foodItemModelList.size(); i++) {
            FoodItemModel foodItemModel = foodItemModelList.get(i);
            foodItemModel.setQuantity(0);
            int itemTotal = 0;
            for (int j = 0; j < dbFoodItemList.size(); j++) {
                if (foodItemModelList.get(i).getItemName().equalsIgnoreCase(dbFoodItemList.get(j).getItemName())) {
                    foodItemModel.setQuantity(dbFoodItemList.get(j).getQuantity());

                }
                itemTotal += dbFoodItemList.get(j).getQuantity();
                totalItem.setText(String.valueOf(itemTotal));
                dot.setVisibility(View.VISIBLE);
            }
        }
        homeItemAdapter.notifyItemInserted(foodItemModelList.size());
        homeItemAdapter.notifyDataSetChanged();
    }
}
