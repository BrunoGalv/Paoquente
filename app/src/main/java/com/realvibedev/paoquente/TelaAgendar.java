package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.realvibedev.paoquente.Remote.APIService;
import com.realvibedev.paoquente.model.Data;
import com.realvibedev.paoquente.model.MyResponse;
import com.realvibedev.paoquente.model.Sender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaAgendar extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaReference = database.getReference();
    private APIService mService;
    private String key, nomePadaria;
    private Button btnNotificar;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_agendar);

        editText = (EditText) findViewById(R.id.editText11);
        btnNotificar = (Button) findViewById(R.id.button19);

        mService = Common.getFCMClient();


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            databaReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nomePadaria").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nomePadaria = (String) dataSnapshot.getValue();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            databaReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("key").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    key = (String) dataSnapshot.getValue();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
        else {
            Toast.makeText(TelaAgendar.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
        }


        btnNotificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    AlertDialog.Builder alerta = new AlertDialog.Builder(TelaAgendar.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção");
                    alerta.setMessage("Deseja notificar: " + "\"" + " " + editText.getText().toString() + " " + "\"" + " ?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(true);
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Notification notification = new Notification( editText.getText().toString() , nomePadaria, alarmSound,"");
                            Data data = new Data(nomePadaria, editText.getText().toString(), "1", "Hora do Pão", "0", "");
                            Sender sender = new Sender("/topics/" + key ,data);
                            mService.sendNotification(sender)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            Toast.makeText(TelaAgendar.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(TelaAgendar.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();

                }
                else {
                    Toast.makeText(TelaAgendar.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
