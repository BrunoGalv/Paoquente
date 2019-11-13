package com.realvibedev.paoquente;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.adapter.Adapter;
import com.realvibedev.paoquente.model.Padarias;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class TelaFavoritos extends Fragment {

    public TelaFavoritos() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Padarias> listaPadarias = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    private FirebaseUser user;
    private TextView txtLogar, txtLogin;
    private Location locationA = new Location("point A");
    private Location locationB = new Location("point B");
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tela_favoritos, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerFav);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        txtLogar = (TextView) rootView.findViewById(R.id.textView194);
        txtLogin = (TextView) rootView.findViewById(R.id.textView195);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new Adapter(listaPadarias);
        recyclerView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            progressBar.setVisibility(View.VISIBLE);
            txtLogar.setVisibility(View.INVISIBLE);
            txtLogin.setVisibility(View.INVISIBLE);

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    db.collection("users").document(user.getUid()).update("Token", instanceIdResult.getToken());
                }
            });


            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mFusedLocationClient!=null) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                locationB.set(location);
                            }
                        }
                    });
                }
            }

            db.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(final DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("Token")!=null) {
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                if (!documentSnapshot.getString("Token").equals(instanceIdResult.getToken())) {
                                    db.collection("users").document(user.getUid()).update("Token", instanceIdResult.getToken());
                                }
                            }
                        });
                    }
                }
            });

            db.collection("users").document(user.getUid()).collection("favoritos").document("favoritos").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.get("quantidade") != null){
                        for (int i = 1; i <= (documentSnapshot.getDouble("quantidade")); i++){
                            if(documentSnapshot.get("favoritos" + i) != null){
                                getInfoPadaria(documentSnapshot.getString("favoritos" + i));
                            }
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        txtLogar.setText("Nenhuma padaria favoritada");
                        txtLogar.setVisibility(View.VISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), R.string.erro, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            txtLogar.setVisibility(View.VISIBLE);
            txtLogin.setVisibility(View.VISIBLE);
        }

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TelaCadastro.class));
            }
        });

        return rootView;
    }

    private void getInfoPadaria(final String key){
        db.collection("listaInfo").document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){
                    Padarias padaria = new Padarias();
                    padaria.setKey(key);
                    padaria.setNomePadaria(documentSnapshot.getString("Nome"));
                    padaria.setAvaliacao(String.valueOf(documentSnapshot.get("avaliacao")));
                    padaria.setImagemPadaria(documentSnapshot.getString("imagem"));
                    padaria.setNumAvaliacoes(String.valueOf(documentSnapshot.get("review")));

                    if (documentSnapshot.getString("ultimaFornada") != null){
                        padaria.setUltimaFornada(documentSnapshot.getString("ultimaFornada"));
                    }

                    if (locationB != null && documentSnapshot.getGeoPoint("lat") != null) {
                        GeoPoint geoPoint = documentSnapshot.getGeoPoint("lat");
                        locationA.setLatitude(geoPoint.getLatitude());
                        locationA.setLongitude(geoPoint.getLongitude());

                        padaria.setKm((locationA.distanceTo(locationB)) / 1000);
                    }
                    listaPadarias.add(padaria);
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), R.string.erro, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
