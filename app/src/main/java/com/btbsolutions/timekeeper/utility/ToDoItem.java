package com.btbsolutions.timekeeper.utility;

import android.support.annotation.NonNull;

public class ToDoItem extends ListItem {

    @NonNull
    private ToDo toDo;

    public ToDoItem(@NonNull ToDo toDo) {
        this.toDo = toDo;
    }

    @NonNull
    public ToDo getToDo() {
        return toDo;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }
}
