package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TelaComercio extends AppCompatActivity {
    private EditText edtNome;
    private EditText edtEstado;
    private EditText edtCidade;
    private EditText edtBairro;
    private EditText edtNomePessoa;
    private EditText edtEmail;
    private EditText edtCelular;
    private EditText edtComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_comercio);

        edtNome = (EditText) findViewById(R.id.editText3);
        edtEstado = (EditText) findViewById(R.id.editText4);
        edtCidade = (EditText) findViewById(R.id.editText5);
        edtBairro = (EditText) findViewById(R.id.editText6);
        edtNomePessoa = (EditText) findViewById(R.id.editText7);
        edtEmail = (EditText) findViewById(R.id.editText8);
        edtCelular = (EditText) findViewById(R.id.editText9);
        edtComentarios = (EditText) findViewById(R.id.edtComentario);
        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton);
        Button btnAdd = (Button) findViewById(R.id.button7);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Requisicoes").child(edtNome.getText().toString());
                databaseReference.child("Nome").setValue(edtNome.getText().toString());
                databaseReference.child("Estado").setValue(edtEstado.getText().toString());
                databaseReference.child("Cidade").setValue(edtCidade.getText().toString());
                databaseReference.child("Bairro").setValue(edtBairro.getText().toString());
                databaseReference.child("NomePessoa").setValue(edtNomePessoa.getText().toString());
                databaseReference.child("Email").setValue(edtEmail.getText().toString());
                databaseReference.child("Telefone").setValue(edtCelular.getText().toString());
                databaseReference.child("Comentarios").setValue(edtComentarios.getText().toString());
                AlertDialog.Builder alerta = new AlertDialog.Builder(TelaComercio.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alerta.setTitle("Obrigado!");
                alerta.setMessage("Entraremos em contato em breve!");
                alerta.setIcon(R.mipmap.ic_info);
                alerta.setCancelable(true);
                alerta.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TelaComercio.super.onBackPressed();
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

    }
}
