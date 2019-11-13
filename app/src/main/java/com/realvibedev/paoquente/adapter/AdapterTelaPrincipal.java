package com.realvibedev.paoquente.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.TelaPadaria;
import com.realvibedev.paoquente.model.PadariasTelaPrincipal;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bruno on 21/03/2018.
 */

public class AdapterTelaPrincipal extends RecyclerView.Adapter<AdapterTelaPrincipal.MyViewHolder> {

    private List<PadariasTelaPrincipal> listaPadarias;
    private Context context;

    public AdapterTelaPrincipal(List<PadariasTelaPrincipal> lista) {
        this.listaPadarias = lista;
    }

    public AdapterTelaPrincipal(Context context) {
        this.context = context;
    }

    public AdapterTelaPrincipal.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_tela_principal, parent, false);
        return new MyViewHolder(itemLista);
    }

    public void onBindViewHolder(AdapterTelaPrincipal.MyViewHolder holder, int position) {

        final PadariasTelaPrincipal padaria = listaPadarias.get(position);
        holder.nomePadaria.setText(padaria.getNomePadaria());
        holder.ultimaFornada.setText(padaria.getUltimaFornada());
        holder.local.setText(padaria.getLocal());
        Picasso.get().load(padaria.getImagemPadaria()).into(holder.imgPadaria);
        holder.btnSaibaMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TelaPadaria.class);
                intent.putExtra("Nome", padaria.getNomePadaria());
                intent.putExtra("Fornada", padaria.getUltimaFornada());
                intent.putExtra("Imagem", padaria.getImagemPadaria());
                intent.putExtra("ImagemMapa", padaria.getImagemMapa());
                intent.putExtra("Key", padaria.getKey());
                view.getContext().startActivity(intent);
            }
        });


    }

    public int getItemCount() {
        return listaPadarias.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nomePadaria, ultimaFornada, local;
        private ImageView imgPadaria;
        private Button btnSaibaMais;

        public MyViewHolder(View itemView) {


            super(itemView);
            nomePadaria = itemView.findViewById(R.id.textView9);
            ultimaFornada = itemView.findViewById(R.id.textView11);
            imgPadaria = itemView.findViewById(R.id.imageView24);
            btnSaibaMais = itemView.findViewById(R.id.button9);
            local = itemView.findViewById(R.id.textView10);
        }
    }
}
