package com.realvibedev.paoquente;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TelaInstrucoes extends AppCompatActivity {

    private Button btnPular, btnSeguinte;
    private ImageView img1, img2, img3;
    private int contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_instrucoes);

        btnPular = (Button) findViewById(R.id.button17);
        btnSeguinte = (Button) findViewById(R.id.button18);
        img1 = (ImageView) findViewById(R.id.imageView38);
        img2 = (ImageView) findViewById(R.id.imageView39);
        img3 = (ImageView) findViewById(R.id.imageView40);

        btnPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaInstrucoes.super.onBackPressed();
            }
        });

        btnSeguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador == 1){
                    img1.setVisibility(View.INVISIBLE);
                    contador = 2;
                }else if (contador == 2){
                    img2.setVisibility(View.INVISIBLE);
                    contador = 3;
                }else if (contador == 3){
                    TelaInstrucoes.super.onBackPressed();
                }
            }
        });

    }
}
