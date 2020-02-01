package com.example.myapplication.chart;

public class ProfitData {
    private float from;
    private float to;
    private int value;
    private String title;
    private int color;

    public ProfitData(float from, float to, int value, String title, int color) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.title = title;
        this.color = color;
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

    public int getColor() {
        return color;
    }
}
