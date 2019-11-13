package com.realvibedev.paoquente.FragmentsPadaria;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterGaleria;
import com.realvibedev.paoquente.model.ListGaleria;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGaleria extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ListGaleria> listGaleria = new ArrayList<>();
    private AdapterGaleria adapter;
    private Intent intent;
    private String key;
    private ProgressBar progressBar;
    private TextView txtSemData;
    private RecyclerView recyclerView;

    public FragmentGaleria() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_galeria, container, false);

        intent = getActivity().getIntent();
        key = intent.getStringExtra("Key");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_Galeria);
        txtSemData = (TextView) rootView.findViewById(R.id.textView35);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar7);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterGaleria(listGaleria, FragmentGaleria.this);
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    private void updateList(){
        db.collection("listaInfo").document(key).collection("Galeria").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                if (listGaleria.size()==0){
                    txtSemData.setVisibility(View.VISIBLE);
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


    public void imagem(String url){
        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog));
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

    @Override
    public void onStart() {
        if (listGaleria.size()==0) {
            listGaleria.clear();
            updateList();
        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
        super.onStart();
    }
}
