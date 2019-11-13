package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.realvibedev.paoquente.Padaria.PainelAdministrativo;

public class TelaPermissaoLocalizacao extends AppCompatActivity {

    private Button button;
    private int aux = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_permissao_localizacao);

        button = findViewById(R.id.button45);

        Intent intent = getIntent();
        aux = intent.getIntExtra("aux", 0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(TelaPermissaoLocalizacao.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    switch (aux){
                        case 0:
                            startActivity(new Intent(TelaPermissaoLocalizacao.this, TelaPrincipal.class));
                            finish();
                            break;
                        case 1:
                            startActivity(new Intent(TelaPermissaoLocalizacao.this, PainelAdministrativo.class));
                            finish();
                            break;
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    AlertDialog.Builder alerta = new AlertDialog.Builder(TelaPermissaoLocalizacao.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Localização necessária");
                    alerta.setMessage("Sem a localização não podemos checar quais padarias estão perto de você!");
                    //alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(false);
                    alerta.setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
