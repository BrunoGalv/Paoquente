package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class TelaPromocao extends AppCompatActivity {

    private TextView txtNomePadaria, txtNomePromo, txtDesc, txtPreco;
    private ImageView imgBack, imgPromo;
    private Intent intent;
    private RelativeLayout relativeLayout;
    private int aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_promocao);

        txtNomePadaria = findViewById(R.id.textView148);
        txtNomePromo = findViewById(R.id.textView145);
        txtDesc = findViewById(R.id.textView146);
        txtPreco = findViewById(R.id.textView147);
        imgBack = findViewById(R.id.imageView123);
        imgPromo = findViewById(R.id.imageView126);
        relativeLayout = findViewById(R.id.fhcfh);
        ImageView imageView = findViewById(R.id.imageView125);

        intent = getIntent();

        aux = intent.getIntExtra("aux", 0);

        if (aux == 1){
            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 0, 20, 0);
            imageView.setLayoutParams(params);
        }
        txtNomePromo.setText(intent.getStringExtra("nomePromo"));
        txtNomePadaria.setText(intent.getStringExtra("nomePadaria"));
        txtDesc.setText(intent.getStringExtra("desc"));
        txtPreco.setText(intent.getStringExtra("preco"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("imagem")).apply(new RequestOptions()
                .centerCrop()
        ).into(imgPromo);

        if (intent.getBooleanExtra("aux", false)){
            relativeLayout.setVisibility(View.INVISIBLE);
        }

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(TelaPromocao.this, TelaPadaria.class);
                intent2.putExtra("Key", intent.getStringExtra("Key"));
                intent2.putExtra("Nome", intent.getStringExtra("nomePadaria"));
                startActivity(intent2);
            }
        });

        imgPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromocao.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_imagem, null);
                alerta.setView(viewAlerta);
                TouchImageView img = (TouchImageView) viewAlerta.findViewById(R.id.touchImageView3);
                Picasso.get().load(intent.getStringExtra("imagem")).into(img);
                alerta.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alerta.setCancelable(true);
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaPromocao.this.onBackPressed();
            }
        });

    }
}
