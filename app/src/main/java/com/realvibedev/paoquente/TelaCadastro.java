package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TelaCadastro extends AppCompatActivity {

    private ImageView imgVoltar;
    private Button btnAvancar, btnEnviarCodigo;
    private EditText edtCodigo;
    private IntlPhoneInput edtNumCel;
    private TextInputLayout edtLayoutCodigo;
    private TextView txtTermosUso;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String verificationCode;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);



        imgVoltar = (ImageView) findViewById(R.id.imageView128);
        btnAvancar = (Button) findViewById(R.id.button20);
        btnEnviarCodigo = (Button) findViewById(R.id.button34);
        edtNumCel = (IntlPhoneInput) findViewById(R.id.my_phone_input);
        edtCodigo = (EditText) findViewById(R.id.etTelefoneCadastrarCelVerificar);
        edtLayoutCodigo = (TextInputLayout) findViewById(R.id.etTelefoneLayoutCadastrarCelVerificar);
        txtTermosUso = (TextView) findViewById(R.id.textView153);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {//if actual version is >= 6.0
            txtTermosUso.setText(R.string.concordoTermosEPoliticaBlack);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber;
                if (edtNumCel.isValid()){
                    phoneNumber = edtNumCel.getNumber();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, TelaCadastro.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            edtNumCel.hideKeyboard();
                            Toast.makeText(TelaCadastro.this, getString(R.string.verificacaoFalhou), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            verificationCode = s;
                            edtNumCel.hideKeyboard();
                            edtLayoutCodigo.setVisibility(View.VISIBLE);
                            btnEnviarCodigo.setVisibility(View.VISIBLE);
                            txtTermosUso.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                            super.onCodeAutoRetrievalTimeOut(s);
                            Toast.makeText(TelaCadastro.this, getString(R.string.verificacaoExcedeu), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(TelaCadastro.this, getString(R.string.numeroCelErrado), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnEnviarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtCodigo.getText().toString();
                if(!code.isEmpty()){
                    signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationCode, code));
                }
            }
        });

        txtTermosUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaCadastro.this, TelaTermosdeUso.class));
            }
        });



        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaCadastro.super.onBackPressed();
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult().getString("nome") == null){
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaCadastro.this, android.R.style.Theme_Holo_Light_Dialog));
                                        LayoutInflater inflater = getLayoutInflater();
                                        final View viewAlerta = inflater.inflate(R.layout.dialog_cadastro, null);
                                        final EditText editText = (EditText) viewAlerta.findViewById(R.id.editText38);
                                        alerta.setView(viewAlerta);
                                        alerta.setCancelable(false);
                                        alerta.setNeutralButton("Concluir Cadastro", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                final Map<String, Object> newUser = new HashMap<>();
                                                newUser.put("numOrdem", 0);
                                                newUser.put("nome", editText.getText().toString());

                                                db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(TelaCadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(TelaCadastro.this, TelaPrincipal.class));
                                                        finishAffinity();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(TelaCadastro.this, "Erro, tente novamente", Toast.LENGTH_SHORT).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        startActivity(new Intent(TelaCadastro.this, TelaCadastro.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                        AlertDialog alertDialog = alerta.create();
                                        alertDialog.show();
                                    }else{
                                        startActivity(new Intent(TelaCadastro.this, TelaPrincipal.class));
                                        finishAffinity();
                                    }
                                }
                            });
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(TelaCadastro.this, getString(R.string.codigoVerificacaoErrado), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(TelaCadastro.this, getString(R.string.codigoVerificacaoDeuRuim), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
