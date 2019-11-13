package com.realvibedev.paoquente;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSlider extends Fragment {

    private String url, promo, promo2;
    private ImageView img;
    private TextView txtPromo, txtPromo2;


    public ImageSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_image_slider, container, false);

        img = rootView.findViewById(R.id.imageView21);
        txtPromo = rootView.findViewById(R.id.textView156);
        txtPromo2 = rootView.findViewById(R.id.textView155);

        final Bundle args = getArguments();
        url = args.getString("url");
        promo = args.getString("promo");
        promo2 = args.getString("promo2");

        txtPromo.setText(promo);
        txtPromo2.setText(promo2);

        Glide.with(getContext()).load(url).apply(new RequestOptions()
                .centerCrop()
        ).into(img);

        if (!args.getString("keyPd").equals("X")) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TelaPromocao.class);
                    intent.putExtra("nomePromo", promo);
                    intent.putExtra("nomePadaria", args.getString("nomePadaria"));
                    intent.putExtra("desc", args.getString("desc"));
                    intent.putExtra("preco", args.getString("preco"));
                    intent.putExtra("imagem", url);
                    intent.putExtra("Key", args.getString("keyPd"));
                    view.getContext().startActivity(intent);
                }
            });
        }

        return rootView;
    }

}
