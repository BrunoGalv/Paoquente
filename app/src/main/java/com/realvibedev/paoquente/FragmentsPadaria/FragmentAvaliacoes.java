package com.realvibedev.paoquente.FragmentsPadaria;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterAvaliacoes;
import com.realvibedev.paoquente.model.ListAvaliacoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAvaliacoes extends Fragment {

    private TextView txtNota, txtNumAvaliacoes, txtSemData;
    private ImageView imgAvaliacao;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Intent intent;
    private String key;
    private RecyclerView recyclerView;
    private List<ListAvaliacoes> listAvaliacoes = new ArrayList<>();
    private AdapterAvaliacoes adapter;
    private ProgressBar progressBar;

    public FragmentAvaliacoes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_avaliacoes, container, false);

        txtNota = (TextView) rootView.findViewById(R.id.textView63);
        txtNumAvaliacoes = (TextView) rootView.findViewById(R.id.textView64);
        imgAvaliacao = (ImageView) rootView.findViewById(R.id.imageView97);
        txtSemData = (TextView) rootView.findViewById(R.id.textView66);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewAvaliacoes);

        intent = getActivity().getIntent();
        key = intent.getStringExtra("Key");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterAvaliacoes(listAvaliacoes);
        recyclerView.setAdapter(adapter);

        db.collection("listaInfo").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (isAdded()) {
                            Double num = document.getDouble("avaliacao");
                            txtNumAvaliacoes.setText(String.valueOf(document.getDouble("review").intValue()) + " avaliações");
                            if (num != 0 && num != 1 && num != 2 && num != 3 && num != 4 && num != 5) {
                                DecimalFormat df = new DecimalFormat("#.##");
                                txtNota.setText(String.valueOf(df.format(num)));
                            } else {
                                txtNota.setText(String.valueOf(num).replace(".", ","));
                            }
                            if (num == 0.0 && num < 0.4) {
                                imgAvaliacao.setImageResource(R.drawable.starzero);
                            } else if (num >= 0.4 && num < 0.9) {
                                imgAvaliacao.setImageResource(R.drawable.starzeromeio);
                            } else if (num >= 0.9 && num < 1.4) {
                                imgAvaliacao.setImageResource(R.drawable.star);
                            } else if (num >= 1.4 && num < 1.9) {
                                imgAvaliacao.setImageResource(R.drawable.starmeio);
                            } else if (num >= 1.9 && num < 2.4) {
                                imgAvaliacao.setImageResource(R.drawable.starr);
                            } else if (num >= 2.4 && num < 2.9) {
                                imgAvaliacao.setImageResource(R.drawable.starrmeio);
                            } else if (num >= 2.9 && num < 3.4) {
                                imgAvaliacao.setImageResource(R.drawable.starrr);
                            } else if (num >= 3.4 && num < 3.9) {
                                imgAvaliacao.setImageResource(R.drawable.starrrmeio);
                            } else if (num >= 3.9 && num < 4.4) {
                                imgAvaliacao.setImageResource(R.drawable.starrrr);
                            } else if (num >= 4.4 && num < 4.8) {
                                imgAvaliacao.setImageResource(R.drawable.starrrrmeio);
                            } else if (num >= 4.8) {
                                imgAvaliacao.setImageResource(R.drawable.starrrrr);
                            }
                        }
                    }
                }
            }
        });

        return rootView;
    }

    private void updateList(){
        db.collection("listaInfo").document("1").collection("Avaliacoes").limit(100).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListAvaliacoes avaliacao = dc.getDocument().toObject(ListAvaliacoes.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            avaliacao.setKey(dc.getDocument().getId());
                            listAvaliacoes.add(avaliacao);

                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            index = getItemIndex((dc.getDocument().getId()));
                            avaliacao.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listAvaliacoes.set(index, avaliacao);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex((dc.getDocument().getId()));

                            if (index != -1) {
                                listAvaliacoes.remove(index);
                                adapter.notifyItemRemoved(index);
                            }
                            break;
                    }
                }
                if (listAvaliacoes.size()==0){
                    txtSemData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private int getItemIndex(String Key){

        int index = -1;

        for (int i = 0; i < listAvaliacoes.size(); i++){
            if ((listAvaliacoes.get(i).getKey()).equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onStart() {
        if (listAvaliacoes.size()==0) {
            listAvaliacoes.clear();
            updateList();
        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
        super.onStart();
    }

}
