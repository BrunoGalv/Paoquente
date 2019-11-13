package com.realvibedev.paoquente;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.realvibedev.paoquente.Padaria.PainelAdministrativo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements Runnable{

    private int aux = 0, aux2 = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF661612"),
                android.graphics.PorterDuff.Mode.MULTIPLY);*/

        VideoView view = (VideoView)findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.logoanimation;
        view.setVideoURI(Uri.parse(path));
        view.setMinimumHeight(180);
        view.setMinimumWidth(300);
        view.setZOrderOnTop(true);
        view.start();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (user != null) {
                db.collection("usersPro").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            aux = 1;
                            aux2 = 1;
                        }
                    }
                });
            }
        } else {
            Toast.makeText(MainActivity.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            aux = 2;
        }



        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (aux){
                            case 0:
                                startActivity(new Intent(MainActivity.this, TelaPrincipal.class));
                                finish();
                                break;
                            case 1:
                                startActivity(new Intent(MainActivity.this, PainelAdministrativo.class));
                                finish();
                                break;
                            case 2:
                                Intent intent = new Intent(MainActivity.this, TelaPermissaoLocalizacao.class);
                                intent.putExtra("aux", aux2);
                                startActivity(intent);
                                finish();
                                break;
                        }
                    }
                }, 2000);
    }

    @Override
    public void run(){
        switch (aux){
            case 0:
                startActivity(new Intent(MainActivity.this, TelaPrincipal.class));
                finish();
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, PainelAdministrativo.class));
                finish();
                break;
            case 2:
                Intent intent = new Intent(MainActivity.this, TelaPermissaoLocalizacao.class);
                intent.putExtra("aux", aux2);
                startActivity(intent);
                finish();
                break;
        }
    }

}
