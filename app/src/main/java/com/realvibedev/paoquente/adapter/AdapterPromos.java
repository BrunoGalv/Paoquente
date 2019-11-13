package com.realvibedev.paoquente.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realvibedev.paoquente.Padaria.TelaPromosPadaria;
import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.TelaPromocao;
import com.realvibedev.paoquente.model.ListPromos;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Meu computador on 28/02/2019.
 */

public class AdapterPromos extends RecyclerView.Adapter<AdapterPromos.MyViewHolder>  {

    private List<ListPromos> listaPromos;
    private int aux = 0;

    public AdapterPromos(List<ListPromos> listaPromos) {
        this.listaPromos = listaPromos;
    }

    public AdapterPromos(List<ListPromos> listaPromos, int aux) {
        this.listaPromos = listaPromos;
        this.aux = aux;
    }

    public AdapterPromos.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_promos, parent, false);
        return new MyViewHolder(itemLista);
    }

    public void onBindViewHolder(final AdapterPromos.MyViewHolder holder, int position) {

        final ListPromos lista = listaPromos.get(position);
        Glide.with(holder.imageView.getContext()).load(lista.getImagem()).apply(new RequestOptions()
                .centerCrop()
        ).into(holder.imageView);

        holder.txtNome.setText(lista.getNome());
        holder.txtMoney.setText(lista.getPreco());
        holder.txtNomePadaria.setText(lista.getNomePadaria());

        if (lista.getTxtPromo() != null){
            holder.txtNome.setText(lista.getTxtPromo());
        }

        if (aux == 0 || aux == 2){
            holder.cardView.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getTxtPromo() == null) {
                    switch (aux) {
                        case 0:
                            ((TelaPromosPadaria) view.getContext()).callDialog(lista.getNome(), lista.getDescricao(), lista.getPreco(), lista.getImagem(), lista.getKey());
                            break;
                        case 1:
                            Intent intent = new Intent(view.getContext(), TelaPromocao.class);
                            intent.putExtra("nomePromo", lista.getNome());
                            intent.putExtra("nomePadaria", lista.getNomePadaria());
                            intent.putExtra("desc", lista.getDescricao());
                            intent.putExtra("preco", lista.getPreco());
                            intent.putExtra("imagem", lista.getImagem());
                            intent.putExtra("Key", lista.getKeyPd());
                            view.getContext().startActivity(intent);
                            break;
                        case 2:
                            Intent intent2 = new Intent(view.getContext(), TelaPromocao.class);
                            intent2.putExtra("nomePromo", lista.getNome());
                            intent2.putExtra("nomePadaria", lista.getNomePadaria());
                            intent2.putExtra("desc", lista.getDescricao());
                            intent2.putExtra("preco", lista.getPreco());
                            intent2.putExtra("imagem", lista.getImagem());
                            intent2.putExtra("Key", lista.getKeyPd());
                            intent2.putExtra("aux", true);
                            view.getContext().startActivity(intent2);
                            break;
                    }
                }else{
                    switch (aux) {
                        case 0:
                            ((TelaPromosPadaria) view.getContext()).callDialogEspecial(lista.getTxtPromo(), lista.getTxtPromo2(), lista.getDescricao(), lista.getPreco(), lista.getImagem(), lista.getKey());
                            break;
                        case 1:
                            Intent intent = new Intent(view.getContext(), TelaPromocao.class);
                            intent.putExtra("nomePromo", lista.getTxtPromo());
                            intent.putExtra("nomePadaria", lista.getNomePadaria());
                            intent.putExtra("desc", lista.getDescricao());
                            intent.putExtra("preco", lista.getPreco());
                            intent.putExtra("imagem", lista.getImagem());
                            intent.putExtra("Key", lista.getKeyPd());
                            view.getContext().startActivity(intent);
                            break;
                        case 2:
                            Intent intent2 = new Intent(view.getContext(), TelaPromocao.class);
                            intent2.putExtra("nomePromo", lista.getTxtPromo());
                            intent2.putExtra("nomePadaria", lista.getNomePadaria());
                            intent2.putExtra("desc", lista.getDescricao());
                            intent2.putExtra("preco", lista.getPreco());
                            intent2.putExtra("imagem", lista.getImagem());
                            intent2.putExtra("Key", lista.getKeyPd());
                            intent2.putExtra("aux", true);
                            view.getContext().startActivity(intent2);
                            break;
                    }
                }
            }
        });

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (aux == 0){
                    ((TelaPromosPadaria) view.getContext()).deletePromo(lista.getKey());
                }
                return false;
            }
        });
    }

    public int getItemCount() {
        return listaPromos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private RelativeLayout relativeLayout;
        private TextView txtNome, txtMoney, txtNomePadaria;
        private CardView cardView;

        public MyViewHolder(View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageView66);
            relativeLayout = itemView.findViewById(R.id.relativeLayout23);
            txtNome = itemView.findViewById(R.id.textView130);
            txtNomePadaria = itemView.findViewById(R.id.textView131);
            txtMoney = itemView.findViewById(R.id.textView132);
            cardView = itemView.findViewById(R.id.cardViewPromos);
        }
    }
}
