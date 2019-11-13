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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterPromos;
import com.realvibedev.paoquente.model.ListPromos;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaPromosPadaria extends AppCompatActivity {

    private ImageView imgBack;
    private Button btnAddPromo;
    private RecyclerView recyclerView;
    private List<ListPromos> listaPromos = new ArrayList<>();
    private AdapterPromos adapter;
    private ProgressBar progressBar, progressBar2;
    private TextView txtNenhum;
    private String imagem, key, nomePadaria;
    private ImageView imgNotificacao;
    private FirebaseUser user;
    private StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_promos_padaria);

        imgBack = findViewById(R.id.imageView65);
        btnAddPromo = findViewById(R.id.button10);
        recyclerView = findViewById(R.id.recyclerView12);
        progressBar2 = findViewById(R.id.progressBar9);
        txtNenhum = findViewById(R.id.textView133);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterPromos(listaPromos);
        recyclerView.setAdapter(adapter);

        db.collection("usersPro").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    key = user.getUid();
                    lat = (Double) document.get("lat");
                    longi = (Double) document.get("longi");
                    nomePadaria = document.getString("Nome");
                    updateList();
                }
            }
        });

        btnAddPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoosePromos();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaPromosPadaria.super.onBackPressed();
            }
        });
    }

    private void updateList(){
        db.collection("promos").whereEqualTo("keyPd", key).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    progressBar2.setVisibility(View.INVISIBLE);
                    return;
                }else {
                    progressBar2.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListPromos promo = dc.getDocument().toObject(ListPromos.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            promo.setKey(dc.getDocument().getId());
                            listaPromos.add(promo);
                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            index = getItemIndex((dc.getDocument().getId()));
                            promo.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listaPromos.set(index, promo);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex((dc.getDocument().getId()));

                            if (index != -1) {
                                listaPromos.remove(index);
                                adapter.notifyItemRemoved(index);
                            }
                            break;
                    }
                }
                if (listaPromos.size()==0){
                    txtNenhum.setVisibility(View.VISIBLE);
                }else {
                    txtNenhum.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private int getItemIndex(String Key){

        int index = -1;

        for (int i = 0; i < listaPromos.size(); i++){
            if (listaPromos.get(i).getKey().equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (user != null && data!=null) {
                final Uri uri = data.getData();
                StorageReference filepath = storageReference.child("ImagemPromo").child(uri.getLastPathSegment());

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
                                    Glide.with(getApplicationContext()).load(imagem).apply(new RequestOptions()
                                            .centerCrop()
                                    ).into(imgNotificacao);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TelaPromosPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TelaPromosPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }else {
                    Toast.makeText(TelaPromosPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }else {
            Toast.makeText(TelaPromosPadaria.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void dialogChoosePromos(){
        CharSequence options[] = new CharSequence[] {"Promoção Normal", "Promoção Especial", "Qual é a diferença entre as Promoções?"};
        AlertDialog.Builder alertaOptions = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromosPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
        alertaOptions.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        callDialog(null, null, null, null, null);
                        break;
                    case 1:
                        callDialogEspecial(null, null, null, null, null, null);
                        break;
                    case 2:
                        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromosPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
                        LayoutInflater inflater = getLayoutInflater();
                        final View viewAlerta = inflater.inflate(R.layout.dialog_expl_promocao, null);
                        alerta.setView(viewAlerta);
                        alerta.setCancelable(true);
                        alerta.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alertDialog = alerta.create();
                        alertDialog.show();
                        break;
                }
            }
        });
        alertaOptions.show();
    }

    public void callDialog(String titulo, String desc, String preco, String imagem2, final String aux){
        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromosPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.dialog_promo, null);
        final EditText editTitulo = viewAlerta.findViewById(R.id.etTitulo);
        final EditText editDesc = viewAlerta.findViewById(R.id.etDescricao);
        final EditText editPreco = viewAlerta.findViewById(R.id.etPreco);
        final RadioButton radioButtonUN = viewAlerta.findViewById(R.id.radioButton4);
        final RadioButton radioButtonKG = viewAlerta.findViewById(R.id.radioButton5);
        final RadioButton radioButtonGR = viewAlerta.findViewById(R.id.radioButton6);
        radioButtonUN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonKG.setChecked(false);
                radioButtonGR.setChecked(false);
            }
        });
        radioButtonKG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonUN.setChecked(false);
                radioButtonGR.setChecked(false);
            }
        });
        radioButtonGR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonKG.setChecked(false);
                radioButtonUN.setChecked(false);
            }
        });
        if (titulo!=null){
            editTitulo.setText(titulo);
        }
        if (desc!=null){
            editDesc.setText(desc);
        }
        editPreco.setText("R$ ");
        if (preco!=null){
            editPreco.setText(preco);
        }
        editPreco.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignored
            }

            // This is the callback we're interested in. Any time
            // your user edits what is in the EditText, this method
            // is called. We'll use it listen for when a user tries
            // to delete the default display text.
            @Override
            public void afterTextChanged(Editable s) {
                // if the user attempts to delete and of the default
                // string displayed, just put it back
                if (s.toString().contains(".")){
                    editPreco.setText(s.toString().replace(".", ","));
                    editPreco.setSelection(s.length());
                }
                if (s.length() < "R$ ".length()) {
                    editPreco.setText("R$ ");
                    editPreco.setSelection(3);
                }
            }
        });
        final ImageView imgAddFoto = viewAlerta.findViewById(R.id.imageView112);
        progressBar = (ProgressBar) viewAlerta.findViewById(R.id.progressBar10);
        imgNotificacao = (ImageView) viewAlerta.findViewById(R.id.imageView111);
        if (imagem2!=null){
            Glide.with(getApplicationContext()).load(imagem2).apply(new RequestOptions()
                    .centerCrop()
            ).into(imgNotificacao);
            imgAddFoto.setVisibility(View.INVISIBLE);
            imagem = imagem2;
        }
        imgNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                imgAddFoto.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        alerta.setIcon(R.mipmap.warning);
        alerta.setCancelable(true);
        String positveButton = "Criar";
        if (aux != null){
            positveButton = "Salvar";
        }
        alerta.setPositiveButton(positveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> promo = new HashMap<>();
                promo.put("nome", editTitulo.getText().toString());
                promo.put("descricao", editDesc.getText().toString());
                if (radioButtonUN.isChecked()){
                    promo.put("preco", editPreco.getText().toString() + " UN");
                }else if (radioButtonKG.isChecked()){
                    promo.put("preco", editPreco.getText().toString() + " KG");
                }else if (radioButtonGR.isChecked()){
                    promo.put("preco", editPreco.getText().toString() + " GR");
                }else {
                    promo.put("preco", editPreco.getText().toString());
                }
                promo.put("imagem", imagem);
                promo.put("keyPd", key);
                promo.put("lat", lat);
                promo.put("longi", longi);
                promo.put("nomePadaria", nomePadaria);

                if (aux == null) {
                    db.collection("promos")
                            .add(promo)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(TelaPromosPadaria.this, "Promoção criada com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    db.collection("promos").document(aux).update(promo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TelaPromosPadaria.this, "Promoção alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TelaPromosPadaria.this, "Erro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (imagem != null) {
                    StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagem);
                    ref2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
                }
            }
        });
        alerta.setView(viewAlerta);
        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
    }

    public void callDialogEspecial(String titulo, String subTitulo, String desc, String preco, String imagem2, final String aux){
        final AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromosPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.dialog_add_promo_especial, null);
        final EditText editTitulo = viewAlerta.findViewById(R.id.etTituloAddPromoEspecial);
        final EditText editSubTitulo = viewAlerta.findViewById(R.id.etSubTituloAddPromoEspecial);
        final EditText editDesc = viewAlerta.findViewById(R.id.etDescricaoAddPromoEspecial);
        final EditText editPreco = viewAlerta.findViewById(R.id.etPrecoAddPromoEspecial);
        final RadioButton radioButtonUN = viewAlerta.findViewById(R.id.radioButton7);
        final RadioButton radioButtonKG = viewAlerta.findViewById(R.id.radioButton8);
        final RadioButton radioButtonGR = viewAlerta.findViewById(R.id.radioButton9);
        radioButtonUN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonKG.setChecked(false);
                radioButtonGR.setChecked(false);
            }
        });
        radioButtonKG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonUN.setChecked(false);
                radioButtonGR.setChecked(false);
            }
        });
        radioButtonGR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonKG.setChecked(false);
                radioButtonUN.setChecked(false);
            }
        });
        if (titulo!=null){
            editTitulo.setText(titulo);
        }
        if (subTitulo!=null){
            editSubTitulo.setText(subTitulo);
        }
        if (desc!=null){
            editDesc.setText(desc);
        }
        editPreco.setText("R$ ");
        if (preco!=null){
            editPreco.setText(preco);
        }
        editPreco.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignored
            }

            // This is the callback we're interested in. Any time
            // your user edits what is in the EditText, this method
            // is called. We'll use it listen for when a user tries
            // to delete the default display text.
            @Override
            public void afterTextChanged(Editable s) {
                // if the user attempts to delete and of the default
                // string displayed, just put it back
                if (s.toString().contains(".")){
                    editPreco.setText(s.toString().replace(".", ","));
                    editPreco.setSelection(s.length());
                }
                if (s.length() < "R$ ".length()) {
                    editPreco.setText("R$ ");
                    editPreco.setSelection(3);
                }
            }
        });
        final ImageView imgAddFoto = viewAlerta.findViewById(R.id.imageView156);
        progressBar = (ProgressBar) viewAlerta.findViewById(R.id.progressBar17);
        imgNotificacao = (ImageView) viewAlerta.findViewById(R.id.imageView155);
        if (imagem2!=null){
            Glide.with(getApplicationContext()).load(imagem2).apply(new RequestOptions()
                    .centerCrop()
            ).into(imgNotificacao);
            imgAddFoto.setVisibility(View.INVISIBLE);
            imagem = imagem2;
        }
        imgNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                imgAddFoto.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        alerta.setIcon(R.mipmap.warning);
        alerta.setCancelable(true);
        String positveButton = "Criar";
        if (aux != null){
            positveButton = "Salvar";
        }
        alerta.setPositiveButton(positveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> promo = new HashMap<>();
                promo.put("txtPromo", editTitulo.getText().toString());
                promo.put("txtPromo2", editSubTitulo.getText().toString());
                promo.put("descricao", editDesc.getText().toString());
                if (radioButtonUN.isChecked()){
                    promo.put("preco", editPreco.getText().toString() + " UN");
                }else if (radioButtonKG.isChecked()){
                    promo.put("preco", editPreco.getText().toString() + " KG");
                }else if (radioButtonGR.isChecked()){
                    promo.put("preco", editPreco.getText().toString() + " GR");
                }else {
                    promo.put("preco", editPreco.getText().toString());
                }
                promo.put("imagem", imagem);
                promo.put("keyPd", key);
                promo.put("lat", lat);
                promo.put("longi", longi);
                promo.put("nomePadaria", nomePadaria);

                if (aux == null) {
                    db.collection("promos")
                            .add(promo)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(TelaPromosPadaria.this, "Promoção criada com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    db.collection("promos").document(aux).update(promo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TelaPromosPadaria.this, "Promoção alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TelaPromosPadaria.this, "Erro, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (imagem != null) {
                    StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagem);
                    ref2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
                }
            }
        });
        alerta.setView(viewAlerta);
        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
    }

    public void deletePromo(final String key){
        CharSequence options[] = new CharSequence[] {"Excluir"};
        AlertDialog.Builder alertaOptions = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromosPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
        alertaOptions.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaPromosPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
                        alerta.setMessage("Tem certeza que deseja apagar a promoção?");
                        alerta.setIcon(R.mipmap.warning);
                        alerta.setCancelable(true);
                        alerta.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("promos").document(key).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(TelaPromosPadaria.this, "Promoção excluída com sucesso!", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TelaPromosPadaria.this, getString(R.string.erro)+ " Cod:" + e, Toast.LENGTH_LONG).show();
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
                        break;
                }
            }
        });
        alertaOptions.show();
    }
}
