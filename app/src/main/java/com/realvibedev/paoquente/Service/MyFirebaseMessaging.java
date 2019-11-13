package com.realvibedev.paoquente.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.realvibedev.paoquente.MainActivity;
import com.realvibedev.paoquente.Padaria.TelaChat;
import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.TelaChatCliente;
import com.realvibedev.paoquente.TelaPromocao;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

/**
 * Created by bruno on 29/03/2018.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private String titulo, texto, imagem, aux, chId, userId;
    private SharedPreferences preferences;
    private Boolean auxNot = true;
    private int aux2;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        chId = "Diversos";

        Calendar calendar = Calendar.getInstance();
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);
        final int horaAtual = Integer.parseInt(String.valueOf(currentHour) + String.valueOf(currentMinute));

        if(preferences.getBoolean("notificacoes", true)){
            if (preferences.getBoolean("notificacoesHora", false)){
                String hora = preferences.getString("notificacoesHora1", "");
                String hora2 = preferences.getString("notificacoesHora2", "");
                if (!hora.equals("") && !hora2.equals("") && hora.length() == 5 && hora2.length() == 5){
                    int horaInt = Integer.parseInt(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1)) + String.valueOf(hora.charAt(3)) + String.valueOf(hora.charAt(4)));
                    int horaInt2 = Integer.parseInt(String.valueOf(hora2.charAt(0)) + String.valueOf(hora2.charAt(1)) + String.valueOf(hora2.charAt(3)) + String.valueOf(hora2.charAt(4)));
                    auxNot = horaInt > horaAtual && horaAtual < horaInt2;
                }else {
                    auxNot = true;
                }
            }else {
                auxNot = true;
            }
        }else {
            auxNot = false;
        }

        if (auxNot) {

            Map<String, String> data = remoteMessage.getData();
            titulo = data.get("titulo");
            texto = data.get("texto");
            imagem = data.get("imagem");
            aux = data.get("aux");
            userId = data.get("userId");
            aux2 = Integer.parseInt(aux);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intent = new Intent();

            switch (aux2){
                case 0:
                    intent = new Intent(this, MainActivity.class);
                    break;
                case 1:
                    intent = new Intent(this, TelaChat.class);
                    intent.putExtra("aux", 1);
                    intent.putExtra("Key", userId);
                    break;
                case 2:
                    intent = new Intent(this, TelaChatCliente.class);
                    intent.putExtra("Key", userId);
                    intent.putExtra("Nome", titulo);
                    break;
                case 3:
                    intent = new Intent(this, TelaPromocao.class);
                    intent.putExtra("aux", 1);
                    intent.putExtra("nomePadaria", titulo);
                    intent.putExtra("desc", texto);
                    intent.putExtra("imagem", imagem);
                    intent.putExtra("Key", userId);
                    break;
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("default",
                        chId,
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(chId);
                mNotificationManager.createNotificationChannel(channel);
            }


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "Default";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notifications", NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                notificationChannel.setDescription("Notificações");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(R.color.colorPrimary);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }


            if (imagem.equals("")) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(titulo)
                        .setContentText(texto)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .setContentIntent(pendingIntent);
                switch (aux2) {
                    case 1:
                        notificationManager.notify(m, builder.build());
                        break;
                    default:
                        notificationManager.notify(0, builder.build());
                }
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(titulo)
                        .setContentText(texto)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .setLargeIcon(getBitmapFromURL(imagem))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(imagem)))
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);
                switch (aux2) {
                    case 1:
                        notificationManager.notify(m, builder.build());
                        break;
                    default:
                        notificationManager.notify(0, builder.build());

                }
            }
            //showNotification(remoteMessage.getNotification());
        }

    }

   /* private void showNotification(RemoteMessage.Notification notification) {

        Intent intent = new Intent(this, PainelAdministrativo.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (notification.getColor().equals("") || notification.getColor().equals(null)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(notification.getTitle())
                    .setContentText(string)
                    .setSound(alarmSound)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }else{

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setSound(alarmSound)
                    .setLargeIcon(getBitmapFromURL(notification.getColor()))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(notification.getColor())))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }

    }*/

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
