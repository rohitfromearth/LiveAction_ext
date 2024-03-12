package com.ext.liveaction_ext;

import android.util.Log;

public class CardItem {
    private final int logoResId;
    private final String title;
    private final String ts_text;
    private final String average_text;
    private final String variance_text;
    private final String ts_value;
    private final String average_value;
    private final String variance_value;

    public CardItem(int logoResId, String title, String ts_text, String average_text, String variance_text, String ts_value, String average_value, String variance_value) {
        this.logoResId = logoResId;
        this.title = title;

        this.ts_text = ts_text;
        this.average_text = average_text;
        Log.d("average text",average_text);
        this.variance_text = variance_text;

        this.ts_value = ts_value;
        this.average_value = average_value;
        this.variance_value = variance_value;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public String getTitle() {
        return title;
    }

    public String getTs_text() {
        return ts_text;
    }

    public String getAverage_text() {
        return average_text;

    }

    public String getVariance_text() {
        return variance_text;
    }

    public String getTs_value() {
        return ts_value;
    }

    public String getAverage_value() {
        return average_value;
    }

    public String getVariance_value() {
        return variance_value;
    }
}