package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.Remote.APIService;
import com.realvibedev.paoquente.model.Data;
import com.realvibedev.paoquente.model.MyResponse;
import com.realvibedev.paoquente.model.Sender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaNotificarImagem extends AppCompatActivity {
    private ImageView imageView, imageView2;
    private Button button, buttonImagem;
    private EditText editText;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaReference = database.getReference();
    private APIService mService;
    private String key, nomePadaria, imagem = "";
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private TextView textView;
    int aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_notificar_imagem);

        imageView = (ImageView) findViewById(R.id.imageView15);
        imageView2 = (ImageView) findViewById(R.id.imageView42);
        editText = (EditText) findViewById(R.id.editText12);
        button = (Button) findViewById(R.id.button21);
        buttonImagem = (Button) findViewById(R.id.button22);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        textView = (TextView) findViewById(R.id.textView33);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF661612"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        mService = Common.getFCMClient();
        storageReference = FirebaseStorage.getInstance().getReference();

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
            Toast.makeText(TelaNotificarImagem.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    AlertDialog.Builder alerta = new AlertDialog.Builder(TelaNotificarImagem.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção");
                    alerta.setMessage("Deseja notificar: " + "\"" + " " + editText.getText().toString() + " " + "\"" + " ?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(true);
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Notification notification = new Notification(editText.getText().toString(), nomePadaria, alarmSound, imagem);
                            Data data = new Data(nomePadaria, editText.getText().toString(), imagem, "Imagem    ", "0", "");
                            Sender sender = new Sender("/topics/" + key , data);
                            mService.sendNotification(sender)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            Toast.makeText(TelaNotificarImagem.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(TelaNotificarImagem.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TelaNotificarImagem.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 2);
            }
        });




    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        progressBar.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        button.setClickable(false);
        buttonImagem.setClickable(false);
        aux = 1;
        if(requestCode == 2 && resultCode == RESULT_OK){
            aux = 2;
            Uri selectedImage = imageReturnedIntent.getData();
            imageView.setImageURI(selectedImage);
            progressBar.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            button.setClickable(false);
            buttonImagem.setClickable(false);
            StorageReference filepath = storageReference.child("ImagensNotificacoes").child(selectedImage.getLastPathSegment());
            filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagem = uri.toString();
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    button.setClickable(true);
                    buttonImagem.setClickable(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    button.setClickable(true);
                    buttonImagem.setClickable(true);
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    button.setClickable(true);
                    buttonImagem.setClickable(true);
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    button.setClickable(true);
                    buttonImagem.setClickable(true);
                }
            });
        }
        if (aux  == 1) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            button.setClickable(true);
            buttonImagem.setClickable(true);
        }

    }

}
