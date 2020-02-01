package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.chart.ProfitData;
import com.example.myapplication.chart.ProfitView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ProfitData> mProfitDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ProfitView profitView = findViewById(R.id.profitView);

        mProfitDatas.add(new ProfitData(0, 10, 3, "deep sleep", Color.DKGRAY));
        mProfitDatas.add(new ProfitData(10, 15, 1, "out sleep", Color.GRAY));
        mProfitDatas.add(new ProfitData(15, 20, 2, "light sleep", Color.GREEN));
        mProfitDatas.add(new ProfitData(20, 22, 1, "out sleep", Color.GRAY));
        mProfitDatas.add(new ProfitData(22, 100, 2, "light sleep", Color.GREEN));
        profitView.setProfitDataList(mProfitDatas);
    }
}
