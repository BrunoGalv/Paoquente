package com.realvibedev.paoquente.FragmentsPadaria;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterPromos;
import com.realvibedev.paoquente.model.ListPromos;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPromocoes extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private List<ListPromos> listaPromos = new ArrayList<>();
    private AdapterPromos adapter;
    private ProgressBar progressBar2;
    private TextView txtNenhum;
    private Intent intent;
    private String key;

    public FragmentPromocoes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_promocoes, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView13);
        progressBar2 = rootView.findViewById(R.id.progressBar14);
        txtNenhum = rootView.findViewById(R.id.textView149);

        intent = getActivity().getIntent();

        key = intent.getStringExtra("Key");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterPromos(listaPromos, 2);
        recyclerView.setAdapter(adapter);

        return rootView;
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
    public void onStart() {
        if (listaPromos.size()==0) {
            listaPromos.clear();
            updateList();
        }else {
            progressBar2.setVisibility(View.INVISIBLE);
        }
        super.onStart();
    }

}
