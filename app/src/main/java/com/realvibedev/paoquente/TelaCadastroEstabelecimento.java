package com.realvibedev.paoquente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.billingclient.api.BillingClient;

public class TelaCadastroEstabelecimento extends AppCompatActivity {

    private ImageView imgBack;
    private Button btnFree, btnPremium;
    private BillingClient billingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_estabelecimento);

        imgBack = findViewById(R.id.imageView70);
        btnFree = findViewById(R.id.button37);
        btnPremium = findViewById(R.id.button38);


        btnFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaCadastroEstabelecimento.this, TelaCadastrar.class);
                intent.putExtra("aux", 0);
                startActivity(intent);
            }
        });

        btnPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaCadastroEstabelecimento.this, TelaCadastrar.class);
                intent.putExtra("aux", 1);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaCadastroEstabelecimento.super.onBackPressed();
            }
        });
    }
}
