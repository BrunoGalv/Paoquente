package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class TelaConfig extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_config);

        Button btnMensagens = (Button) findViewById(R.id.button2);
        Button btnEditPerfil = (Button) findViewById(R.id.button3);
        Button btnEstabelecimento = (Button) findViewById(R.id.button4);
        Button btnSobre = (Button) findViewById(R.id.button5);
        Button btnConfig = (Button) findViewById(R.id.button8);
        Button btnAjuda = (Button) findViewById(R.id.button48);
        imgBack = findViewById(R.id.imageView71);
        txtEntrar = findViewById(R.id.textView31);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            txtEntrar.setText("Sair");
            txtEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(TelaConfig.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção!");
                    alerta.setMessage("Tem certeza que deseja sair?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(true);
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(TelaConfig.this, TelaPrincipal.class));
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
            btnEditPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(TelaConfig.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção!");
                    alerta.setMessage("Tem certeza que deseja sair?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(true);
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(TelaConfig.this, TelaPrincipal.class));
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
        }else {
            btnEditPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(TelaConfig.this, TelaCadastro.class));
                }
            });
        }


        btnMensagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(TelaConfig.this, TelaMensagens.class));
                }else{
                    startActivity(new Intent(TelaConfig.this, TelaCadastro.class));
                }
            }
        });


        btnEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaConfig.this, TelaPainel.class));
            }
        });

        btnSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.realvibedev.paoquente"));
                startActivity(i);
            }
        });

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaConfig.this, TelaConfiguracoes.class));
            }
        });

        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://api.whatsapp.com/send?phone=5521969110444&text=Olá,%20estou%20com%20uma%20dúvida%20no%20App%20Pão%20Quente."));
                startActivity(i);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaConfig.super.onBackPressed();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
