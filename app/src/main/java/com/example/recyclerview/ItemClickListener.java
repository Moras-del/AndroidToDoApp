package com.example.recyclerview;

import com.example.model.ToDoEvent;

@FunctionalInterface
public interface ItemClickListener {
    void deleteItem(ToDoEvent toDoEvent);
}
