package pl.moras.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.moras.model.ToDoEvent;
import pl.moras.todoapplication.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoEventAdapter extends RecyclerView.Adapter<ToDoEventAdapter.MyViewHolder> {
    private final List<ToDoEvent> toDoEvents;
    private List<ToDoEvent> filtered; //filtered uzywany gdy wybiera sie przedzial czasowy zadan
    private final ItemClickListener itemClickListener; //listener z mainactivity
    private final Context context;
    public ToDoEventAdapter(ItemClickListener itemClickListener, Context context){
        this.itemClickListener = itemClickListener;
        this.toDoEvents = new ArrayList<>();
        this.context = context;
    }

    public void setTodos(List<ToDoEvent> todos){
        toDoEvents.clear();
        toDoEvents.addAll(todos);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ToDoEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoEventAdapter.MyViewHolder holder, int position) {
        ToDoEvent toDoEvent = toDoEvents.get(position);
        holder.description.setText(toDoEvent.getOpis());
        holder.dateStart.setText(context.getString(R.string.todo_start, dateToString(toDoEvent.getDatastart(), "dd/MM/YYYY")));
        holder.dateEnd.setText(context.getString(R.string.todo_end, dateToString(toDoEvent.getDatakoniec(), "dd/MM/YYYY HH:mm")));
        holder.deleteTask.setOnClickListener(view->itemClickListener.onTodoClick(toDoEvent));
    }

    @Override
    public int getItemCount() {
        return filtered!=null? filtered.size() : toDoEvents.size();

    }

    public void filterTodos(int days){
        filtered = toDoEvents
                .stream()
                .filter(toDoEvent -> toDoEvent.getDatakoniec().isBefore(LocalDateTime.now().plusDays(days)))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void deleteFilter(){
        filtered = null;
        notifyDataSetChanged();
    }

    private String dateToString(LocalDateTime date, String format){
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView description;
        private TextView dateStart;
        private TextView dateEnd;
        private TextView deleteTask;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteTask = itemView.findViewById(R.id.deleteTask);
            deleteTask.setTextColor(Color.RED);
            description = itemView.findViewById(R.id.opis);
            dateStart = itemView.findViewById(R.id.datastart);
            dateEnd = itemView.findViewById(R.id.datakoniec);
        }
    }
}
