package com.realvibedev.paoquente.Padaria;

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

import com.realvibedev.paoquente.Common;
import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.Remote.APIService;
import com.realvibedev.paoquente.adapter.AdapterChat;
import com.realvibedev.paoquente.model.Data;
import com.realvibedev.paoquente.model.ListChat;
import com.realvibedev.paoquente.model.MyResponse;
import com.realvibedev.paoquente.model.Sender;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaChat extends AppCompatActivity {

    private TextView txtNome;
    private ImageView imgBack, btnEnviar;
    private EditText edtMens;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private Intent intent;
    private RecyclerView recyclerView;
    private List<ListChat> listChats = new ArrayList<>();
    private AdapterChat adapter;
    private APIService mService;
    private String token, nome, uidCliente, key, nomeCliente;
    private boolean aux;
    private int aux2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_chat);

        imgBack = (ImageView) findViewById(R.id.imageView114);
        btnEnviar = (ImageView) findViewById(R.id.imageView116);
        txtNome = (TextView) findViewById(R.id.textView138);
        edtMens = (EditText) findViewById(R.id.editTextMsgChat);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView4);
        intent = getIntent();

        mService = Common.getFCMClient();

        uidCliente = intent.getStringExtra("Key");
        aux2 = intent.getIntExtra("aux", 0);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (uidCliente!=null){
            if (user!=null) {
                DocumentReference docRef = db.collection("usersPro").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                key = user.getUid();
                                nome = (String) document.getData().get("nome");
                                updateList(db.collection("users").document(uidCliente).collection("mensagens").document(key).collection("chat"));
                                db.collection("users").document(uidCliente).collection("mensagens").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                aux = true;
                                            }else {
                                                aux = false;
                                            }
                                        }
                                    }
                                });
                                db.collection("users").document(uidCliente).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                nomeCliente = (String) (document.getData().get("nome"));
                                                txtNome.setText(nomeCliente);
                                                token = (String) (document.getData().get("Token"));
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(TelaChat.this, getString(R.string.erro) + " Cod: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterChat(listChats);
        recyclerView.setAdapter(adapter);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (!edtMens.getText().toString().equals("") && !edtMens.getText().toString().equals(" ")){
                        if (key!=null && uidCliente!=null) {
                            if (token != null) {
                                final String texto = edtMens.getText().toString();
                                if (aux) {
                                    final DocumentReference sfDocRefUser = db.collection("users").document(uidCliente).collection("mensagens").document(key);
                                    final DocumentReference sfDocRefUser2 = db.collection("users").document(uidCliente);
                                    final DocumentReference sfDocRefUserPro = db.collection("usersPro").document(user.getUid()).collection("mensagens").document(uidCliente);
                                    final DocumentReference sfDocRefUserPro2 = db.collection("usersPro").document(user.getUid());


                                    final Map<String, Object> newDataUserChat = new HashMap<>();
                                    newDataUserChat.put("msg", texto);
                                    newDataUserChat.put("aux", 0);


                                    db.runTransaction(new Transaction.Function<Void>() {
                                        @Override
                                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot snapshot = transaction.get(sfDocRefUser);
                                            Double newNumMsg = snapshot.getDouble("numMsg") + 1;
                                            transaction.update(sfDocRefUser, "numMsg", newNumMsg);

                                            String string = String.valueOf(newNumMsg.intValue());

                                            switch (String.valueOf(newNumMsg.intValue()).length()){
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

                                            final DocumentReference sfDocRefUserChat = db.collection("users").document(uidCliente).collection("mensagens").document(key).collection("chat").document(string);


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
                                                        transaction.update(sfDocRefUser, "ordem", numOrdem+1);
                                                        transaction.update(sfDocRefUser2, "numOrdem", numOrdem+1);
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
                                                        transaction.update(sfDocRefUserPro, "ordem", numOrdem2+1);
                                                        transaction.update(sfDocRefUserPro2, "numOrdem", numOrdem2+1);
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

                                    final DocumentReference sfDocRef = db.collection("listaInfo").document(key);
                                    final DocumentReference sfDocRefUserPro = db.collection("usersPro").document(user.getUid()).collection("mensagens").document(uidCliente);
                                    final DocumentReference sfDocRefUserPro2 = db.collection("usersPro").document(user.getUid());
                                    final DocumentReference sfDocRefUser = db.collection("users").document(uidCliente).collection("mensagens").document(key);
                                    final DocumentReference sfDocRefUserChat = db.collection("users").document(uidCliente).collection("mensagens").document(key).collection("chat").document("0000001");
                                    final DocumentReference sfDocRefUser2 = db.collection("users").document(uidCliente);

                                    final Map<String, Object> newDataUser = new HashMap<>();
                                    newDataUser.put("nome", nome);
                                    newDataUser.put("numMsg", 1);
                                    newDataUser.put("ultMsg", texto);

                                    final Map<String, Object> newDataUserChat = new HashMap<>();
                                    newDataUserChat.put("msg", texto);
                                    newDataUserChat.put("aux", 0);


                                    final Map<String, Object> newDataUserPro = new HashMap<>();
                                    newDataUserPro.put("nome", nomeCliente);
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
                                            Toast.makeText(TelaChat.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    edtMens.setText("");
                                    aux = true;
                                }

                            }
                        }else {
                            Toast.makeText(TelaChat.this, getResources().getString(R.string.erro), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(TelaChat.this, getResources().getString(R.string.semConexao), Toast.LENGTH_SHORT).show();
                }
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aux2 == 0) {
                    TelaChat.super.onBackPressed();
                }else {
                    startActivity(new Intent(TelaChat.this, PainelAdministrativo.class));
                    finish();
                }
            }
        });

    }

    private void updateList(CollectionReference collectionReference){
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    Toast.makeText(TelaChat.this, getString(R.string.erro)+ " Cod:" + e, Toast.LENGTH_LONG).show();
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
                            index = getItemIndex(Integer.parseInt(dc.getDocument().getId()));
                            mensagem.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listChats.set(index, mensagem);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex(Integer.parseInt(dc.getDocument().getId()));

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

    private int getItemIndex(int Key){

        int index = -1;

        for (int i = 0; i < listChats.size(); i++){
            if (Integer.parseInt(listChats.get(i).getKey()) == (Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void enviarNotificacao(String texto){
        Data data = new Data(nome, texto, "", "Mensagens", "2", user.getUid());
        Sender sender = new Sender(token, data);
        mService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        //Toast.makeText(TelaChat.this, response.message() + "A"+ call.isExecuted() + token, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        //Toast.makeText(TelaChat.this, t.getMessage() + "B" + call.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (aux2 == 0) {
            super.onBackPressed();
        }else {
            startActivity(new Intent(TelaChat.this, PainelAdministrativo.class));
            finish();
        }
    }
}
