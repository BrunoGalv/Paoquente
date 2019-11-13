package com.realvibedev.paoquente.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.model.ListCartoes;

import java.util.List;

/**
 * Created by bruno on 20/10/18.
 */

public class AdapterCartoes extends RecyclerView.Adapter<AdapterCartoes.MyViewHolder> {

    private List<ListCartoes> listaCartoes;

    public AdapterCartoes() {
    }

    public AdapterCartoes(List<ListCartoes> listaCartoes) {
        this.listaCartoes = listaCartoes;
    }


    public AdapterCartoes.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_cartoesdecredito, parent, false);
        return new MyViewHolder(itemLista);
    }


    public void onBindViewHolder(AdapterCartoes.MyViewHolder holder, int position) {

        final ListCartoes lista = listaCartoes.get(position);

        switch (lista.getKey()){
            case "1":
                holder.texto.setText(holder.texto.getResources().getString(R.string.mastercard));
                holder.imgCartao.setImageResource(R.drawable.mastercardlogo);
                break;
            case "2":
                holder.texto.setText(holder.texto.getResources().getString(R.string.visa));
                holder.imgCartao.setImageResource(R.drawable.visalogo);
                break;
            case "3":
                holder.texto.setText(holder.texto.getResources().getString(R.string.elo));
                holder.imgCartao.setImageResource(R.drawable.elologo);
                break;
            case "4":
                holder.texto.setText(holder.texto.getResources().getString(R.string.hipercard));
                holder.imgCartao.setImageResource(R.drawable.hipercardlogo);
                break;
            case "5":
                holder.texto.setText(holder.texto.getResources().getString(R.string.americanexpress));
                holder.imgCartao.setImageResource(R.drawable.americanexpress);
                break;
            case "6":
                holder.texto.setText(holder.texto.getResources().getString(R.string.dinersclub));
                holder.imgCartao.setImageResource(R.drawable.cartaodiners);
                break;
            default:
                holder.imgCartao.setImageResource(R.drawable.creditcard);
                holder.texto.setText(lista.getKey());
        }
    }

    public int getItemCount() {
        return listaCartoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView texto;
        private ImageView imgCartao;

        public MyViewHolder(View itemView) {

            super(itemView);
            texto = itemView.findViewById(R.id.textView82);
            imgCartao = itemView.findViewById(R.id.imageView81);
        }
    }
}
