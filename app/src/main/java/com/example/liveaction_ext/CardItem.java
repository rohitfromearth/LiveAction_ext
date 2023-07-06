package com.example.liveaction_ext;

public class CardItem {
    private final int logoResId;
    private final String title;
    private final String info1;
    private final String info2;
    private final String info3;

    public CardItem(int logoResId, String title, String info1, String info2, String info3) {
        this.logoResId = logoResId;
        this.title = title;
        this.info1 = info1;
        this.info2 = info2;
        this.info3 = info3;
    }

    public int getLogoResId() {
        return logoResId;
    }


    public String getTitle() {
        return title;
    }

    public String getInfo1() {
        return info1;
    }

    public String getInfo2() {
        return info2;
    }

    public String getInfo3() {
        return info3;
    }
}