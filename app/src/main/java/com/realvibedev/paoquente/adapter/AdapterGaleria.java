package com.realvibedev.paoquente.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.realvibedev.paoquente.FragmentsPadaria.FragmentGaleria;
import com.realvibedev.paoquente.Padaria.TelaGaleriaPadaria;
import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.model.ListGaleria;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by bruno on 24/05/2018.
 */

public class AdapterGaleria extends RecyclerView.Adapter<AdapterGaleria.MyViewHolder> {

    private List<ListGaleria> listaGalerias;
    private FragmentGaleria fragmentGaleria;

    public AdapterGaleria(List<ListGaleria> listaGalerias) {
        this.listaGalerias = listaGalerias;
    }

    public AdapterGaleria(List<ListGaleria> listaGalerias, FragmentGaleria fragmentGaleria) {
        this.listaGalerias = listaGalerias;
        this.fragmentGaleria = fragmentGaleria;
    }

    public AdapterGaleria.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_galeria, parent, false);
        return new MyViewHolder(itemLista);
    }

    public void onBindViewHolder(final AdapterGaleria.MyViewHolder holder, int position) {

        final ListGaleria lista = listaGalerias.get(position);
        Glide.with(holder.imageView.getContext()).load(lista.getImagem()).apply(new RequestOptions()
                .centerCrop()
        ).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentGaleria!=null) {
                    (fragmentGaleria).imagem(lista.getImagem());
                }else {
                    ((TelaGaleriaPadaria) view.getContext()).imagem(lista.getImagem());
                }
            }
        });
        if (fragmentGaleria==null){
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((TelaGaleriaPadaria)view.getContext()).apagarFt(lista.getImagem(), lista.getKey());
                    return false;
                }
            });
        }
    }

    public int getItemCount() {
        return listaGalerias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageView92);
        }
    }


}
