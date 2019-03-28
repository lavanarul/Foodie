package com.f22labs.food.view;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.f22labs.food.R;
import com.f22labs.food.databinding.ActivityHomeBinding;
import com.f22labs.food.viewmodel.MainViewModel;

public class HomeActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    ActivityHomeBinding activityHomeBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();

    }

    private void initializeViews() {
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mainViewModel = new MainViewModel(this);
        activityHomeBinding.setFood(mainViewModel);

        Toolbar toolbar = activityHomeBinding.toolBar;
        setSupportActionBar(toolbar);

        RecyclerView itemRecycler = activityHomeBinding.itemRecycler;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        itemRecycler.setLayoutManager(linearLayoutManager);
        HomeItemAdapter homeItemAdapter = new HomeItemAdapter(mainViewModel);
        itemRecycler.setAdapter(homeItemAdapter);

    }

    @Override
    protected void onStart() {
        mainViewModel.getFoodDetails();
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
                    mainViewModel.getPriceSort();
                    dialog.dismiss();
                } else {
                    mainViewModel.getRatingSort();
                    dialog.dismiss();

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
