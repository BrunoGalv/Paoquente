package com.realvibedev.paoquente;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TelaConfiguracoes extends AppCompatActivity {

    private ImageView imgBack;
    private Button btnNot, btnContato, btnAjuda, btnTermosdeUso, btnPoliticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_configuracoes);

        imgBack = (ImageView) findViewById(R.id.imageView162);
        btnNot = (Button) findViewById(R.id.button41);
        btnAjuda = (Button) findViewById(R.id.button42);
        btnContato = (Button) findViewById(R.id.button43);
        btnTermosdeUso = (Button) findViewById(R.id.button44);
        btnPoliticas = (Button) findViewById(R.id.button46);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaConfiguracoes.super.onBackPressed();
            }
        });

        btnNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaConfiguracoes.this, TelaConfigNotificacoeseOutros.class));
            }
        });

        btnContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://api.whatsapp.com/send?phone=5521969110444"));
                startActivity(i);
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

        btnTermosdeUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaConfiguracoes.this, TelaTermosdeUso.class));
            }
        });

        btnPoliticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaConfiguracoes.this, TelaTermosdeUso.class));
            }
        });

    }
}
