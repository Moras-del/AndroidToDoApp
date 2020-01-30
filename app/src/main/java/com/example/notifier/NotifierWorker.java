package com.example.notifier;

import android.app.AutomaticZenRule;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.model.AppDatabase;
import com.example.model.ToDoEvent;
import com.example.todoapplication.MainActivity;
import com.example.todoapplication.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NotifierWorker extends Worker {

    private static final int NOTIFICATION_ID = 101;

    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";

    private static final String CHANNEL_NAME = "Notification Channel";

    public NotifierWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        ToDoEvent toDoEvent = AppDatabase.getDBInstance(getApplicationContext()).toDoEventDao().getFirst();
        while (toDoEvent.getDatakoniec().isBefore(LocalDateTime.now())){
            AppDatabase.getDBInstance(getApplicationContext()).toDoEventDao().delete(toDoEvent);
            toDoEvent = AppDatabase.getDBInstance(getApplicationContext()).toDoEventDao().getFirst();
        }
        enableChannel();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, notifier(toDoEvent));
        return Result.success();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enableChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification notifier(ToDoEvent toDoEvent) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.options_button)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("ToDoApp przypomnienie")
                .setContentText(toDoEvent.getOpis() + " kończy się " + toDoEvent.getDatakoniec().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return notificationBuilder.build();
    }
}