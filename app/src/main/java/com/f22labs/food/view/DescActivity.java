package com.f22labs.food.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.f22labs.food.R;
import com.f22labs.food.databinding.ActivityDescriptionBinding;
import com.f22labs.food.model.Food;
import com.f22labs.food.viewmodel.DescViewModel;

public class DescActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ActivityDescriptionBinding activityDescriptionBinding = DataBindingUtil.setContentView(this, R.layout.activity_description);
        Food foodItemModel = getIntent().getParcelableExtra(getString(R.string.food_item));
        activityDescriptionBinding.setDesc(new DescViewModel(this, foodItemModel));
    }


}
