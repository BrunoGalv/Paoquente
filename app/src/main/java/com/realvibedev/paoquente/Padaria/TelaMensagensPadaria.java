package com.realvibedev.paoquente.Padaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterMensagens;
import com.realvibedev.paoquente.model.ListMensagens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class TelaMensagensPadaria extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ListMensagens> listMensagens = new ArrayList<>();
    private AdapterMensagens adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private ImageView imgBack;
    private ProgressBar progressBar;
    private TextView txtNenhum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mensagens_padaria);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        imgBack = (ImageView) findViewById(R.id.imageView109);
        progressBar = (ProgressBar) findViewById(R.id.progressBar12);
        txtNenhum = (TextView) findViewById(R.id.textView135);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            db.collection("usersPro").document(user.getUid()).collection("mensagens").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new AdapterMensagens(listMensagens);
        recyclerView.setAdapter(adapter);



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaMensagensPadaria.super.onBackPressed();
            }
        });

    }

    private void updateList(){
        db.collection("usersPro").document(user.getUid()).collection("mensagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    Toast.makeText(TelaMensagensPadaria.this, getString(R.string.erro)+ " Cod:" + e, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListMensagens mensagem = dc.getDocument().toObject(ListMensagens.class);
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

    public void apagarMensag(final String keyMensagem){
        if (user!=null){
            CharSequence options[] = new CharSequence[] {"Excluir"};
            AlertDialog.Builder alertaOptions = new AlertDialog.Builder(new ContextThemeWrapper(TelaMensagensPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
            alertaOptions.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:
                            AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaMensagensPadaria.this, android.R.style.Theme_Holo_Light_Dialog));
                            alerta.setMessage("Tem certeza que deseja apagar a mensagem?");
                            alerta.setIcon(R.mipmap.warning);
                            alerta.setCancelable(true);
                            alerta.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("usersPro").document(user.getUid()).collection("mensagens").document(keyMensagem).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(TelaMensagensPadaria.this, "Mensagem excluída com sucesso!", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TelaMensagensPadaria.this, getString(R.string.erro)+ " Cod:" + e, Toast.LENGTH_LONG).show();
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

    @Override
    public void onStart() {
        if (user!=null) {
            listMensagens.clear();
            updateList();
            super.onStart();
        }
    }
}
