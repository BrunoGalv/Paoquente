package com.realvibedev.paoquente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.realvibedev.paoquente.FragmentsPadaria.FragmentAvaliacoes;
import com.realvibedev.paoquente.FragmentsPadaria.FragmentGaleria;
import com.realvibedev.paoquente.FragmentsPadaria.FragmentInformacoes;
import com.realvibedev.paoquente.FragmentsPadaria.FragmentPromocoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class TelaPadaria extends AppCompatActivity {
    private TextView txtNomePadaria;
    private Intent intent;
    private BottomNavigationView navigation;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView imgBack, imgFavoritar, imgChat;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private String uid, nome, key;
    boolean aBoolean = true, premium = false;
    private int quantidadeFavoritos = 0, i2, aux;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_informacao:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_promocoes:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_galeria:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_star:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_padaria);

        txtNomePadaria = (TextView) findViewById(R.id.textView55);
        imgBack = findViewById(R.id.imageView82);
        imgChat = findViewById(R.id.imageView84);
        imgFavoritar = findViewById(R.id.imageView85);

        intent = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();

        navigation = (BottomNavigationView) findViewById(R.id.navigationPadaria);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.containerPadaria);
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
                        break;
                    case 1:
                        MenuItem menuItem1 = navigation.getMenu().getItem(1);
                        menuItem1.setChecked(true);
                        break;
                    case 2:
                        MenuItem menuItem2 = navigation.getMenu().getItem(2);
                        menuItem2.setChecked(true);
                        break;
                    case 3:
                        MenuItem menuItem3 = navigation.getMenu().getItem(3);
                        menuItem3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nome = intent.getStringExtra("Nome");
        key = intent.getStringExtra("Key");
        txtNomePadaria.setText(nome);

        if (key!=null){
            db.collection("usersPro").document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot!=null) {
                        premium = documentSnapshot.getBoolean("Premium");
                    }
                }
            });
        }


        if (user != null){
            uid = user.getUid();
            db.collection("users").document(uid).collection("favoritos").document("favoritos").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            quantidadeFavoritos = (document.getDouble("quantidade")).intValue();
                            for (int i = 1; i <= quantidadeFavoritos; i++) {
                                final int finalX = i;
                                if (document.getData().get("favoritos" + String.valueOf(i))!=null) {
                                    final String auxString = document.getString("favoritos" + String.valueOf(i));
                                    if (auxString.equals(key)){
                                        imgFavoritar.setImageResource(R.mipmap.favoritewhitefull);
                                        aBoolean = false;
                                        i2 = finalX;
                                    }
                                }
                            }
                        }
                    }
                }
            });

        }

        imgFavoritar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (user!=null) {
                    uid = user.getUid();
                    if (nome != null && !nome.isEmpty()) {
                        if (aBoolean) {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            alerta.setMessage("Deseja adicionar " + nome + " aos seus favoritos?");
                            alerta.setIcon(R.mipmap.ic_info);
                            alerta.setCancelable(true);
                            alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (quantidadeFavoritos == 0) {
                                        final Map<String, Object> newData = new HashMap<>();
                                        newData.put("favoritos1", key);
                                        newData.put("quantidade", 1);
                                        db.collection("users").document(uid).collection("favoritos").document("favoritos").set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseMessaging.getInstance().subscribeToTopic(key);
                                                imgFavoritar.setImageResource(R.mipmap.favoritewhitefull);
                                                aBoolean = false;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TelaPadaria.this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        final DocumentReference sfDocRef = db.collection("users").document(uid).collection("favoritos").document("favoritos");
                                        db.runTransaction(new Transaction.Function<Void>() {
                                            @Override
                                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                                double newQuantidade = snapshot.getDouble("quantidade") + 1;
                                                transaction.update(sfDocRef, "quantidade", newQuantidade);
                                                transaction.update(sfDocRef, "favoritos" + (int) newQuantidade, key);
                                                // Success
                                                return null;
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseMessaging.getInstance().subscribeToTopic(key);
                                                imgFavoritar.setImageResource(R.mipmap.favoritewhitefull);
                                                aBoolean = false;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TelaPadaria.this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                            alerta.setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alertDialog = alerta.create();
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            alerta.setMessage("Deseja excluir " + nome + " dos seus favoritos?");
                            alerta.setIcon(R.mipmap.ic_info);
                            alerta.setCancelable(true);
                            alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("users").document(uid).collection("favoritos").document("favoritos").update("favoritos" + String.valueOf(i2), FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                                            startActivity(new Intent(TelaPadaria.this, TelaPrincipal.class));
                                            finishAffinity();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TelaPadaria.this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            alerta.setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alertDialog = alerta.create();
                            alertDialog.show();
                        }
                    }
                }else {
                    startActivity(new Intent(TelaPadaria.this, TelaCadastro.class));
                }
            }
        });

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (premium) {
                    if (user != null) {
                        Intent intent = new Intent(TelaPadaria.this, TelaChatCliente.class);
                        intent.putExtra("Key", key);
                        intent.putExtra("aux", 1);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(TelaPadaria.this, TelaCadastro.class));
                    }
                }else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(TelaPadaria.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Não é possível usar o Chat!");
                    alerta.setMessage("Infelizmente esta Padaria não possui o Chat, contacte a padaria para poderem liberar este recurso.");
                    //alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(false);
                    alerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaPadaria.super.onBackPressed();
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
                    return new FragmentInformacoes();
                case 1:
                    return new FragmentPromocoes();
                case 2:
                    return new FragmentGaleria();
                case 3:
                    return new FragmentAvaliacoes();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
