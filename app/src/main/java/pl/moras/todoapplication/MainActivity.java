package pl.moras.todoapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import pl.moras.daysvalues.Days;
import pl.moras.model.ToDoEvent;
import pl.moras.notifier.NotifierOptions;
import pl.moras.notifier.NotifierWorker;
import pl.moras.recyclerview.ItemClickListener;
import pl.moras.recyclerview.ToDoEventAdapter;


import pl.moras.viewmodel.ToDoViewModel;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ToDoEventAdapter customAdapter;
    private ToDoViewModel toDoViewModel;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotifierOptions.initialize(this);


        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getToDoEventList().observe(this, observer);
        toDoViewModel.deleteOldTodos();

        customAdapter = new ToDoEventAdapter(itemClickListener, this);
        
        recyclerView =                       findViewById(R.id.recyclerView);
        Switch notifierSwitch =              findViewById(R.id.notifierswitch);
        spinner =                            findViewById(R.id.spinner);
        Button newTodoButton = findViewById(R.id.newtodobutton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinneritem, Days.getDays(this)));
        if (!NotifierOptions.getCachedDescriptionsSet().isEmpty()) { //show dialog with tasks which were deleted due to expiration
            showDeletedTodos(NotifierOptions.getCachedDescriptionsSet());
            NotifierOptions.removeCachedTodos();
        }

        //otwórz kreator zadań
        newTodoButton.setOnClickListener(view -> {    // nowe zadanie
            DialogFragment dialogFragment = new NewToDoFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialogFragment.show(fragmentManager, "newtodo");
        });
        

        boolean isWorkerEnabled = NotifierOptions.getNotifierState();
        notifierSwitch.setChecked(isWorkerEnabled);
        notifierSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){
                PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotifierWorker.class, 15, TimeUnit.MINUTES).build();
                WorkManager.getInstance(getApplicationContext()).enqueue(periodicWorkRequest);
                NotifierOptions.saveWorkerId(periodicWorkRequest);
                NotifierOptions.saveNotifierState(true);
            } else {
                WorkManager.getInstance(getApplicationContext()).cancelWorkById(NotifierOptions.getWorkerId());
                NotifierOptions.saveNotifierState(false);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {   //wybierz przedzial czasowy do filtrowania zadan
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Days choosenDay = Days.values()[position];
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
        MobileAds.initialize(this);
    }

    private void showDeletedTodos(Set<String> descriptionsSet) {
        String[] descriptions = descriptionsSet.toArray(new String[0]);
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.deleted_todos_dialog_title))
                .setItems(descriptions, (dialogInterface, i)->{})
                .setPositiveButton(getText(R.string.ok), (dialogInterface, i) -> { })
                .show();
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

    private ItemClickListener itemClickListener = (this::deleteTodo);

    private void deleteTodo(ToDoEvent toDoEventToDelete){ //alertdialog z zapytaniem o usuniecie zadania
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.delete_request))
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> toDoViewModel.delete(toDoEventToDelete))
                .show();
    }
}
