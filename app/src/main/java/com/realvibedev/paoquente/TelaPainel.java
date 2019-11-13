package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.Padaria.PainelAdministrativo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TelaPainel extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView, imgBack;
    private ProgressBar progressBar;
    private Button button;
    private TextView txtEsqueceuSenha, txtQueroCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_painel);

        edtEmail = (EditText) findViewById(R.id.etemail);
        edtSenha = (EditText) findViewById(R.id.etPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        imageView = (ImageView) findViewById(R.id.imageView12);
        imgBack = (ImageView) findViewById(R.id.imageView101);
        button = (Button) findViewById(R.id.button6);
        txtEsqueceuSenha = (TextView) findViewById(R.id.textView5);
        txtQueroCadastrar = (TextView) findViewById(R.id.textView67);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF661612"),
                android.graphics.PorterDuff.Mode.MULTIPLY);


        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), android.R.style.Theme_Holo_Light_Dialog));
                alerta.setMessage("Tem certeza que deseja redefinir a senha?");
                alerta.setCancelable(true);
                alerta.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.sendPasswordResetEmail(edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(TelaPainel.this, "Email de redefinição de senha enviado!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(TelaPainel.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        txtQueroCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaPainel.this, TelaCadastroEstabelecimento.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaPainel.super.onBackPressed();
            }
        });
    }

    public void login(View view){
        if (!edtEmail.getText().toString().equals("")) {
            if (!edtSenha.getText().toString().equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                button.setClickable(false);
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    firebaseAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(TelaPainel.this, "Email ou senha inválidos!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                imageView.setVisibility(View.INVISIBLE);
                                button.setClickable(true);
                            } else {
                                startActivity(new Intent(TelaPainel.this, PainelAdministrativo.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                            button.setClickable(true);
                        }
                    });
                }
                else {
                    Toast.makeText(TelaPainel.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    button.setClickable(true);
                }
            }else {
                Toast.makeText(TelaPainel.this, "Preencha o campo de senha!", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(TelaPainel.this, "Preencha o campo de email!", Toast.LENGTH_LONG).show();
        }
    }
}
