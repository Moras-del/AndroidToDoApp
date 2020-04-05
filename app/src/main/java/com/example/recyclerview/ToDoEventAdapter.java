package com.example.recyclerview;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.ToDoEvent;
import com.example.todoapplication.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoEventAdapter extends RecyclerView.Adapter<ToDoEventAdapter.MyViewHolder> {
    private final List<ToDoEvent> toDoEvents;
    private List<ToDoEvent> filtered; //filtered uzywany gdy wybiera sie przedzial czasowy zadan
    private ItemClickListener itemClickListener; //listener z mainactivity

    public ToDoEventAdapter(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
        this.toDoEvents = new ArrayList<>();
    }

    public void setTodos(List<ToDoEvent> todos){
        toDoEvents.clear();
        toDoEvents.addAll(todos);
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView description;
        private TextView dateStart;
        private TextView dateEnd;
        private TextView deleteTask;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteTask = itemView.findViewById(R.id.deleteTask);
            description = itemView.findViewById(R.id.opis);
            dateStart = itemView.findViewById(R.id.datastart);
            dateEnd = itemView.findViewById(R.id.datakoniec);
        }
    }

    @NonNull
    @Override
    public ToDoEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ToDoEventAdapter.MyViewHolder holder, int position) {
        ToDoEvent toDoEvent = toDoEvents.get(position);
        holder.description.setText(toDoEvent.getOpis());
        holder.dateStart.setText("zaczęto: "+toDoEvent.getDatastart().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        holder.dateEnd.setText("Koniec: "+toDoEvent.getDatakoniec().format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm")));
        holder.deleteTask.setOnClickListener(view->itemClickListener.deleteItem(toDoEvent));
    }

    @Override
    public int getItemCount() {
        return filtered!=null? filtered.size() : toDoEvents.size();

    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
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


}
