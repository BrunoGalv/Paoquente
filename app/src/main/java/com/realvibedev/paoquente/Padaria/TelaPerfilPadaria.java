package com.realvibedev.paoquente.Padaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TelaPerfilPadaria extends AppCompatActivity {

    private ImageView imgBack, imgEdtPerfil, imgPerfil;
    private StorageReference storageReference;
    private String imagem, city;
    private FirebaseUser user;
    private TextView txtSalvar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText editTextNome;
    private Button btnEdtInfo, btnAttEmail, btnAttSenha, btnGaleria;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil_padaria);

        imgBack = (ImageView) findViewById(R.id.imageView31);
        imgEdtPerfil = (ImageView) findViewById(R.id.imageView33);
        imgPerfil = (ImageView) findViewById(R.id.imageView32);
        txtSalvar= (TextView) findViewById(R.id.textView41);
        editTextNome= (EditText) findViewById(R.id.etNomeEditarPerfil);
        btnEdtInfo= (Button) findViewById(R.id.button28);
        btnAttEmail= (Button) findViewById(R.id.button14);
        btnAttSenha= (Button) findViewById(R.id.button13);
        btnGaleria= (Button) findViewById(R.id.button11);
        progressBar = (ProgressBar) findViewById(R.id.progressBar5);
        storageReference = FirebaseStorage.getInstance().getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            db.collection("usersPro").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            editTextNome.setText((String) document.getData().get("Nome"));
                            imagem = (String) document.getData().get("Imagem");
                            city = getCity(document.getDouble("lat"), document.getDouble("longi"));
                            if (imagem != null){
                                Picasso.get().load(imagem).into(imgPerfil);
                            }
                        }
                    } else {
                        Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro) + " Cod: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        imgEdtPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), android.R.style.Theme_Holo_Light_Dialog));
                alerta.setMessage("Deseja mudar o logo?");
                alerta.setCancelable(true);
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
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

        txtSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextNome.getText().toString().equals("")){
                    if (user != null && city != null) {
                        db.collection("usersPro").document(user.getUid()).update("Nome", editTextNome.getText().toString());
                        db.collection("listaInfo").document(user.getUid()).update("Nome", editTextNome.getText().toString());
                        db.collection("listaPadarias").document(city).collection(city).document(user.getUid()).update("nomePadaria", editTextNome.getText().toString());
                        if (imagem!=null){
                            db.collection("usersPro").document(user.getUid()).update("Imagem", imagem);
                            db.collection("listaInfo").document(user.getUid()).update("imagem", imagem);
                            db.collection("listaPadarias").document(city).collection(city).document(user.getUid()).update("imagemPadaria", imagem);
                        }
                        Toast.makeText(TelaPerfilPadaria.this, "Salvo!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(TelaPerfilPadaria.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEdtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaPerfilPadaria.this, TelaEditInfo.class));
            }
        });

        btnAttSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_att_senha, null);
                final EditText editText = viewAlerta.findViewById(R.id.etSenhaAttSenha);
                final EditText editTextSenhaAtual = viewAlerta.findViewById(R.id.etSenhaAntigaAttSenha);
                alerta.setIcon(R.mipmap.warning);
                alerta.setCancelable(true);
                alerta.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editText.getText().toString().equals("")&&!editTextSenhaAtual.getText().toString().equals("")) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user!=null) {
                                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), editTextSenhaAtual.getText().toString());
                                user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        user.updatePassword(editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(TelaPerfilPadaria.this, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro) + " COD: " + e, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro) + " COD: " + e, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else {
                                Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro), Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(TelaPerfilPadaria.this, "Preencha a senha", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alerta.setView(viewAlerta);
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        btnAttEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_att_senha, null);
                final EditText editText = viewAlerta.findViewById(R.id.etemailAttEmail);
                final TextInputLayout editText2 = viewAlerta.findViewById(R.id.etSenhaLayoutAttSenha);
                final TextInputLayout editText3 = viewAlerta.findViewById(R.id.etEmaiLayoutAttEmail);
                final EditText editTextSenhaAtual = viewAlerta.findViewById(R.id.etSenhaAntigaAttSenha);
                editText3.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.INVISIBLE);
                TextView txt = viewAlerta.findViewById(R.id.textView182);
                txt.setText("Atualizar email");
                alerta.setIcon(R.mipmap.warning);
                alerta.setCancelable(true);
                alerta.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editText.getText().toString().equals("")) {
                            if (!editTextSenhaAtual.getText().toString().equals("")) {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user!=null) {
                                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), editTextSenhaAtual.getText().toString());
                                    user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            user.updateEmail(editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(TelaPerfilPadaria.this, "Email atualizado com sucesso!", Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro) + " COD: " + e, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro) + " COD: " + e, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(TelaPerfilPadaria.this, getString(R.string.erro), Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(TelaPerfilPadaria.this, "Preencha a senha", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(TelaPerfilPadaria.this, "Preencha o email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alerta.setView(viewAlerta);
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaPerfilPadaria.this, TelaGaleriaPadaria.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaPerfilPadaria.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null) {
                if (imagem!=null){
                    if (!imagem.equals("https://firebasestorage.googleapis.com/v0/b/paoquente-bce48.appspot.com/o/logoPaoQuente3.png?alt=media&token=5bef782c-3ee1-45e7-8138-f1b8497f29dd")) {
                        StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagem);
                        ref2.delete();
                    }
                }

                Uri uri = data.getData();
                StorageReference filepath = storageReference.child("Padaria").child(user.getUid()).child(uri.getLastPathSegment());

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bmp!=null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] dataByte = baos.toByteArray();

                    filepath.putBytes(dataByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imagem = uri.toString();
                                    Picasso.get().load(imagem).into(imgPerfil);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TelaPerfilPadaria.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(TelaPerfilPadaria.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public String getCity(Double lat, Double longi){

        String city = null;

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            city = addresses.get(0).getAdminArea();
        }

        return city;
    }
}
