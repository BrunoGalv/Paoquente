package com.realvibedev.paoquente.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.TelaChatCliente;
import com.realvibedev.paoquente.model.ListMensagensCliente;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMensagensCliente extends RecyclerView.Adapter<AdapterMensagensCliente.MyViewHolder> {
    private List<ListMensagensCliente> listMensagenses;

    public AdapterMensagensCliente(List<ListMensagensCliente> listMensagenses) {
        this.listMensagenses = listMensagenses;
    }


    @Override
    public AdapterMensagensCliente.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagens_cliente, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(AdapterMensagensCliente.MyViewHolder holder, int position) {

        final ListMensagensCliente lista = listMensagenses.get(position);
        holder.txtNome.setText(lista.getNome());
        holder.txtUltMens.setText(lista.getUltMsg());
        Picasso.get().load(lista.getImagem()).into(holder.circleImageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TelaChatCliente.class);
                intent.putExtra("Key", String.valueOf(lista.getKey()));
                intent.putExtra("Nome", String.valueOf(lista.getNome()));
                view.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return listMensagenses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNome, txtUltMens;
        private CircleImageView circleImageView;
        private RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {

            super(itemView);
            txtNome = itemView.findViewById(R.id.textView178);
            txtUltMens = itemView.findViewById(R.id.textView179);
            circleImageView = itemView.findViewById(R.id.circleImageViewMensagens);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutMensagem);
        }
    }
}
