package com.realvibedev.paoquente.Padaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.adapter.AdapterCartoes;
import com.realvibedev.paoquente.adapter.AdapterHorarios;
import com.realvibedev.paoquente.adapter.AdapterListaTelefone;
import com.realvibedev.paoquente.model.ListCartoes;
import com.realvibedev.paoquente.model.ListHorarios;
import com.realvibedev.paoquente.model.ListTelefone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaEditInfo extends AppCompatActivity {

    private ImageView imgBack;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private Button btnEditHorarios, btnEditTelefones, btnEditDinheiro, btnEditCartaoCredito, btnEditCartaoDebito, btnEditRedesSociais;
    private TextView txtDinheiro;
    private boolean dinheiro = false;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private RecyclerView recyclerView4;
    private List<ListTelefone> listTelefones = new ArrayList<>();
    private List<ListCartoes> listCartoes = new ArrayList<>();
    private List<ListCartoes> listCartoes2 = new ArrayList<>();
    private List<ListHorarios> listHorarios = new ArrayList<>();
    private AdapterListaTelefone adapter;
    private AdapterCartoes adapter2;
    private AdapterCartoes adapter3;
    private AdapterHorarios adapter4;
    private String key, state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_edit_info);

        imgBack = (ImageView) findViewById(R.id.imageView57);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView9);
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView10);
        recyclerView3 = (RecyclerView) findViewById(R.id.recyclerView11);
        recyclerView4 = (RecyclerView) findViewById(R.id.recyclerView8);
        btnEditHorarios = (Button) findViewById(R.id.button30);
        btnEditTelefones = (Button) findViewById(R.id.button31);
        btnEditDinheiro = (Button) findViewById(R.id.button32);
        btnEditCartaoCredito = (Button) findViewById(R.id.button33);
        btnEditCartaoDebito = (Button) findViewById(R.id.button35);
        txtDinheiro = (TextView) findViewById(R.id.textView48);


        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            loadData();
            key = user.getUid();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TelaEditInfo.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(TelaEditInfo.this);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(TelaEditInfo.this);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(TelaEditInfo.this);
        recyclerView4.setLayoutManager(layoutManager4);
        recyclerView4.setHasFixedSize(true);


        adapter = new AdapterListaTelefone(listTelefones, TelaEditInfo.this);
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

        /*
        btnEditDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaEditInfo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_descricao, null);
                alerta.setView(viewAlerta);
                final EditText edtDescricao = (EditText) viewAlerta.findViewById(R.id.editTextDescricao);
                alerta.setMessage(getResources().getString(R.string.digiteDescricao));
                alerta.setCancelable(true);
                alerta.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (key!=null&&state!=null){
                            if (!edtDescricao.getText().toString().equals("")) {
                                db.collection("listaInfo").document(key).update("Descricao", edtDescricao.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        txtDescricao.setText(edtDescricao.getText().toString());
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadData();
                                    }
                                });
                            }
                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });
        */

        btnEditDinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaEditInfo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_dinheiro, null);
                alerta.setView(viewAlerta);
                final Switch switchDin = (Switch) viewAlerta.findViewById(R.id.switch9);
                switchDin.setChecked(dinheiro);
                alerta.setCancelable(true);
                alerta.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (key!=null&&state!=null){
                            if (switchDin.isChecked()){
                                db.collection("listaInfo").document(key).update("Dinheiro", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadData();
                                        dinheiro = true;
                                    }
                                });
                            }else {
                                db.collection("listaInfo").document(key).update("Dinheiro", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadData();
                                        dinheiro = false;
                                    }
                                });
                            }
                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });


        btnEditTelefones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaEditInfo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_telefones, null);
                alerta.setView(viewAlerta);
                final EditText edtTelefone = (EditText) viewAlerta.findViewById(R.id.editText29);
                final EditText edtTelefone2 = (EditText) viewAlerta.findViewById(R.id.editText30);
                final EditText edtTelefone3 = (EditText) viewAlerta.findViewById(R.id.editText31);
                final EditText edtTelefone4 = (EditText) viewAlerta.findViewById(R.id.editText32);
                final EditText edtTelefone5 = (EditText) viewAlerta.findViewById(R.id.editText33);
                final IntlPhoneInput phoneInput = (IntlPhoneInput) viewAlerta.findViewById(R.id.phoneInput);
                alerta.setCancelable(false);
                alerta.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (key!=null&&state!=null){

                            List<String> array = new ArrayList<String>();

                            if (!edtTelefone.getText().toString().equals("")) {
                                array.add(edtTelefone.getText().toString());
                            }
                            if (!edtTelefone2.getText().toString().equals("")) {
                                array.add(edtTelefone2.getText().toString());
                            }
                            if (!edtTelefone3.getText().toString().equals("")) {
                                array.add(edtTelefone3.getText().toString());
                            }
                            if (!edtTelefone4.getText().toString().equals("")) {
                                array.add(edtTelefone4.getText().toString());
                            }
                            if (!edtTelefone5.getText().toString().equals("")) {
                                array.add(edtTelefone5.getText().toString());
                            }
                            if (phoneInput.isValid()){
                                array.add("W" + phoneInput.getNumber());
                            }

                            db.collection("listaInfo").document(key).update("telefones", array).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadData();
                                }
                            });
                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        btnEditCartaoCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> newData = new HashMap<>();
                newData.put("a", "a");
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaEditInfo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_cartao, null);
                alerta.setView(viewAlerta);
                final EditText edtOutros = (EditText) viewAlerta.findViewById(R.id.editText34);
                final Switch switchC1 = (Switch) viewAlerta.findViewById(R.id.switch10);
                final Switch switchC2 = (Switch) viewAlerta.findViewById(R.id.switch11);
                final Switch switchC3 = (Switch) viewAlerta.findViewById(R.id.switch12);
                final Switch switchC4 = (Switch) viewAlerta.findViewById(R.id.switch13);
                final Switch switchC5 = (Switch) viewAlerta.findViewById(R.id.switch14);
                final Switch switchC6 = (Switch) viewAlerta.findViewById(R.id.switch15);
                if (listCartoes!=null){
                    for (int i = 0; i < listCartoes.size(); i++){
                        switch (listCartoes.get(i).getKey()){
                            case "1":
                                switchC1.setChecked(true);
                                break;
                            case "2":
                                switchC2.setChecked(true);
                                break;
                            case "3":
                                switchC3.setChecked(true);
                                break;
                            case "4":
                                switchC4.setChecked(true);
                                break;
                            case "5":
                                switchC5.setChecked(true);
                                break;
                            case "6":
                                switchC6.setChecked(true);
                                break;
                        }
                    }
                }
                alerta.setCancelable(true);
                alerta.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (key!=null&&state!=null){

                            List<String> array = new ArrayList<String>();

                            if (switchC1.isChecked()){
                                array.add("1");
                            }
                            if (switchC2.isChecked()){
                                array.add("2");
                            }
                            if (switchC3.isChecked()){
                                array.add("3");
                            }
                            if (switchC4.isChecked()){
                                array.add("4");
                            }
                            if (switchC5.isChecked()){
                                array.add("5");
                            }
                            if (switchC6.isChecked()){
                                array.add("6");
                            }
                            if (!edtOutros.getText().toString().equals("") && !(edtOutros.getText().toString().equals("excluir") || edtOutros.getText().toString().equals("Excluir"))){
                                array.add(edtOutros.getText().toString());
                            }

                            db.collection("listaInfo").document(key).update("cartoes", array).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadData();
                                }
                            });
                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        btnEditCartaoDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> newData = new HashMap<>();
                newData.put("a", "a");
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaEditInfo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_cartao, null);
                alerta.setView(viewAlerta);
                final EditText edtOutros = (EditText) viewAlerta.findViewById(R.id.editText34);
                final Switch switchC1 = (Switch) viewAlerta.findViewById(R.id.switch10);
                final Switch switchC2 = (Switch) viewAlerta.findViewById(R.id.switch11);
                final Switch switchC3 = (Switch) viewAlerta.findViewById(R.id.switch12);
                final Switch switchC4 = (Switch) viewAlerta.findViewById(R.id.switch13);
                final Switch switchC5 = (Switch) viewAlerta.findViewById(R.id.switch14);
                final Switch switchC6 = (Switch) viewAlerta.findViewById(R.id.switch15);
                if (listCartoes2!=null){
                    for (int i = 0; i < listCartoes2.size(); i++){
                        switch (listCartoes2.get(i).getKey()){
                            case "1":
                                switchC1.setChecked(true);
                                break;
                            case "2":
                                switchC2.setChecked(true);
                                break;
                            case "3":
                                switchC3.setChecked(true);
                                break;
                            case "4":
                                switchC4.setChecked(true);
                                break;
                            case "5":
                                switchC5.setChecked(true);
                                break;
                            case "6":
                                switchC6.setChecked(true);
                                break;
                        }
                    }
                }
                alerta.setCancelable(true);
                alerta.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (key!=null&&state!=null){

                            List<String> array = new ArrayList<String>();

                            if (switchC1.isChecked()){
                                array.add("1");
                            }
                            if (switchC2.isChecked()){
                                array.add("2");
                            }
                            if (switchC3.isChecked()){
                                array.add("3");
                            }
                            if (switchC4.isChecked()){
                                array.add("4");
                            }
                            if (switchC5.isChecked()){
                                array.add("5");
                            }
                            if (switchC6.isChecked()){
                                array.add("6");
                            }
                            if (!edtOutros.getText().toString().equals("") && !(edtOutros.getText().toString().equals("excluir") || edtOutros.getText().toString().equals("Excluir"))){
                                array.add(edtOutros.getText().toString());
                            }

                            db.collection("listaInfo").document(key).update("cartoes2", array).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadData();
                                }
                            });

                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        btnEditHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> newData = new HashMap<>();
                newData.put("horario", "nao");
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(TelaEditInfo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_horarios, null);
                alerta.setView(viewAlerta);
                //final EditText edtDescricao = (EditText) viewAlerta.findViewById(R.id.editTextDescricao);
                final Switch switchSeg = (Switch) viewAlerta.findViewById(R.id.switch2);
                final Switch switchTer = (Switch) viewAlerta.findViewById(R.id.switch3);
                final Switch switchQuar = (Switch) viewAlerta.findViewById(R.id.switch4);
                final Switch switchQuin = (Switch) viewAlerta.findViewById(R.id.switch5);
                final Switch switchSex = (Switch) viewAlerta.findViewById(R.id.switch6);
                final Switch switchSab = (Switch) viewAlerta.findViewById(R.id.switch7);
                final Switch switchDom = (Switch) viewAlerta.findViewById(R.id.switch8);
                final EditText edt = (EditText) viewAlerta.findViewById(R.id.editText);
                final EditText edt2 = (EditText) viewAlerta.findViewById(R.id.editText2);
                final EditText edt3 = (EditText) viewAlerta.findViewById(R.id.editText3);
                final EditText edt4 = (EditText) viewAlerta.findViewById(R.id.editText4);
                final EditText edt5 = (EditText) viewAlerta.findViewById(R.id.editText5);
                final EditText edt6 = (EditText) viewAlerta.findViewById(R.id.editText6);
                final EditText edt7 = (EditText) viewAlerta.findViewById(R.id.editText7);
                final EditText edt8 = (EditText) viewAlerta.findViewById(R.id.editText8);
                final EditText edt9 = (EditText) viewAlerta.findViewById(R.id.editText9);
                final EditText edt10 = (EditText) viewAlerta.findViewById(R.id.editText10);
                final EditText edt11 = (EditText) viewAlerta.findViewById(R.id.editText11);
                final EditText edt12 = (EditText) viewAlerta.findViewById(R.id.editText12);
                final EditText edt13 = (EditText) viewAlerta.findViewById(R.id.editText13);
                final EditText edt14 = (EditText) viewAlerta.findViewById(R.id.editText14);
                final EditText edt15 = (EditText) viewAlerta.findViewById(R.id.editText15);
                final EditText edt16 = (EditText) viewAlerta.findViewById(R.id.editText16);
                final EditText edt17 = (EditText) viewAlerta.findViewById(R.id.editText17);
                final EditText edt18 = (EditText) viewAlerta.findViewById(R.id.editText18);
                final EditText edt19 = (EditText) viewAlerta.findViewById(R.id.editText19);
                final EditText edt20 = (EditText) viewAlerta.findViewById(R.id.editText20);
                final EditText edt21 = (EditText) viewAlerta.findViewById(R.id.editText21);
                final EditText edt22 = (EditText) viewAlerta.findViewById(R.id.editText22);
                final EditText edt23 = (EditText) viewAlerta.findViewById(R.id.editText23);
                final EditText edt24 = (EditText) viewAlerta.findViewById(R.id.editText24);
                final EditText edt25 = (EditText) viewAlerta.findViewById(R.id.editText25);
                final EditText edt26 = (EditText) viewAlerta.findViewById(R.id.editText26);
                final EditText edt27 = (EditText) viewAlerta.findViewById(R.id.editText27);
                final EditText edt28 = (EditText) viewAlerta.findViewById(R.id.editText28);

                edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt2.requestFocus();
                        }
                    }
                });
                edt2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt3.requestFocus();
                        }
                    }
                });
                edt3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt4.requestFocus();
                        }
                    }
                });
                edt4.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt5.requestFocus();
                        }
                    }
                });
                edt5.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt6.requestFocus();
                        }
                    }
                });
                edt6.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt7.requestFocus();
                        }
                    }
                });
                edt7.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt8.requestFocus();
                        }
                    }
                });
                edt8.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt9.requestFocus();
                        }
                    }
                });
                edt9.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt10.requestFocus();
                        }
                    }
                });
                edt10.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt11.requestFocus();
                        }
                    }
                });
                edt11.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt12.requestFocus();
                        }
                    }
                });
                edt12.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt13.requestFocus();
                        }
                    }
                });
                edt13.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt14.requestFocus();
                        }
                    }
                });
                edt14.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt15.requestFocus();
                        }
                    }
                });
                edt15.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt16.requestFocus();
                        }
                    }
                });
                edt16.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt17.requestFocus();
                        }
                    }
                });
                edt17.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt18.requestFocus();
                        }
                    }
                });
                edt18.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt19.requestFocus();
                        }
                    }
                });
                edt19.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt20.requestFocus();
                        }
                    }
                });
                edt20.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt21.requestFocus();
                        }
                    }
                });
                edt21.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt22.requestFocus();
                        }
                    }
                });
                edt22.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt23.requestFocus();
                        }
                    }
                });
                edt23.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt24.requestFocus();
                        }
                    }
                });
                edt24.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt25.requestFocus();
                        }
                    }
                });
                edt25.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt26.requestFocus();
                        }
                    }
                });
                edt26.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt27.requestFocus();
                        }
                    }
                });
                edt27.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 2){
                            edt28.requestFocus();
                        }
                    }
                });

                alerta.setCancelable(false);
                alerta.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (key!=null && state!=null){

                            List<String> array = new ArrayList<String>();

                            if ((!edt.getText().toString().equals("") && !edt2.getText().toString().equals("") && !edt3.getText().toString().equals("") && !edt4.getText().toString().equals("")) || switchSeg.isChecked()){
                                if (switchSeg.isChecked()){
                                    array.add("1;N");
                                }else {
                                    array.add("1;" + edt.getText().toString()+":"+edt2.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt3.getText().toString()+":"+edt4.getText().toString());
                                }
                            }
                            if ((!edt5.getText().toString().equals("") && !edt6.getText().toString().equals("") && !edt7.getText().toString().equals("") && !edt8.getText().toString().equals("")) || switchTer.isChecked()){
                                if (switchTer.isChecked()){
                                    array.add("2;N");
                                }else {
                                    array.add("2;" + edt5.getText().toString()+":"+edt6.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt7.getText().toString()+":"+edt8.getText().toString());
                                }
                            }
                            if ((!edt9.getText().toString().equals("") && !edt10.getText().toString().equals("") && !edt11.getText().toString().equals("") && !edt12.getText().toString().equals("")) || switchQuar.isChecked()){
                                if (switchQuar.isChecked()){
                                    array.add("3;N");
                                }else {
                                    array.add("3;" + edt9.getText().toString()+":"+edt10.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt11.getText().toString()+":"+edt12.getText().toString());
                                }
                            }
                            if ((!edt13.getText().toString().equals("") && !edt14.getText().toString().equals("") && !edt15.getText().toString().equals("") && !edt16.getText().toString().equals("")) || switchQuin.isChecked()){
                                if (switchQuin.isChecked()){
                                    array.add("4;N");
                                }else {
                                    array.add("4;" + edt13.getText().toString()+":"+edt14.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt15.getText().toString()+":"+edt16.getText().toString());
                                }
                            }
                            if ((!edt17.getText().toString().equals("") && !edt18.getText().toString().equals("") && !edt19.getText().toString().equals("") && !edt20.getText().toString().equals("")) || switchSex.isChecked()){
                                if (switchSex.isChecked()){
                                    array.add("5;N");
                                }else {
                                    array.add("5;" + edt17.getText().toString()+":"+edt18.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt19.getText().toString()+":"+edt20.getText().toString());
                                }
                            }
                            if ((!edt21.getText().toString().equals("") && !edt22.getText().toString().equals("") && !edt23.getText().toString().equals("") && !edt24.getText().toString().equals("")) || switchSab.isChecked()){
                                if (switchSab.isChecked()){
                                    array.add("6;N");
                                }else {
                                    array.add("6;" + edt21.getText().toString()+":"+edt22.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt23.getText().toString()+":"+edt24.getText().toString());
                                }
                            }
                            if ((!edt25.getText().toString().equals("") && !edt26.getText().toString().equals("") && !edt27.getText().toString().equals("") && !edt28.getText().toString().equals("")) || switchDom.isChecked()){
                                if (switchDom.isChecked()){
                                    array.add("7;N");
                                }else {
                                    array.add("7;" + edt25.getText().toString()+":"+edt26.getText().toString()+" "+getResources().getString(R.string.as)+" "+edt27.getText().toString()+":"+edt28.getText().toString());
                                }
                            }
                            db.collection("listaInfo").document(key).update("horarios", array).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadData();
                                }
                            });
                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaEditInfo.super.onBackPressed();
            }
        });
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

    private void loadData(){
        db.collection("usersPro").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        key = user.getUid();
                        state = (String) document.getData().get("State");
                        DocumentReference docRef2 = db.collection("listaInfo").document(key);
                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        dinheiro = (boolean) document.getData().get("Dinheiro");
                                        if (!dinheiro){
                                            txtDinheiro.setText("Não é aceito dinheiro");
                                        }else {
                                            txtDinheiro.setText("Dinheiro");
                                        }

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
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(TelaEditInfo.this, getString(R.string.erro) + " Cod: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
