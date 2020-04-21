package pl.moras.recyclerview;

import pl.moras.model.ToDoEvent;

@FunctionalInterface
public interface ItemClickListener {
    void onTodoClick(ToDoEvent toDoEvent);
}
