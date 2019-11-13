package com.realvibedev.paoquente.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.model.ListTelefone;

import java.util.List;

/**
 * Created by bruno on 20/10/18.
 */

public class AdapterListaTelefone extends RecyclerView.Adapter<AdapterListaTelefone.MyViewHolder> {
    private List<ListTelefone> listaTelefones;
    private Activity activity;

    public AdapterListaTelefone() {
    }

    public AdapterListaTelefone(List<ListTelefone> listaTelefones) {
        this.listaTelefones = listaTelefones;
    }

    public AdapterListaTelefone(List<ListTelefone> listaTelefones, Activity activity) {
        this.listaTelefones = listaTelefones;
        this.activity = activity;
    }

    public AdapterListaTelefone.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_telefone, parent, false);
        return new MyViewHolder(itemLista);
    }


    public void onBindViewHolder(AdapterListaTelefone.MyViewHolder holder, int position) {

        final ListTelefone lista = listaTelefones.get(position);
        if (lista.getNumeroTelefone().contains("W")){
            holder.texto.setText("Whatsapp");
            holder.numero.setText(lista.getNumeroTelefone().substring(1));
        } else{
            holder.texto.setText("Telefone" + lista.getKey() + ":");
            holder.numero.setText(lista.getNumeroTelefone());
        }

        holder.numero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getNumeroTelefone().contains("W")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + lista.getNumeroTelefone().substring(1)));
                    view.getContext().startActivity(intent);
                } else {
                    if (ContextCompat.checkSelfPermission(view.getContext(),
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                Manifest.permission.CALL_PHONE)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + lista.getNumeroTelefone()));
                        view.getContext().startActivity(callIntent);
                    }
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getNumeroTelefone().contains("W")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + lista.getNumeroTelefone().substring(1)));
                    view.getContext().startActivity(intent);
                } else {
                    if (ContextCompat.checkSelfPermission(view.getContext(),
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                Manifest.permission.CALL_PHONE)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + lista.getNumeroTelefone()));
                        view.getContext().startActivity(callIntent);
                    }
                }
            }
        });
    }

    public int getItemCount() {
        return listaTelefones.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView texto, numero;
        private ImageView imageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            texto = itemView.findViewById(R.id.textView84);
            numero = itemView.findViewById(R.id.textView83);
            imageView = itemView.findViewById(R.id.imageView83);
        }
    }
}
