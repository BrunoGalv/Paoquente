package com.realvibedev.paoquente.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.Padaria.TelaMensagensPadaria;
import com.realvibedev.paoquente.Padaria.TelaChat;
import com.realvibedev.paoquente.model.ListMensagens;

import java.util.List;

/**
 * Created by Meu computador on 13/03/2019.
 */

public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.MyViewHolder> {
    private List<ListMensagens> listMensagenses;

    public AdapterMensagens() {
    }

    public AdapterMensagens(List<ListMensagens> listMensagenses) {
        this.listMensagenses = listMensagenses;
    }


    @Override
    public AdapterMensagens.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_mensagens, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(AdapterMensagens.MyViewHolder holder, int position) {

        final ListMensagens lista = listMensagenses.get(position);
        holder.txtNome.setText(lista.getNome());
        holder.txtUltMens.setText(lista.getUltMsg());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TelaChat.class);
                intent.putExtra("Key", String.valueOf(lista.getKey()));
                view.getContext().startActivity(intent);
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((TelaMensagensPadaria)view.getContext()).apagarMensag(lista.getKey());
                return false;
            }
        });
    }

    public int getItemCount() {
        return listMensagenses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNome, txtUltMens;
        private RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {

            super(itemView);
            txtNome = itemView.findViewById(R.id.textView136);
            txtUltMens = itemView.findViewById(R.id.textView137);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutChat);
        }
    }
}
