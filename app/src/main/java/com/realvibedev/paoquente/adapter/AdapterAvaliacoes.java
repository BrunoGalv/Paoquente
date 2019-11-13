package com.realvibedev.paoquente.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.model.ListAvaliacoes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bruno on 22/05/2018.
 */

public class AdapterAvaliacoes extends RecyclerView.Adapter<AdapterAvaliacoes.MyViewHolder>{

    private List<ListAvaliacoes> listaAvaliacoes;

    public AdapterAvaliacoes() {
    }

    public AdapterAvaliacoes(List<ListAvaliacoes> listaAvaliacoes) {
        this.listaAvaliacoes = listaAvaliacoes;
    }


    public AdapterAvaliacoes.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_avaliacoes, parent, false);
        return new MyViewHolder(itemLista);
    }

    public void onBindViewHolder(AdapterAvaliacoes.MyViewHolder holder, int position) {

        final ListAvaliacoes lista = listaAvaliacoes.get(position);
        holder.txtNome.setText(lista.getNome());
        holder.txtAvaliacao.setText(lista.getTexto());
        holder.txtNumAvaliacao.setText(lista.getAvaliacao());
        if (lista.getTexto() == null) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) holder.imageView.getLayoutParams();
            marginParams.setMargins(20, 30*-1, 20, 0);
        }
        if (lista.getImagem() != null){
            Glide.with(holder.imageView.getContext()).load(lista.getImagem()).thumbnail(0.5f).apply(new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.perfilpng)
            ).into(holder.imgPerfil);
        }
        switch (lista.getAvaliacao()){
            case "1,0":
                holder.imgStar.setImageResource(R.drawable.star);
                break;
            case "2,0":
                holder.imgStar.setImageResource(R.drawable.starr);
                break;
            case "3,0":
                holder.imgStar.setImageResource(R.drawable.starrr);
                break;
            case "4,0":
                holder.imgStar.setImageResource(R.drawable.starrrr);
                break;
            case "5,0":
                holder.imgStar.setImageResource(R.drawable.starrrrr);
                break;
        }


    }

    public int getItemCount() {
        return listaAvaliacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNome, txtAvaliacao, txtNumAvaliacao;
        private CircleImageView imgPerfil;
        private ImageView imgStar, imageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            txtNome = itemView.findViewById(R.id.textView73);
            txtNumAvaliacao = itemView.findViewById(R.id.textView74);
            txtAvaliacao = itemView.findViewById(R.id.textView68);
            imgPerfil = itemView.findViewById(R.id.circleImageViewListAvaliacoes);
            imgStar = itemView.findViewById(R.id.imageView99);
            imageView = itemView.findViewById(R.id.imageView100);
        }
    }



}
