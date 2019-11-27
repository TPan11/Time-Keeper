package com.btbsolutions.timekeeper.utility;

import android.support.annotation.NonNull;

public class HeaderItem extends ListItem {

    @NonNull
    private Double group;

    public HeaderItem(@NonNull Double group) {
        this.group = group;
    }

    @NonNull
    public Double getGroup() {
        return group;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
