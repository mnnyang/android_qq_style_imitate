package com.mnn.nnn.listindex;

import android.support.annotation.NonNull;

/**
 * Created by mnn on 2016/12/10.
 */

public class Friend implements Comparable<Friend> {
    public Friend(String name) {
        this.name = name;
        setPinYin(PinYingUtil.getPinyin(name));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setPinYin(PinYingUtil.getPinyin(name));
    }

    private String name;

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    private String pinYin;

    @Override
    public int compareTo(@NonNull Friend another) {

        return pinYin.compareTo(another.getPinYin());
    }
}
