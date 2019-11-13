package com.realvibedev.paoquente.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.TelaPadaria;
import com.realvibedev.paoquente.model.Padarias;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by bruno on 16/03/2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Padarias> listaPadarias;
    private static final String NUMERODEPADARIASADICIONADAS = "NumerodePadariasAdicionadas";
    private int numerosDePadariasAdicionadas = 0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    boolean aBoolean = true;

    public Adapter(List<Padarias> lista) {
        this.listaPadarias = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Padarias padaria = listaPadarias.get(position);
        int numAvaliacoes;
        if (!padaria.getNumAvaliacoes().equals("null")) {
            numAvaliacoes = Integer.parseInt(padaria.getNumAvaliacoes());
            if (numAvaliacoes <= 5){
                holder.numAvaliacoes.setText("Nova!");
            }else {
                holder.numAvaliacoes.setText(padaria.getNumAvaliacoes() + " avaliações");
            }
        }

        DecimalFormat df = new DecimalFormat("###.#");

        holder.nomePadaria.setText(padaria.getNomePadaria());
        holder.ultimaFornada.setText(padaria.getUltimaFornada());

        if (padaria.getAvaliacao().length() == 1){
            holder.avaliacao.setText(padaria.getAvaliacao() + ",0");
        }else {
            holder.avaliacao.setText(padaria.getAvaliacao());
        }

        holder.txtKm.setText(String.valueOf(df.format(padaria.getKm())) + " Km");
        Picasso.get().load(padaria.getImagemPadaria()).into(holder.imgPadaria);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TelaPadaria.class);
                intent.putExtra("Nome", padaria.getNomePadaria());
                intent.putExtra("Key", padaria.getKey());
                view.getContext().startActivity(intent);
            }
        });



        /*
        numerosDePadariasAdicionadas = preferences.getInt("NumeroPadarias", 0);
        preferences = holder.imgStar.getContext().getSharedPreferences(NUMERODEPADARIASADICIONADAS, 0);

        for (int i = 1; i <= numerosDePadariasAdicionadas; i++){
            String ref = preferences.getString("ref"+String.valueOf(i), "");
            if (ref.equals(padaria.getRef())){
                holder.imgStar.setImageResource(R.drawable.starcheia);
            }
        }

        holder.btnFavoritar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                for (int i = 1; i <= numerosDePadariasAdicionadas; i++){
                    int aux = preferences.getInt(String.valueOf(i), 0);
                    if (aux == Integer.parseInt(padaria.getKey())){
                        Toast.makeText(view.getContext(), "Padaria já adicionada.", Toast.LENGTH_LONG).show();
                        aBoolean = false;
                    }
                }
                if (aBoolean) {
                    editor = preferences.edit();
                    AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setMessage("Deseja adicionar " + padaria.getNomePadaria() + " aos seus favoritos ?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(true);
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (numerosDePadariasAdicionadas == 0) {
                                editor.putInt("1", Integer.parseInt(padaria.getKey()));
                                editor.putInt("NumeroPadarias", 1);
                                editor.apply();
                            } else {
                                editor.putInt(String.valueOf(numerosDePadariasAdicionadas + 1), Integer.parseInt(padaria.getKey()));
                                editor.putInt("NumeroPadarias", numerosDePadariasAdicionadas + 1);
                                editor.apply();
                            }
                            FirebaseMessaging.getInstance().subscribeToTopic(padaria.getKey());
                            holder.imgStar.setImageResource(R.drawable.starcheia);
                        }
                    });
                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return listaPadarias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nomePadaria, ultimaFornada, avaliacao, numAvaliacoes, txtKm;
        private ImageView imgPadaria;
        private RelativeLayout relativeLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            nomePadaria = itemView.findViewById(R.id.textView6);
            ultimaFornada = itemView.findViewById(R.id.textView7);
            imgPadaria = itemView.findViewById(R.id.imageView22);
            avaliacao = itemView.findViewById(R.id.textView8);
            numAvaliacoes = itemView.findViewById(R.id.textView50);
            txtKm = itemView.findViewById(R.id.textView154);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutPadaria);
        }
    }

}
