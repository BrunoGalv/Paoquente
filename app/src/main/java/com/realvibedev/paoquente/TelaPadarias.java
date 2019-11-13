package com.realvibedev.paoquente;


import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realvibedev.paoquente.adapter.Adapter;
import com.realvibedev.paoquente.adapter.AdapterPromos;
import com.realvibedev.paoquente.model.ListPromos;
import com.realvibedev.paoquente.model.Padarias;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TelaPadarias extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    private List<Padarias> listaPadarias = new ArrayList<>();
    private List<Padarias> listaPadariasSearch = new ArrayList<>();
    private Adapter adapter;
    private List<ListPromos> listaPromos = new ArrayList<>();
    private List<ListPromos> listaPromosAux = new ArrayList<>();
    private List<ListPromos> listaPromosSearch = new ArrayList<>();
    private AdapterPromos adapter2;
    private FusedLocationProviderClient mFusedLocationClient;
    private String city;
    private Location locationA = new Location("point A");
    private Location locationB = new Location("point B");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    private TextView txtNumPadarias, txtNumPromos;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ImageView imgSlide, imgSlide2, imgSlide3, imgSlide4, imgSlide5;
    private int totalImages, auxImages;
    private ListenerRegistration registration, registration2;

    public TelaPadarias() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tela_padarias, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recycler2);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar6);
        txtNumPadarias = rootView.findViewById(R.id.textView);
        txtNumPromos = rootView.findViewById(R.id.textView143);
        imgSlide = rootView.findViewById(R.id.imageView67);
        imgSlide2 = rootView.findViewById(R.id.imageView108);
        imgSlide3 = rootView.findViewById(R.id.imageView122);
        imgSlide4 = rootView.findViewById(R.id.imageView69);
        imgSlide5 = rootView.findViewById(R.id.imageView130);

        auxImages = 0;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.container3);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new Adapter(listaPadarias);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);

        adapter2 = new AdapterPromos(listaPromos, 1);
        recyclerView2.setAdapter(adapter2);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mFusedLocationClient!=null) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            locationB.set(location);
                            city = getCity(location.getLatitude(), location.getLongitude());
                            city = "Rio de Janeiro";
                            if (city!=null){
                                progressBar.setVisibility(View.VISIBLE);
                                updateList();
                                updateList2();
                            }
                        }
                    }
                });
            }
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        imgSlide.setImageResource(R.drawable.ballimagesliderfull);
                        imgSlide2.setImageResource(R.drawable.ballimageslider);
                        imgSlide3.setImageResource(R.drawable.ballimageslider);
                        imgSlide4.setImageResource(R.drawable.ballimageslider);
                        imgSlide5.setImageResource(R.drawable.ballimageslider);
                        break;
                    case 1:
                        imgSlide.setImageResource(R.drawable.ballimageslider);
                        imgSlide2.setImageResource(R.drawable.ballimagesliderfull);
                        imgSlide3.setImageResource(R.drawable.ballimageslider);
                        imgSlide4.setImageResource(R.drawable.ballimageslider);
                        imgSlide5.setImageResource(R.drawable.ballimageslider);
                        break;
                    case 2:
                        imgSlide.setImageResource(R.drawable.ballimageslider);
                        imgSlide2.setImageResource(R.drawable.ballimageslider);
                        imgSlide3.setImageResource(R.drawable.ballimagesliderfull);
                        imgSlide4.setImageResource(R.drawable.ballimageslider);
                        imgSlide5.setImageResource(R.drawable.ballimageslider);
                        break;
                    case 3:
                        imgSlide.setImageResource(R.drawable.ballimageslider);
                        imgSlide2.setImageResource(R.drawable.ballimageslider);
                        imgSlide3.setImageResource(R.drawable.ballimageslider);
                        imgSlide4.setImageResource(R.drawable.ballimageslider);
                        imgSlide5.setImageResource(R.drawable.ballimagesliderfull);
                        break;
                    case 4:
                        imgSlide.setImageResource(R.drawable.ballimageslider);
                        imgSlide2.setImageResource(R.drawable.ballimageslider);
                        imgSlide3.setImageResource(R.drawable.ballimageslider);
                        imgSlide4.setImageResource(R.drawable.ballimageslider);
                        imgSlide5.setImageResource(R.drawable.ballimagesliderfull);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        imgSlide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        imgSlide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });

        imgSlide4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(3);
            }
        });

        imgSlide5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(4);
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        if (auxImages == totalImages){
                            auxImages = 0;
                        }else {
                            auxImages += 1;
                        }
                        mViewPager.setCurrentItem(auxImages);
                    }
                });
            }
        }, 5000, 4000);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ImageSlider f = new ImageSlider();
                    // Supply index input as an argument.
                    Bundle args = new Bundle();
                    args.putString("url", listaPromosAux.get(0).getImagem());
                    args.putString("promo", listaPromosAux.get(0).getTxtPromo());
                    args.putString("promo2", listaPromosAux.get(0).getTxtPromo2());
                    args.putString("nomePadaria", listaPromosAux.get(0).getNomePadaria());
                    args.putString("desc", listaPromosAux.get(0).getDescricao());
                    args.putString("preco", listaPromosAux.get(0).getPreco());
                    args.putString("keyPd", listaPromosAux.get(0).getKeyPd());
                    f.setArguments(args);
                    return f;
                case 1:
                    ImageSlider f1 = new ImageSlider();
                    // Supply index input as an argument.
                    Bundle args1 = new Bundle();
                    args1.putString("url", listaPromosAux.get(1).getImagem());
                    args1.putString("promo", listaPromosAux.get(1).getTxtPromo());
                    args1.putString("promo2", listaPromosAux.get(1).getTxtPromo2());
                    args1.putString("nomePadaria", listaPromosAux.get(1).getNomePadaria());
                    args1.putString("desc", listaPromosAux.get(1).getDescricao());
                    args1.putString("preco", listaPromosAux.get(1).getPreco());
                    args1.putString("keyPd", listaPromosAux.get(1).getKeyPd());
                    f1.setArguments(args1);
                    return f1;
                case 2:
                    ImageSlider f2 = new ImageSlider();
                    // Supply index input as an argument.
                    Bundle args2 = new Bundle();
                    args2.putString("url", listaPromosAux.get(2).getImagem());
                    args2.putString("promo", listaPromosAux.get(2).getTxtPromo());
                    args2.putString("promo2", listaPromosAux.get(2).getTxtPromo2());
                    args2.putString("nomePadaria", listaPromosAux.get(2).getNomePadaria());
                    args2.putString("desc", listaPromosAux.get(2).getDescricao());
                    args2.putString("preco", listaPromosAux.get(2).getPreco());
                    args2.putString("keyPd", listaPromosAux.get(2).getKeyPd());
                    f2.setArguments(args2);
                    return f2;
                case 3:
                    ImageSlider f3 = new ImageSlider();
                    // Supply index input as an argument.
                    Bundle args3 = new Bundle();
                    args3.putString("url", listaPromosAux.get(3).getImagem());
                    args3.putString("promo", listaPromosAux.get(3).getTxtPromo());
                    args3.putString("promo2", listaPromosAux.get(3).getTxtPromo2());
                    args3.putString("nomePadaria", listaPromosAux.get(3).getNomePadaria());
                    args3.putString("desc", listaPromosAux.get(3).getDescricao());
                    args3.putString("preco", listaPromosAux.get(3).getPreco());
                    args3.putString("keyPd", listaPromosAux.get(3).getKeyPd());
                    f3.setArguments(args3);
                    return f3;
                case 4:
                    ImageSlider f4 = new ImageSlider();
                    // Supply index input as an argument.
                    Bundle args4 = new Bundle();
                    args4.putString("url", listaPromosAux.get(4).getImagem());
                    args4.putString("promo", listaPromosAux.get(4).getTxtPromo());
                    args4.putString("promo2", listaPromosAux.get(4).getTxtPromo2());
                    args4.putString("nomePadaria", listaPromosAux.get(4).getNomePadaria());
                    args4.putString("desc", listaPromosAux.get(4).getDescricao());
                    args4.putString("preco", listaPromosAux.get(4).getPreco());
                    args4.putString("keyPd", listaPromosAux.get(4).getKeyPd());
                    f4.setArguments(args4);
                    return f4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show X total pages.
            return totalImages;
        }
    }

    private void updateList(){
        registration = db.collection("listaPadarias").document(city).collection(city).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    Padarias padaria = dc.getDocument().toObject(Padarias.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            locationA.setLatitude(padaria.getLat());
                            locationA.setLongitude(padaria.getLongi());
                            float km = locationA.distanceTo(locationB) / 1000;

                            if (500 >= km) {
                                padaria.setKey(dc.getDocument().getId());
                                padaria.setKm(km);
                                listaPadarias.add(padaria);
                                adapter.notifyDataSetChanged();
                            }

                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            index = getItemIndex((dc.getDocument().getId()));
                            padaria.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listaPadarias.set(index, padaria);
                                adapter.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex((dc.getDocument().getId()));

                            if (index != -1) {
                                listaPadarias.remove(index);
                                adapter.notifyItemRemoved(index);
                            }
                            break;
                    }
                }
                if (listaPadarias.size()==0){
                    txtNumPadarias.setText("Nenhuma padaria próxima");
                }
            }
        });
    }

    private void updateList2(){
        registration2 = db.collection("promos").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERRO", "Listen failed.", e);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    ListPromos promo = dc.getDocument().toObject(ListPromos.class);
                    int index;
                    switch (dc.getType()) {
                        case ADDED:
                            locationA.setLatitude(promo.getLat());
                            locationA.setLongitude(promo.getLongi());
                            float km = locationA.distanceTo(locationB) / 1000;
                            promo.setKm(km);

                            if (500 >= km) {
                                if (promo.getTxtPromo() == null) {
                                    promo.setKey(dc.getDocument().getId());
                                    listaPromos.add(promo);
                                    adapter2.notifyDataSetChanged();
                                }else {
                                    promo.setKey(dc.getDocument().getId());
                                    listaPromosAux.add(promo);
                                }
                            }

                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            index = getItemIndex2((dc.getDocument().getId()));
                            promo.setKey(dc.getDocument().getId());

                            if (index != -1) {
                                listaPromos.set(index, promo);
                                adapter2.notifyItemChanged(index);
                            }
                            break;
                        case REMOVED:
                            index = getItemIndex2((dc.getDocument().getId()));

                            if (index != -1) {
                                listaPromos.remove(index);
                                adapter2.notifyItemRemoved(index);
                            }
                            break;
                    }
                }
                if (listaPromos.size()==0){
                    //txtNumPromos.setText("Nenhuma promoção próxima");
                    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.container3);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    int sizeInDP = 10;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                                    .getDisplayMetrics());
                    params.setMargins(0, marginInDp, 0 ,0);
                    txtNumPadarias.setLayoutParams(params);
                    txtNumPromos.setVisibility(View.INVISIBLE);
                    recyclerView2.setVisibility(View.INVISIBLE);
                }
                if (listaPromosAux.size() > 5){
                    totalImages = 5;
                }else {
                    totalImages = listaPromosAux.size();
                }
                if (totalImages == 0){
                    totalImages = 1;
                    ListPromos promoSEMPROMO = new ListPromos();
                    promoSEMPROMO.setImagem("https://firebasestorage.googleapis.com/v0/b/paoquente-bce48.appspot.com/o/aux%2Fsempromo.jpeg?alt=media&token=e7b134cd-74ed-4e60-a0d6-d93779de4326");
                    promoSEMPROMO.setTxtPromo("Sem promoção ativa");
                    promoSEMPROMO.setKeyPd("X");
                    listaPromosAux.add(promoSEMPROMO);
                }
                Collections.sort(listaPromosAux, new Comparator<ListPromos>() {
                    @Override
                    public int compare(ListPromos listPromos, ListPromos t1) {
                        float key1 = listPromos.getKm();
                        float key2 = t1.getKm();
                        return Double.compare(key1, key2);
                    }
                });
                makeVisibleIndicators();
                mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        });
    }

    private int getItemIndex(String Key){

        int index = -1;

        for (int i = 0; i < listaPadarias.size(); i++){
            if (listaPadarias.get(i).getKey().equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    private int getItemIndex2(String Key){

        int index = -1;

        for (int i = 0; i < listaPromos.size(); i++){
            if (listaPromos.get(i).getKey().equals(Key)){
                index = i;
                break;
            }
        }
        return index;
    }

    public String getCity(Double lat, Double longi){

        String city = null;

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            city = addresses.get(0).getAdminArea();
        }

        return city;
    }

    private void makeVisibleIndicators(){
        switch (totalImages){
            case 2:
                imgSlide2.setVisibility(View.VISIBLE);
                break;
            case 3:
                imgSlide2.setVisibility(View.VISIBLE);
                imgSlide3.setVisibility(View.VISIBLE);
                break;
            case 4:
                imgSlide2.setVisibility(View.VISIBLE);
                imgSlide3.setVisibility(View.VISIBLE);
                imgSlide4.setVisibility(View.VISIBLE);
                break;
            default:
                if (totalImages > 4) {
                    imgSlide2.setVisibility(View.VISIBLE);
                    imgSlide3.setVisibility(View.VISIBLE);
                    imgSlide4.setVisibility(View.VISIBLE);
                    imgSlide5.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    public void search(String stringSearch){

        if (recyclerView!=null) {
            if (stringSearch == null) {
                adapter = new Adapter(listaPadarias);
                recyclerView.setAdapter(adapter);

                adapter2 = new AdapterPromos(listaPromos, 1);
                recyclerView2.setAdapter(adapter2);
            } else {

                listaPadariasSearch.clear();

                adapter = new Adapter(listaPadariasSearch);
                recyclerView.setAdapter(adapter);

                for (int i = 0; i < listaPadarias.size(); i++) {
                    if (listaPadarias.get(i).getNomePadaria().toLowerCase().contains(stringSearch.toLowerCase())) {
                        listaPadariasSearch.add(listaPadarias.get(i));
                        adapter.notifyDataSetChanged();
                    }
                }


                listaPromosSearch.clear();

                adapter2 = new AdapterPromos(listaPromosSearch);
                recyclerView2.setAdapter(adapter2);

                for (int i = 0; i < listaPromos.size(); i++) {
                    if (listaPromos.get(i).getNomePadaria().toLowerCase().contains(stringSearch.toLowerCase())) {
                        listaPromosSearch.add(listaPromos.get(i));
                        adapter2.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (registration != null && registration != null) {
            registration.remove();
            registration2.remove();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (registration != null && registration != null) {
            registration.remove();
            registration2.remove();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (registration != null && registration != null) {
            registration.remove();
            registration2.remove();
        }
        super.onStop();
    }
}
