package com.realvibedev.paoquente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class TelaMapa extends AppCompatActivity {
    private Intent intent;
    private TouchImageView imageViewMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mapa);

        imageViewMapa = (TouchImageView) findViewById(R.id.imageMapa);

        intent = getIntent();

        Picasso.get().load(intent.getStringExtra("ImagemMapa")).into(imageViewMapa);
    }
}
