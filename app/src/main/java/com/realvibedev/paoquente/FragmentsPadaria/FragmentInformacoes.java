package com.realvibedev.paoquente.FragmentsPadaria;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.Maps.MapaFragment;
import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterCartoes;
import com.realvibedev.paoquente.adapter.AdapterHorarios;
import com.realvibedev.paoquente.adapter.AdapterListaTelefone;
import com.realvibedev.paoquente.model.ListCartoes;
import com.realvibedev.paoquente.model.ListHorarios;
import com.realvibedev.paoquente.model.ListTelefone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInformacoes extends Fragment {

    private TextView txtDinheiro;
    private ImageView imgPadaria;
    private String facebook, instagram;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private RecyclerView recyclerView4;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ListTelefone> listTelefones = new ArrayList<>();
    private List<ListCartoes> listCartoes = new ArrayList<>();
    private List<ListCartoes> listCartoes2 = new ArrayList<>();
    private List<ListHorarios> listHorarios = new ArrayList<>();
    private AdapterListaTelefone adapter;
    private AdapterCartoes adapter2;
    private AdapterCartoes adapter3;
    private AdapterHorarios adapter4;
    private boolean dinheiro = false;
    private Intent intent;
    private String key;

    public FragmentInformacoes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tela_informacoes, container, false);

        txtDinheiro = (TextView) rootView.findViewById(R.id.textView58);
        imgPadaria = rootView.findViewById(R.id.imageView36);

        intent = getActivity().getIntent();

        key = intent.getStringExtra("Key");

        db.collection("listaInfo").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && isAdded()) {
                        dinheiro = (boolean) document.getData().get("Dinheiro");

                        Picasso.get().load(document.getString("imagem")).into(imgPadaria);

                        if (!dinheiro){
                            txtDinheiro.setText("Não é aceito dinheiro");
                        }

                        if (listHorarios.size()==0||listCartoes.size()==0||listCartoes2.size()==0||listTelefones.size()==0) {
                            listCartoes.clear();
                            listCartoes2.clear();
                            listTelefones.clear();
                            listHorarios.clear();

                            if (document.getData().get("cartoes") != null) {
                                cartoes((List<String>) document.getData().get("cartoes"));
                            }

                            if (document.getData().get("cartoes2") != null) {
                                cartoes2((List<String>) document.getData().get("cartoes2"));
                            }

                            if (document.getData().get("telefones") != null) {
                                telefones((List<String>) document.getData().get("telefones"));
                            }

                            if (document.getData().get("horarios") != null) {
                                horarios((List<String>) document.getData().get("horarios"));
                            }
                        }

                        GeoPoint geoPoint = document.getGeoPoint("lat");
                        MapaFragment fragment = new MapaFragment();
                        Bundle arguments = new Bundle();
                        arguments.putDouble("lat", geoPoint.getLatitude());
                        arguments.putDouble("longi", geoPoint.getLongitude());
                        arguments.putString("nome", (String) document.getData().get("nome"));
                        fragment.setArguments(arguments);
                        FragmentManager manager = getChildFragmentManager();
                        int fragmentCount = manager.getBackStackEntryCount();
                        assert manager != null;
                        FragmentTransaction tx = manager.beginTransaction();
                        if (fragmentCount == 0) {
                            tx.add(R.id.framelayoutSalaoInformacoes, fragment);
                        } else {
                            tx.replace(R.id.framelayoutSalaoInformacoes, fragment);
                        }
                        tx.commitAllowingStateLoss();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.erro) + " Cod: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });



        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewListaTelefones);
        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recyclerViewListaCartoes);
        recyclerView3 = (RecyclerView) rootView.findViewById(R.id.recyclerViewListaCartoesDebito);
        recyclerView4 = (RecyclerView) rootView.findViewById(R.id.recyclerViewListaHorarios);



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getContext());
        recyclerView4.setLayoutManager(layoutManager4);
        recyclerView4.setHasFixedSize(true);


        adapter = new AdapterListaTelefone(listTelefones, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        adapter2 = new AdapterCartoes(listCartoes);
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setNestedScrollingEnabled(false);

        adapter3 = new AdapterCartoes(listCartoes2);
        recyclerView3.setAdapter(adapter3);
        recyclerView3.setNestedScrollingEnabled(false);

        adapter4 = new AdapterHorarios(listHorarios);
        recyclerView4.setAdapter(adapter4);
        recyclerView4.setNestedScrollingEnabled(false);


        return rootView;
    }

    private void cartoes(List<String> array){
        for (int i = 0; i < array.size(); i++){
            if (array.get(i).length() > 1){
                ListCartoes cartao = new ListCartoes();
                cartao.setKey(array.get(i));
                listCartoes.add(cartao);
            }else {
                ListCartoes cartao = new ListCartoes();
                cartao.setKey(array.get(i));
                listCartoes.add(cartao);
            }
        }
        adapter2.notifyDataSetChanged();
    }

    private void cartoes2(List<String> array){
        for (int i = 0; i < array.size(); i++){
            if (array.get(i).length() > 1){
                ListCartoes cartao = new ListCartoes();
                cartao.setKey(array.get(i));
                listCartoes2.add(cartao);
            }else {
                ListCartoes cartao = new ListCartoes();
                cartao.setKey(array.get(i));
                listCartoes2.add(cartao);
            }
        }
        adapter3.notifyDataSetChanged();
    }

    private void telefones(List<String> array){
        for (int i = 0; i < array.size(); i++){
            if (array.get(i).contains("+")){
                ListTelefone numero = new ListTelefone();
                numero.setKey("10");
                numero.setNumeroTelefone(array.get(i));
                listTelefones.add(numero);
            }else {
                ListTelefone numero = new ListTelefone();
                numero.setKey(String.valueOf(i + 1));
                numero.setNumeroTelefone(array.get(i));
                listTelefones.add(numero);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void horarios(List<String> array){
        for (int i = 0; i < array.size(); i++){
            ListHorarios horario = new ListHorarios();

            if (array.get(i).contains("1;")){
                horario.setKey("1");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
            if (array.get(i).contains("2;")){
                horario.setKey("2");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
            if (array.get(i).contains("3;")){
                horario.setKey("3");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
            if (array.get(i).contains("4;")){
                horario.setKey("4");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
            if (array.get(i).contains("5;")){
                horario.setKey("5");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
            if (array.get(i).contains("6;")){
                horario.setKey("6");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
            if (array.get(i).contains("7;")){
                horario.setKey("7");
                if (array.get(i).contains("N")){
                    horario.setHorario("nao");
                }else {
                    horario.setHorario(array.get(i).substring(2));
                }
                listHorarios.add(horario);
            }
        }
        adapter4.notifyDataSetChanged();
    }

}
