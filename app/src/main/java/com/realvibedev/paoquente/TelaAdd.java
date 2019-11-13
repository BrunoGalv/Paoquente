package com.realvibedev.paoquente;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.realvibedev.paoquente.adapter.Adapter;
import com.realvibedev.paoquente.model.Padarias;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TelaAdd extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Padarias> listaPadarias = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaReference;
    private Adapter adapter;
    private Spinner spinnerEstado, spinnerCidade, spinnerBairro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_add);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAdd);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new Adapter(listaPadarias);
        recyclerView.setAdapter(adapter);


        //Spinners
        spinnerEstado = (Spinner) findViewById(R.id.spinner);
        spinnerCidade = (Spinner) findViewById(R.id.spinner2);
        spinnerBairro = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter adapterSpinnerEstado = ArrayAdapter.createFromResource(this,R.array.Estados, android.R.layout.simple_list_item_activated_1);
        final ArrayAdapter adapterSpinnerCidadeRJ = ArrayAdapter.createFromResource(this,R.array.CidadesRJ, android.R.layout.simple_list_item_activated_1);
        final ArrayAdapter adapterSpinnerCidadeSP = ArrayAdapter.createFromResource(this,R.array.CidadesSP, android.R.layout.simple_list_item_activated_1);
        final ArrayAdapter adapterSpinnerBairroRJ = ArrayAdapter.createFromResource(this,R.array.BairrosRJ, android.R.layout.simple_list_item_activated_1);
        final ArrayAdapter adapterSpinnerBairroSP = ArrayAdapter.createFromResource(this,R.array.BairrosSP, android.R.layout.simple_list_item_activated_1);


        spinnerEstado.setAdapter(adapterSpinnerEstado);
        spinnerCidade.setAdapter(adapterSpinnerCidadeRJ);
        spinnerBairro.setAdapter(adapterSpinnerBairroRJ);


        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        spinnerCidade.setAdapter(adapterSpinnerCidadeRJ);
                        spinnerBairro.setAdapter(adapterSpinnerBairroRJ);
                        break;
                    case 1:
                        spinnerCidade.setAdapter(adapterSpinnerCidadeSP);
                        spinnerBairro.setAdapter(adapterSpinnerBairroSP);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String aux = (String) spinnerCidade.getSelectedItem();
                switch (aux){
                    case "Rio de Janeiro":
                        spinnerBairro.setAdapter(adapterSpinnerBairroRJ);
                        break;
                    case "São Paulo":
                        spinnerBairro.setAdapter(adapterSpinnerBairroSP);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBairro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String aux = (String) spinnerBairro.getSelectedItem();
                switch (aux){
                    case "Anchieta":
                        databaReference = database.getReference("PadariasRj").child("PadariasAnchieta");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Bangu":
                        databaReference = database.getReference("PadariasRj").child("PadariasBangu");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Barra da Tijuca":
                        databaReference = database.getReference("PadariasRj").child("PadariasBarra da Tijuca");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Benfica":
                        databaReference = database.getReference("PadariasRj").child("PadariasBenfica");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Bento Ribeiro":
                        databaReference = database.getReference("PadariasRj").child("PadariasBento Ribeiro");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Bonsucesso":
                        databaReference = database.getReference("PadariasRj").child("PadariasBonsucesso");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Botafogo":
                        databaReference = database.getReference("PadariasRj").child("PadariasBotafogo");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Campinho":
                        databaReference = database.getReference("PadariasRj").child("PadariasCampinho");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Campo dos Afonsos":
                        databaReference = database.getReference("PadariasRj").child("PadariasCampo dos Afonsos");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Campo Grande":
                        databaReference = database.getReference("PadariasRj").child("PadariasCampo Grande");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Cascadura":
                        databaReference = database.getReference("PadariasRj").child("PadariasCascadura");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Catete":
                        databaReference = database.getReference("PadariasRj").child("PadariasCatete");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Centro":
                        databaReference = database.getReference("PadariasRj").child("PadariasCentro");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Cidade de Deus":
                        databaReference = database.getReference("PadariasRj").child("PadariasCidade de Deus");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Cidade Universitária":
                        databaReference = database.getReference("PadariasRj").child("PadariasCidade Universitária");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Coelho Neto":
                        databaReference = database.getReference("PadariasRj").child("PadariasCoelho Neto");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Copacabana":
                        databaReference = database.getReference("PadariasRj").child("PadariasCopacabana");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Deodoro":
                        databaReference = database.getReference("PadariasRj").child("PadariasDeodoro");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Encantado":
                        databaReference = database.getReference("PadariasRj").child("PadariasEncantado");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Engenho de Dentro":
                        databaReference = database.getReference("PadariasRj").child("PadariasEngenho de Dentro");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Engenho Novo":
                        databaReference = database.getReference("PadariasRj").child("PadariasEngenho Novo");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Flamengo":
                        databaReference = database.getReference("PadariasRj").child("PadariasFlamengo");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Freguesia":
                        databaReference = database.getReference("PadariasRj").child("PadariasFreguesia");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Galeão":
                        databaReference = database.getReference("PadariasRj").child("PadariasGaleão");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Gardênia Azul":
                        databaReference = database.getReference("PadariasRj").child("PadariasGardênia Azul");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Gávea":
                        databaReference = database.getReference("PadariasRj").child("PadariasGávea");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Grajaú":
                        databaReference = database.getReference("PadariasRj").child("PadariasGrajaú");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Guadalupe":
                        databaReference = database.getReference("PadariasRj").child("PadariasGuadalupe");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Guaratiba":
                        databaReference = database.getReference("PadariasRj").child("PadariasGuaratiba");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Humaitá":
                        databaReference = database.getReference("PadariasRj").child("PadariasHumaitá");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Inhaúma":
                        databaReference = database.getReference("PadariasRj").child("PadariasInhaúma");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Ipanema":
                        databaReference = database.getReference("PadariasRj").child("PadariasIpanema");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Irajá":
                        databaReference = database.getReference("PadariasRj").child("PadariasIrajá");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Jacaré":
                        databaReference = database.getReference("PadariasRj").child("PadariasJacaré");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Jacarepaguá":
                        databaReference = database.getReference("PadariasRj").child("PadariasJacarepaguá");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Jardim Botânico":
                        databaReference = database.getReference("PadariasRj").child("PadariasJardim Botânico");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Jardim Sulacap":
                        databaReference = database.getReference("PadariasRj").child("PadariasJardim Sulacap");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Lagoa":
                        databaReference = database.getReference("PadariasRj").child("PadariasLagoa");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Laranjeiras":
                        databaReference = database.getReference("PadariasRj").child("PadariasLaranjeiras");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Leblon":
                        databaReference = database.getReference("PadariasRj").child("PadariasLeblon");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Leme":
                        databaReference = database.getReference("PadariasRj").child("PadariasLeme");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Madureira":
                        databaReference = database.getReference("PadariasRj").child("PadariasMadureira");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Magalhães Bastos":
                        databaReference = database.getReference("PadariasRj").child("PadariasMagalhães Bastos");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Maracanã":
                        databaReference = database.getReference("PadariasRj").child("PadariasMaracanã");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Marechal Hermes":
                        databaReference = database.getReference("PadariasRj").child("PadariasMarechal Hermes");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Méier":
                        databaReference = database.getReference("PadariasRj").child("PadariasMéier");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "EncantadoMeier":
                        databaReference = database.getReference("PadariasRj").child("PadariasMeier");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Olaria":
                        databaReference = database.getReference("PadariasRj").child("PadariasOlaria");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Oswaldo Cruz":
                        databaReference = database.getReference("PadariasRj").child("PadariasOswaldo Cruz");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Paciência":
                        databaReference = database.getReference("PadariasRj").child("PadariasPaciência");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Padre Miguel":
                        databaReference = database.getReference("PadariasRj").child("PadariasPadre Miguel");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Pavuna":
                        databaReference = database.getReference("PadariasRj").child("PadariasPavuna");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Praça Seca":
                        databaReference = database.getReference("PadariasRj").child("PadariasPraça Seca");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Ramos":
                        databaReference = database.getReference("PadariasRj").child("PadariasRamos");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Realengo":
                        databaReference = database.getReference("PadariasRj").child("PadariasRealengo");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Recreio dos Bandeirantes":
                        databaReference = database.getReference("PadariasRj").child("PadariasRecreio dos Bandeirantes");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Rocha Miranda":
                        databaReference = database.getReference("PadariasRj").child("PadariasRocha Miranda");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "São Conrado":
                        databaReference = database.getReference("PadariasRj").child("PadariasSão Conrado");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "São Cristóvão":
                        databaReference = database.getReference("PadariasRj").child("PadariasSão Cristóvão");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Senador Camará":
                        databaReference = database.getReference("PadariasRj").child("PadariasSenador Camará");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Tanque":
                        databaReference = database.getReference("PadariasRj").child("PadariasTanque");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Taquara":
                        databaReference = database.getReference("PadariasRj").child("PadariasTaquara");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Tijuca":
                        databaReference = database.getReference("PadariasRj").child("PadariasTijuca");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Urca":
                        databaReference = database.getReference("PadariasRj").child("PadariasUrca");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Vargem Grande":
                        databaReference = database.getReference("PadariasRj").child("PadariasVargem Grande");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Vargem Pequena":
                        databaReference = database.getReference("PadariasRj").child("PadariasVargem Pequena");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Vila Isabel":
                        databaReference = database.getReference("PadariasRj").child("PadariasVila Isabel");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Vila Militar":
                        databaReference = database.getReference("PadariasRj").child("PadariasVila Militar");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                    case "Vila Valqueire":
                        databaReference = database.getReference("PadariasRj").child("PadariasVilaValqueire");
                        listaPadarias = new ArrayList<>();
                        adapter = new Adapter(listaPadarias);
                        recyclerView.setAdapter(adapter);
                        updateList();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void updateList(){
        databaReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Padarias padarias = dataSnapshot.getValue(Padarias.class);
                padarias.setKey(dataSnapshot.getKey());
                listaPadarias.add(padarias);

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Padarias padarias = dataSnapshot.getValue(Padarias.class);
                padarias.setKey(dataSnapshot.getKey());
                int index = getItemIndex(padarias);

                listaPadarias.set(index, padarias);
                adapter.notifyItemChanged(index);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Padarias padarias = dataSnapshot.getValue(Padarias.class);

                int index = getItemIndex(padarias);

                listaPadarias.remove(index);
                adapter.notifyItemRemoved(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(Padarias padarias){

        int index = -1;

        for (int i = 0; i < listaPadarias.size(); i++){
            if (listaPadarias.get(i).getKey().equals(padarias.getKey())){
                index = i;
                break;
            }
        }
        return index;
    }

}
