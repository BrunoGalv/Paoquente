package com.realvibedev.paoquente;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.adapter.AdapterMensagensCliente;
import com.realvibedev.paoquente.model.ListMensagensCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TelaMensagens extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ListMensagensCliente> listMensagens = new ArrayList<>();
    private AdapterMensagensCliente adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private ImageView imgBack;
    private Intent intent;
    private ProgressBar progressBar;
    private TextView txtNenhum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mensagens);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMensagens);
        imgBack = (ImageView) findViewById(R.id.imageView160);
        progressBar = (ProgressBar) findViewById(R.id.progressBar18);
        txtNenhum = (TextView) findViewById(R.id.textView177);

        intent = getIntent();

        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterMensagensCliente(listMensagens);
        recyclerView.setAdapter(adapter);

        if (user != null){
            db.collection("users").document(user.getUid()).collection("mensagens").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0){
                            txtNenhum.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
            updateList();
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaMensagens.super.onBackPressed();
            }
        });
    }

    private void updateList(){
        db.collection("users").document(user.getUid()).collection("mensagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    Toast.makeText(TelaMensagens.this, getString(R.string.erro)+ " Cod:" + e, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListMensagensCliente mensagem = dc.getDocument().toObject(ListMensagensCliente.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            mensagem.setKey(dc.getDocument().getId());
                            listMensagens.add(mensagem);

                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            index = getItemIndex(dc.getDocument().getId());
                            mensagem.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listMensagens.set(index, mensagem);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex(dc.getDocument().getId());

                            if (index != -1) {
                                listMensagens.remove(index);
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

        for (int i = 0; i < listMensagens.size(); i++){
            if (listMensagens.get(i).getKey().equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }
}
