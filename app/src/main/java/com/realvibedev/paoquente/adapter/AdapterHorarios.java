package com.realvibedev.paoquente.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.model.ListHorarios;

import java.util.List;

/**
 * Created by bruno on 20/10/18.
 */

public class AdapterHorarios extends RecyclerView.Adapter<AdapterHorarios.MyViewHolder> {


    private List<ListHorarios> listaHorarios;

    public AdapterHorarios() {
    }

    public AdapterHorarios(List<ListHorarios> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    public AdapterHorarios.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_horarios, parent, false);
        return new MyViewHolder(itemLista);
    }

    public void onBindViewHolder(AdapterHorarios.MyViewHolder holder, int position) {

        final ListHorarios lista = listaHorarios.get(position);
        switch (lista.getKey()){
            case "1":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.segundafeira));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
            case "2":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.tercafeira));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
            case "3":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.quartafeira));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
            case "4":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.quintafeira));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
            case "5":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.sextafeira));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
            case "6":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.sabado));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
            case "7":
                holder.txtDia.setText(holder.txtDia.getResources().getString(R.string.domingo));
                if (!lista.getHorario().equals("nao")) {
                    holder.txthorario.setText(lista.getHorario());
                } else {
                    holder.txthorario.setText(holder.txtDia.getResources().getString(R.string.naoabre));
                }
                break;
        }


    }

    public int getItemCount() {
        return listaHorarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txthorario, txtDia;

        public MyViewHolder(View itemView) {

            super(itemView);
            txtDia = itemView.findViewById(R.id.textView70);
            txthorario = itemView.findViewById(R.id.textView69);
        }
    }
}
