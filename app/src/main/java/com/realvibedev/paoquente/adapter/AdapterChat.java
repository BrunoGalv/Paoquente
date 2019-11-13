package com.realvibedev.paoquente.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.model.ListChat;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder> {

    private List<ListChat> listChats;
    private Boolean aux = true;

    public AdapterChat() {
    }

    public AdapterChat(List<ListChat> listChats) {
        this.listChats = listChats;
    }

    public AdapterChat(List<ListChat> listChats, Boolean aux) {
        this.listChats = listChats;
        this.aux = aux;
    }

    @Override
    public AdapterChat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(AdapterChat.MyViewHolder holder, int position) {

        final ListChat lista = listChats.get(position);
        holder.setIsRecyclable(false);
        if (lista.getAux() != null) {
            if (aux) {
                if (lista.getAux() == 1) {
                    holder.txtMsg.setText(lista.getMsg());
                    holder.relativeLayoutMsg.setVisibility(View.VISIBLE);
                } else if (lista.getAux() == 0) {
                    holder.txtMsgMe.setText(lista.getMsg());
                    holder.relativeLayoutMsgMe.setVisibility(View.VISIBLE);
                }
            }else {
                if (lista.getAux() == 0) {
                    holder.txtMsg.setText(lista.getMsg());
                    holder.relativeLayoutMsg.setVisibility(View.VISIBLE);
                } else if (lista.getAux() == 1) {
                    holder.txtMsgMe.setText(lista.getMsg());
                    holder.relativeLayoutMsgMe.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public int getItemCount() {
        return listChats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtMsg, txtMsgMe;
        private RelativeLayout relativeLayoutMsg, relativeLayoutMsgMe;

        public MyViewHolder(View itemView) {

            super(itemView);
            txtMsg = itemView.findViewById(R.id.textView139);
            txtMsgMe = itemView.findViewById(R.id.textView140);
            relativeLayoutMsg = itemView.findViewById(R.id.relativeLayout32);
            relativeLayoutMsgMe = itemView.findViewById(R.id.relativeLayout33);

        }
    }
}
