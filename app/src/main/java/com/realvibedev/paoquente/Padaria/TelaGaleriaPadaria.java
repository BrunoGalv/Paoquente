package com.realvibedev.paoquente.Padaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterGaleria;
import com.realvibedev.paoquente.model.ListGaleria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaGaleriaPadaria extends AppCompatActivity {

    private ImageView imgBack, imgFoto;
    private Button btnAdd;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private List<ListGaleria> listGaleria = new ArrayList<>();
    private AdapterGaleria adapter;
    private Intent intent;
    private FirebaseUser user;
    private String key;
    private ProgressBar progressBar;
    private String imagemPro;
    private StorageReference storageReference;
    private TextView txtSemData;
    private int aux = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_galeria_padaria);

        imgBack = (ImageView) findViewById(R.id.imageView120);
        btnAdd = (Button) findViewById(R.id.button12);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        txtSemData = (TextView) findViewById(R.id.textView142);

        intent = getIntent();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView6);

        if (user!=null) {
            DocumentReference docRef = db.collection("usersPro").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            key = user.getUid();
                            updateList();
                            checarData();
                        }
                    } else {
                        Toast.makeText(TelaGaleriaPadaria.this, getString(R.string.erro) + " Cod: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TelaGaleriaPadaria.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterGaleria(listGaleria);
        recyclerView.setAdapter(adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key!=null){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaGaleriaPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
                    LayoutInflater inflater = getLayoutInflater();
                    final View viewAlerta = inflater.inflate(R.layout.dialog_add_foto, null);
                    alerta.setView(viewAlerta);
                    progressBar = (ProgressBar) viewAlerta.findViewById(R.id.progressBar13);
                    imgFoto = (ImageView) viewAlerta.findViewById(R.id.imageView121);
                    imgFoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, 2);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                    alerta.setMessage("Selecione a foto desejada");
                    alerta.setCancelable(false);
                    alerta.setPositiveButton(getResources().getString(R.string.add), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (imagemPro!=null) {

                                final Map<String, Object> newData = new HashMap<>();
                                newData.put("imagem", imagemPro);
                                db.collection("listaInfo").document(key).collection("Galeria").add(newData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(TelaGaleriaPadaria.this, "Imagem adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                                        if (aux == 0){
                                            txtSemData.setVisibility(View.INVISIBLE);
                                            aux = 1;
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(TelaGaleriaPadaria.this, "Selecione a imagem", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (imagemPro!=null) {
                                StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagemPro);
                                ref2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                            }
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaGaleriaPadaria.super.onBackPressed();
            }
        });
    }

    private void updateList(){
        db.collection("listaInfo").document(key).collection("Galeria").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListGaleria imagem = dc.getDocument().toObject(ListGaleria.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            imagem.setKey(dc.getDocument().getId());
                            listGaleria.add(imagem);

                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            index = getItemIndex((dc.getDocument().getId()));
                            imagem.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listGaleria.set(index, imagem);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex((dc.getDocument().getId()));

                            if (index != -1) {
                                listGaleria.remove(index);
                                adapter.notifyItemRemoved(index);
                            }
                            break;
                    }
                }
            }
        });
    }

    private int getItemIndex(String Key){

        int index = -1;

        for (int i = 0; i < listGaleria.size(); i++){
            if ((listGaleria.get(i).getKey()).equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (user != null) {
                Uri uri = data.getData();
                StorageReference filepath = storageReference.child("ListaComercios").child(String.valueOf(key)).child("Galeria").child(uri.getLastPathSegment());

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
                                    imagemPro = uri.toString();
                                    Picasso.get().load(imagemPro).into(imgFoto);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TelaGaleriaPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TelaGaleriaPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }else {
            Toast.makeText(TelaGaleriaPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void apagarFt(final String imagem, final String keyImagem){
        if (imagem!=null) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaGaleriaPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
            alerta.setMessage("Deseja excluir a imagem?");
            alerta.setCancelable(true);
            alerta.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    db.collection("listaInfo").document(key).collection("Galeria").document(keyImagem).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TelaGaleriaPadaria.this, "Imagem excluída com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagem);
                    ref2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
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
    }

    private void checarData(){
        db.collection("listaInfo").document(key).collection("Galeria").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0){
                        txtSemData.setVisibility(View.VISIBLE);
                        aux = 0;
                    }
                }
            }
        });
    }

    public void imagem(String url){
        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaGaleriaPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.dialog_imagem, null);
        alerta.setView(viewAlerta);
        TouchImageView img = (TouchImageView) viewAlerta.findViewById(R.id.touchImageView3);
        Picasso.get().load(url).into(img);
        alerta.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alerta.setCancelable(true);
        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
    }
}
