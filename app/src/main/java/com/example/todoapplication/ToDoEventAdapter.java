package com.example.todoapplication;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.ToDoEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoEventAdapter extends RecyclerView.Adapter<ToDoEventAdapter.MyViewHolder> {
    private final List<ToDoEvent> toDoEvents;
    private List<ToDoEvent> filtered; //filtered uzywany gdy wybiera sie przedzial czasowy zadan
    private ItemClickListener onClickListener; //listener z mainactivity

    ToDoEventAdapter(ItemClickListener itemClickListener){
        this.onClickListener = itemClickListener;
        this.toDoEvents = new ArrayList<>();
    }

    void setTodos(List<ToDoEvent> todos){
        toDoEvents.clear();
        toDoEvents.addAll(todos);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView description;
        private TextView dateStart;
        private TextView dateEnd;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.description = itemView.findViewById(R.id.opis);
            this.dateStart = itemView.findViewById(R.id.datastart);
            this.dateEnd = itemView.findViewById(R.id.datakoniec);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener!=null) onClickListener.onItemClick(view, toDoEvents.get(getAdapterPosition()));
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
    }

    @Override
    public int getItemCount() {
        return filtered!=null? filtered.size() : toDoEvents.size();

    }

    public interface ItemClickListener{
        void onItemClick(View view, ToDoEvent toDoEvent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    void filterTodos(int days){
        filtered = toDoEvents
                .stream()
                .filter(toDoEvent -> toDoEvent.getDatakoniec().isBefore(LocalDateTime.now().plusDays(days)))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    void deleteFilter(){
        filtered = null;
        notifyDataSetChanged();
    }


}
