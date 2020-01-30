package com.example.todoapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.daysvalues.Days;
import com.example.model.ToDoEvent;
import com.example.notifier.NotifierOptions;
import com.example.notifier.NotifierWorker;
import com.example.viewmodel.ToDoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int NORMAL_HEIGHT;
    private int CLICKED_HEIGHT;
    private RecyclerView recyclerView;
    private ToDoEventAdapter customAdapter;
    private ToDoViewModel toDoViewModel;
    private NotifierOptions notifierOptions;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        toDoViewModel.getToDoEventList().observe(this, observer);
        toDoViewModel.deleteOldTodos();

        customAdapter = new ToDoEventAdapter(itemClickListener);
        
        recyclerView =                       findViewById(R.id.recyclerView);
        Switch notifierSwitch =              findViewById(R.id.notifierswitch);
        spinner =                    findViewById(R.id.spinner);
        FloatingActionButton newTodoButton = findViewById(R.id.newtodobutton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinneritem, Days.values()));

        newTodoButton.setOnClickListener(new View.OnClickListener() {  //otwórz kreator zadań
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {    // nowe zadanie
                DialogFragment dialogFragment = new NewToDoFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager, "newtodo");
            }
        });
        
        notifierOptions = new NotifierOptions(this);
        boolean isWorkerEnabled = notifierOptions.getOptionState();
        notifierSwitch.setChecked(isWorkerEnabled);
        notifierSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked){
                    PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotifierWorker.class, 15, TimeUnit.MINUTES).build();
                    WorkManager.getInstance(getApplicationContext()).enqueue(periodicWorkRequest);
                    notifierOptions.saveWorkerId(periodicWorkRequest);
                    notifierOptions.saveOptionState(true);
                } else {
                    WorkManager.getInstance(getApplicationContext()).cancelWorkById(notifierOptions.getWorkerId());
                    notifierOptions.saveOptionState(false);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {   //wybierz przedzial czasowy do filtrowania zadan
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String text = adapterView.getItemAtPosition(position).toString();
                Days choosenDay = Days.fromString(text);
                if (choosenDay == Days.ALL)
                    customAdapter.deleteFilter();
                else {
                    customAdapter.filterTodos(choosenDay.getNumOfDays());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private Observer<List<ToDoEvent>> observer = new Observer<List<ToDoEvent>>() {
        @Override
        public void onChanged(List<ToDoEvent> toDoEvents) {
            customAdapter.setTodos(toDoEvents);
            customAdapter.deleteFilter();   //
            spinner.setSelection(0);        //ustaw recyclerview na widok domyslny (usun filtrowanie zadan)
            if (recyclerView.getAdapter()==null)
                recyclerView.setAdapter(customAdapter);
        }
    };

    private ToDoEventAdapter.ItemClickListener itemClickListener = new ToDoEventAdapter.ItemClickListener() { //animacja klikniecia viewu z recycleview
        @Override
        public void onItemClick(View view, ToDoEvent toDoEvent) {
            if (NORMAL_HEIGHT==0) {                 //sprawdz normalna wysokosc viewu
                NORMAL_HEIGHT = view.getHeight();
                CLICKED_HEIGHT = NORMAL_HEIGHT+60;
            }
            boolean open;
            ValueAnimator anim = null;
            if (view.getHeight()==NORMAL_HEIGHT) {                  //sprawdz czy view jest juz klikniety...
                open = false;
                anim = ValueAnimator.ofInt(NORMAL_HEIGHT, CLICKED_HEIGHT);  //...jak nie to view sie "rozciaga"...
            } else {
                open = true;
                anim = ValueAnimator.ofInt(CLICKED_HEIGHT, NORMAL_HEIGHT);  //...jak tak to view sie "zwija"
            }
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    view.getLayoutParams().height = (int)valueAnimator.getAnimatedValue();
                    view.requestLayout();
                    View deleteText = view.findViewById(R.id.deleteTask);
                    if (open) {
                        deleteText.setVisibility(View.INVISIBLE);                           //pokaz/ukryj textview z zapytaniem u usunieciem zadania
                    } else {
                        deleteText.setVisibility(View.VISIBLE);
                        deleteText.setOnClickListener(v-> deleteTodo(toDoEvent));
                    }
                }
            });
            anim.setDuration(100);
            anim.start();
        }
    };

    private void deleteTodo(ToDoEvent toDoEventToDelete){ //alertdialog z zapytaniem o usuniecie zadania
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Usunąć?");
        alertDialogBuilder.setPositiveButton("tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toDoViewModel.delete(toDoEventToDelete);
            }
        });
        alertDialogBuilder.setNegativeButton("nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialogBuilder.show();
    }
}
