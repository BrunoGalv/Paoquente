package com.realvibedev.paoquente.Maps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by bruno on 23/05/2018.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Double lat, longi;
    private String name;
    private LatLng coordenada;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            lat = getArguments().getDouble("lat");
            longi = getArguments().getDouble("longi");
            name = getArguments().getString("nome");
            coordenada = new LatLng(lat, longi);
            getMapAsync(MapaFragment.this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (coordenada != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coordenada, 17);
            googleMap.moveCamera(update);
            MarkerOptions marcador = new MarkerOptions();
            marcador.position(coordenada);
            googleMap.addMarker(marcador);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String uri = "geo: " + lat + "," + longi + "?q=" + lat + "," + longi + " ("+name+")";
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri)));
                    return true;
                }
            });
        }
    }
}

