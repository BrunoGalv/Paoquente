package com.realvibedev.paoquente.Padaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.TelaPrincipal;
import com.google.firebase.auth.FirebaseAuth;

public class TelaConfigAdministrativo extends AppCompatActivity {

    private Button btnSair, btnFale, btnDesenvolvedora, btnPerfil;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_config_administrativo);

        btnFale = (Button) findViewById(R.id.button25);
        btnSair = (Button) findViewById(R.id.button27);
        btnDesenvolvedora = (Button) findViewById(R.id.button26);
        btnPerfil = (Button) findViewById(R.id.button24);
        imgBack = (ImageView) findViewById(R.id.imageView43);


        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaConfigAdministrativo.this, TelaPerfilPadaria.class));
            }
        });

        btnFale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=5521969110444&text=Olá,%20estou%20com%20uma%20dúvida%20no%20App%20Pão%20Quente."));
                view.getContext().startActivity(intent);
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(TelaConfigAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alerta.setTitle("Atenção!");
                alerta.setMessage("Tem certeza que deseja sair?");
                alerta.setIcon(R.mipmap.ic_info);
                alerta.setCancelable(true);
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(TelaConfigAdministrativo.this, TelaPrincipal.class));
                        finishAffinity();
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
        });

        btnDesenvolvedora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://realvibedev.com/sobre-nos/"));
                startActivity(i);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaConfigAdministrativo.super.onBackPressed();
            }
        });
    }
}
