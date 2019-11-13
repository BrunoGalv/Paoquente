package com.realvibedev.paoquente;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.Remote.APIService;
import com.realvibedev.paoquente.adapter.AdapterChat;
import com.realvibedev.paoquente.model.Data;
import com.realvibedev.paoquente.model.ListChat;
import com.realvibedev.paoquente.model.MyResponse;
import com.realvibedev.paoquente.model.Sender;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaChatCliente extends AppCompatActivity {

    private CircleImageView imgPerfil;
    private TextView txtNome;
    private ImageView imgBack, btnEnviar, btnInfo;
    private EditText edtMens;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private Intent intent;
    private RecyclerView recyclerView;
    private List<ListChat> listChats = new ArrayList<>();
    private AdapterChat adapter;
    private APIService mService;
    private String token, nomePadaria, nome, uid, imagem, key;
    private boolean aux;
    private int auxInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_chat_cliente);

        imgBack = (ImageView) findViewById(R.id.imageView157);
        btnEnviar = (ImageView) findViewById(R.id.imageView159);
        btnInfo = (ImageView) findViewById(R.id.imageView158);
        imgPerfil = (CircleImageView) findViewById(R.id.circleImageViewTelaChat);
        txtNome = (TextView) findViewById(R.id.textView175);
        edtMens = (EditText) findViewById(R.id.editTextTelaChat);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTelaChat);
        intent = getIntent();

        auxInfo = intent.getIntExtra("aux", 0);

        mService = Common.getFCMClient();

        user = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new AdapterChat(listChats, false);
        recyclerView.setAdapter(adapter);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auxInfo == 1){
                    TelaChatCliente.super.onBackPressed();
                }else{
                    Intent intent = new Intent(view.getContext(), TelaPadaria.class);
                    //intent.putExtra("Nome", padaria.getNomePadaria());
                    intent.putExtra("Key", key);
                    view.getContext().startActivity(intent);
                }
            }
        });


        if (user != null){
            if (intent.getStringExtra("Key")!=null) {

                key = intent.getStringExtra("Key");
                uid = key;
                updateList(db.collection("users").document(user.getUid()).collection("mensagens").document(key).collection("chat"));

                db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                nome = ((String) document.getData().get("nome"));
                            }
                        } else {
                            Toast.makeText(TelaChatCliente.this, getString(R.string.erro), Toast.LENGTH_LONG).show();
                        }
                    }
                });


                db.collection("listaInfo").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if ((document.getData().get("Nome"))!=null){
                                    nomePadaria = ((String) document.getData().get("Nome"));
                                    txtNome.setText(nomePadaria);
                                }
                                if (document.getData().get("imagem")!=null){
                                    imagem = (String) document.getData().get("imagem");
                                    Glide.with(getApplicationContext()).load(imagem).into(imgPerfil);
                                }
                                if (document.getData().get("Token")!=null){
                                    token = ((String) document.getData().get("Token"));
                                }
                                db.collection("users").document(user.getUid()).collection("mensagens").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            aux = document.exists();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet

                    if (user!=null && uid!=null && token!=null) {
                        if (!edtMens.getText().toString().equals("") && !edtMens.getText().toString().equals(" ")) {
                            final String texto = edtMens.getText().toString();
                            if (aux) {
                                final DocumentReference sfDocRefUser = db.collection("users").document(user.getUid()).collection("mensagens").document(key);
                                final DocumentReference sfDocRefUser2 = db.collection("users").document(user.getUid());
                                final DocumentReference sfDocRefUserPro = db.collection("usersPro").document(uid).collection("mensagens").document(user.getUid());
                                final DocumentReference sfDocRefUserPro2 = db.collection("usersPro").document(uid);


                                final Map<String, Object> newDataUserChat = new HashMap<>();
                                newDataUserChat.put("msg", texto);
                                newDataUserChat.put("aux", 1);


                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshot = transaction.get(sfDocRefUser);
                                        Double newNumMsg = snapshot.getDouble("numMsg") + 1;
                                        transaction.update(sfDocRefUser, "numMsg", newNumMsg);

                                        String string = String.valueOf(newNumMsg.intValue());

                                        switch (String.valueOf(newNumMsg.intValue()).length()) {
                                            case 1:
                                                string = "000000" + String.valueOf(newNumMsg.intValue());
                                                break;
                                            case 2:
                                                string = "00000" + String.valueOf(newNumMsg.intValue());
                                                break;
                                            case 3:
                                                string = "0000" + String.valueOf(newNumMsg.intValue());
                                                break;
                                            case 4:
                                                string = "000" + String.valueOf(newNumMsg.intValue());
                                                break;
                                            case 5:
                                                string = "00" + String.valueOf(newNumMsg.intValue());
                                                break;
                                            case 6:
                                                string = "0" + String.valueOf(newNumMsg.intValue());
                                                break;
                                        }

                                        final DocumentReference sfDocRefUserChat = db.collection("users").document(user.getUid()).collection("mensagens").document(key).collection("chat").document(string);


                                        transaction.set(sfDocRefUserChat, newDataUserChat);
                                        transaction.update(sfDocRefUser, "ultMsg", texto);
                                        transaction.update(sfDocRefUserPro, "ultMsg", texto);
                                        // Success
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        db.runTransaction(new Transaction.Function<Void>() {
                                            @Override
                                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                                DocumentSnapshot snapshot = transaction.get(sfDocRefUser);
                                                DocumentSnapshot snapshotUser2 = transaction.get(sfDocRefUser2);

                                                Double ordem = snapshot.getDouble("ordem");
                                                Double numOrdem = snapshotUser2.getDouble("numOrdem");

                                                if (!ordem.equals(numOrdem)) {
                                                    transaction.update(sfDocRefUser, "ordem", numOrdem + 1);
                                                    transaction.update(sfDocRefUser2, "numOrdem", numOrdem + 1);
                                                }
                                                // Success
                                                return null;
                                            }
                                        });
                                        db.runTransaction(new Transaction.Function<Void>() {
                                            @Override
                                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                                DocumentSnapshot snapshotPro = transaction.get(sfDocRefUserPro);
                                                DocumentSnapshot snapshotUserPro2 = transaction.get(sfDocRefUserPro2);

                                                Double ordem2 = snapshotPro.getDouble("ordem");
                                                Double numOrdem2 = snapshotUserPro2.getDouble("numOrdem");

                                                if (!ordem2.equals(numOrdem2)) {
                                                    transaction.update(sfDocRefUserPro, "ordem", numOrdem2 + 1);
                                                    transaction.update(sfDocRefUserPro2, "numOrdem", numOrdem2 + 1);
                                                }
                                                // Success
                                                return null;
                                            }
                                        });
                                        enviarNotificacao(texto);
                                    }
                                });

                                edtMens.setText("");
                            } else {

                                final DocumentReference sfDocRefUserPro = db.collection("usersPro").document(uid).collection("mensagens").document(user.getUid());
                                final DocumentReference sfDocRefUserPro2 = db.collection("usersPro").document(uid);
                                final DocumentReference sfDocRefUser = db.collection("users").document(user.getUid()).collection("mensagens").document(key);
                                final DocumentReference sfDocRefUserChat = db.collection("users").document(user.getUid()).collection("mensagens").document(key).collection("chat").document("0000001");
                                final DocumentReference sfDocRefUser2 = db.collection("users").document(user.getUid());

                                final Map<String, Object> newDataUser = new HashMap<>();
                                newDataUser.put("nome", nomePadaria);
                                newDataUser.put("numMsg", 1);
                                newDataUser.put("ultMsg", texto);
                                newDataUser.put("imagem", imagem);

                                final Map<String, Object> newDataUserChat = new HashMap<>();
                                newDataUserChat.put("msg", texto);
                                newDataUserChat.put("aux", 1);


                                final Map<String, Object> newDataUserPro = new HashMap<>();
                                newDataUserPro.put("nome", nome);
                                newDataUserPro.put("ultMsg", texto);

                                db.runTransaction(new Transaction.Function<Double>() {
                                    @Override
                                    public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshotUser2 = transaction.get(sfDocRefUser2);
                                        DocumentSnapshot snapshotUserPro2 = transaction.get(sfDocRefUserPro2);
                                        Double newNumOrdem = snapshotUser2.getDouble("numOrdem") + 1;
                                        Double newNumOrdem2 = snapshotUserPro2.getDouble("numOrdem") + 1;


                                        newDataUser.put("ordem", newNumOrdem);
                                        newDataUserPro.put("ordem", newNumOrdem2);


                                        transaction.update(sfDocRefUserPro2, "numOrdem", newNumOrdem2);
                                        transaction.update(sfDocRefUser2, "numOrdem", newNumOrdem);
                                        transaction.set(sfDocRefUser, newDataUser);
                                        transaction.set(sfDocRefUserChat, newDataUserChat);
                                        transaction.set(sfDocRefUserPro, newDataUserPro);
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Double>() {
                                    @Override
                                    public void onSuccess(Double result) {
                                        enviarNotificacao(texto);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TelaChatCliente.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                edtMens.setText("");
                                aux = true;
                            }
                        }
                    }else {
                        Toast.makeText(TelaChatCliente.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TelaChatCliente.this, getResources().getString(R.string.semConexao), Toast.LENGTH_LONG).show();
                }
            }
        });





        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaChatCliente.super.onBackPressed();
            }
        });

    }

    private void updateList(CollectionReference collectionReference){
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    Toast.makeText(TelaChatCliente.this, getString(R.string.erro)+ " Cod:" + e, Toast.LENGTH_LONG).show();
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListChat mensagem = dc.getDocument().toObject(ListChat.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            mensagem.setKey(dc.getDocument().getId());
                            listChats.add(mensagem);

                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(listChats.size() -1);
                            break;
                        case MODIFIED:
                            index = getItemIndex((dc.getDocument().getId()));
                            mensagem.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listChats.set(index, mensagem);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex((dc.getDocument().getId()));

                            if (index != -1) {
                                listChats.remove(index);
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

        for (int i = 0; i < listChats.size(); i++){
            if ((listChats.get(i).getKey()).equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void enviarNotificacao(String texto){
        if (nome !=null && token !=null) {
            //Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
            Data data = new Data(nome, texto, "", "Mensagens", "1", user.getUid());
            Sender sender = new Sender(token, data);
            mService.sendNotification(sender)
                    .enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                        }
                    });
        }
    }
}
