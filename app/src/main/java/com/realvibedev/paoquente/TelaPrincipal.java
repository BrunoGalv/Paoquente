package com.realvibedev.paoquente;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.Padaria.PainelAdministrativo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaPrincipal extends AppCompatActivity {
    private static final String NUMERODEPADARIASADICIONADAS = "NumerodePadariasAdicionadas";
    private BottomNavigationView navigation;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private android.widget.SearchView searchView;
    private TelaPadarias telaPadarias = new TelaPadarias();
    private TextView txtFavoritos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        ImageView btnConfig = (ImageView) findViewById(R.id.imageView);
        navigation = (BottomNavigationView) findViewById(R.id.navigationBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        searchView = findViewById(R.id.searchView);
        txtFavoritos = findViewById(R.id.textView157);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (user != null) {
                    db.collection("usersPro").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                startActivity(new Intent(TelaPrincipal.this, PainelAdministrativo.class));
                                finishAffinity();
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(TelaPrincipal.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
            }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        MenuItem menuItem = navigation.getMenu().getItem(0);
                        menuItem.setChecked(true);
                        searchView.setVisibility(View.VISIBLE);
                        txtFavoritos.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        MenuItem menuItem1 = navigation.getMenu().getItem(1);
                        menuItem1.setChecked(true);
                        searchView.setVisibility(View.INVISIBLE);
                        txtFavoritos.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container2);

        final TelaPadarias screen = (TelaPadarias) fragment;

        searchView.setQueryHint("Nome da padaria");
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(s != null && !s.isEmpty()){

                    telaPadarias.search(s);


                }else{
                    //if search text is null
                    //return default
                    telaPadarias.search(null);

                }
                return false;
            }
        });


        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaPrincipal.this, TelaConfig.class));
                overridePendingTransition(R.anim.your_left_to_right, R.anim.your_right_to_left);
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return telaPadarias;
                case 1:
                    return new TelaFavoritos();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_favoritos:
                    mViewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }

    };
}
