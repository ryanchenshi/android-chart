package com.example.myapplication.chart;

public class ProfitData {
    private float from;
    private float to;
    private int value;
    private String title;

    public ProfitData(float from, float to, int value, String title) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.title = title;
    }

    public float getFrom() {
        return from;
    }

    public float getTo() {
        return to;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
