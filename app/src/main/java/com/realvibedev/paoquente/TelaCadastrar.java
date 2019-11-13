package com.realvibedev.paoquente;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.realvibedev.paoquente.masks.CpfCnpjMaks;
import com.realvibedev.paoquente.masks.ValidaCPF;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelaCadastrar extends AppCompatActivity implements PurchasesUpdatedListener {

    private static final int REQUEST_PLACE_PICKER = 1;
    private Intent intent;
    private int aux;
    private ImageView imgBack, imgLoading;
    private GeoPoint geoPoint;
    private String state, endereco, nomePlano, token;
    private TextView txtEndereco;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private Button btnCadastrar;
    private BillingClient mBillingClient;
    private EditText edtEmail, edtNome, edtSenha, edtTelefone, edtCnpj;
    private Map<String, Object> newBakery , newUser, newListaInfo;
    private boolean auxPagamento;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastrar);

        txtEndereco = findViewById(R.id.textView170);
        btnCadastrar = findViewById(R.id.button39);
        imgBack = findViewById(R.id.imageView131);
        edtNome = (EditText) findViewById(R.id.etNomeSalaoPlano);
        edtEmail = (EditText) findViewById(R.id.etEmailSalaoPlano);
        edtSenha = (EditText) findViewById(R.id.etSenhaSalaoPlano);
        edtTelefone = (EditText) findViewById(R.id.etTelefoneSalaoPlano);
        edtCnpj = (EditText) findViewById(R.id.etCnpjSalaoPlano);
        imgLoading = findViewById(R.id.imageView172);
        progressBar = findViewById(R.id.progressBar19);

        edtCnpj.addTextChangedListener(CpfCnpjMaks.insert(edtCnpj));

        intent = getIntent();

        aux = intent.getIntExtra("aux", 0);

        nomePlano = "assinatura.1.0";

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( TelaCadastrar.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                token = "A";
            }
        });


        mBillingClient = BillingClient.newBuilder(TelaCadastrar.this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                mBillingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                        if (billingResponseCode == BillingClient.BillingResponse.OK) {
                            // The billing client is ready. You can query purchases here.
                        }
                    }
                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                });
            }
        });


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean auxCPF;

                if (ValidaCPF.isCPF(CpfCnpjMaks.unmask(edtCnpj.getText().toString())) && ((CpfCnpjMaks.unmask(edtCnpj.getText().toString())).length() == 11)) {
                    auxCPF = true;
                }else auxCPF = (CpfCnpjMaks.unmask(edtCnpj.getText().toString())).length() == 14;


                if (mBillingClient!=null) {
                    if (!edtEmail.getText().toString().equals("")) {
                        if (!edtSenha.getText().toString().equals("")) {
                            if (!edtNome.getText().toString().equals("")) {
                                if (!edtTelefone.getText().toString().equals("")) {
                                    if (!edtCnpj.getText().toString().equals("")) {
                                        if (auxCPF) {
                                            if (geoPoint != null && state != null) {
                                                if (edtSenha.getText().toString().length() >= 6) {
                                                    if (isEmailValid(edtEmail.getText().toString())) {
                                                        newBakery = new HashMap<>();
                                                        newBakery.put("nomePadaria", edtNome.getText().toString());
                                                        newBakery.put("telefone", edtTelefone.getText().toString());
                                                        newBakery.put("CNPJ", 1);
                                                        newBakery.put("imagemPadaria", "https://firebasestorage.googleapis.com/v0/b/paoquente-bce48.appspot.com/o/logoPaoQuente3.png?alt=media&token=5bef782c-3ee1-45e7-8138-f1b8497f29dd");
                                                        newBakery.put("Descricao", "Padaria nova!");
                                                        newBakery.put("Dinheiro", true);
                                                        newBakery.put("avaliacao", "5,0");
                                                        newBakery.put("numAvaliacoes", "0");
                                                        newBakery.put("lat", geoPoint.getLatitude());
                                                        newBakery.put("longi", geoPoint.getLongitude());
                                                        newBakery.put("ultimaFornada", "Última fornada: 00:00");

                                                        newUser = new HashMap<>();
                                                        newUser.put("Nome", edtNome.getText().toString());
                                                        newUser.put("State", state);
                                                        newUser.put("lat", geoPoint.getLatitude());
                                                        newUser.put("longi", geoPoint.getLongitude());
                                                        newUser.put("numOrdem", 0);
                                                        newUser.put("Imagem", "https://firebasestorage.googleapis.com/v0/b/paoquente-bce48.appspot.com/o/logoPaoQuente3.png?alt=media&token=5bef782c-3ee1-45e7-8138-f1b8497f29dd");
                                                        newUser.put("Token", token);

                                                        newListaInfo = new HashMap<>();
                                                        newListaInfo.put("lat", geoPoint);
                                                        newListaInfo.put("imagem", "https://firebasestorage.googleapis.com/v0/b/paoquente-bce48.appspot.com/o/logoPaoQuente3.png?alt=media&token=5bef782c-3ee1-45e7-8138-f1b8497f29dd");
                                                        newListaInfo.put("Dinheiro", true);
                                                        newListaInfo.put("avaliacao", 5);
                                                        newListaInfo.put("Nome", edtNome.getText().toString());
                                                        newListaInfo.put("review", 0);
                                                        newListaInfo.put("Token", token);

                                                        if (aux == 1 && !auxPagamento) {
                                                            newUser.put("Premium", true);
                                                            newUser.put("dateSubs", FieldValue.serverTimestamp());
                                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                                    .setSku(nomePlano)
                                                                    .setType(BillingClient.SkuType.SUBS) // SkuType.SUB for subscription
                                                                    .build();
                                                            mBillingClient.launchBillingFlow(TelaCadastrar.this, flowParams);
                                                        } else {
                                                            progressBar.setVisibility(View.VISIBLE);
                                                            progressBar.setVisibility(View.VISIBLE);

                                                            newUser.put("Premium", false);
                                                            firebaseAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (!task.isSuccessful()) {
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        Toast.makeText(TelaCadastrar.this, getString(R.string.erro), Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                        if (user != null) {
                                                                            WriteBatch batch = db.batch();
                                                                            batch.set(db.collection("listaPadarias").document(state).collection(state).document(user.getUid()), newBakery);
                                                                            batch.set(db.collection("usersPro").document(user.getUid()), newUser);
                                                                            batch.set(db.collection("listaInfo").document(user.getUid()), newListaInfo);
                                                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    startActivity(new Intent(TelaCadastrar.this, MainActivity.class));
                                                                                    finishAffinity();
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        Toast.makeText(TelaCadastrar.this, getString(R.string.digiteEmailValido), Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(TelaCadastrar.this, getString(R.string.senhaPequena), Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(TelaCadastrar.this, "Defina o local do estabelecimento", Toast.LENGTH_LONG).show();
                                            }
                                        }else {
                                            Toast.makeText(TelaCadastrar.this, "CPF ou CNPJ inválido", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(TelaCadastrar.this, "Preencha o CNPJ ou CPF do Responsável", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(TelaCadastrar.this, getString(R.string.preenchaTelefone), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(TelaCadastrar.this, getString(R.string.preenchaNome), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(TelaCadastrar.this, getString(R.string.preenchaoSenha), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(TelaCadastrar.this, getString(R.string.preenchaoEmail), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaCadastrar.super.onBackPressed();
            }
        });

    }

    public void onPickButtonClick(View v) {

        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {


            Place place = PlaceAutocomplete.getPlace(this, data);

            if (place != null) {
                LatLng latLng = place.getLatLng();
                geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
                state = getCity(latLng.latitude, latLng.longitude);
                String adrress[] = place.getAddress().toString().split(",");
                if (adrress[0].equals("")){
                    endereco = place.getName().toString();
                }else {
                    if (adrress.length>=2) {
                        endereco = adrress[0] + "," + adrress[1];
                    }else {
                        endereco = adrress[0];
                    }
                }
                txtEndereco.setText(endereco);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public String getCity(Double lat, Double longi){

        String city = null;

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                auxPagamento = true;
                newUser.put("purchaseInfo", purchase.getOriginalJson());
                newUser.put("orderId", purchase.getOrderId());
                firebaseAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(TelaCadastrar.this, getString(R.string.erro), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                WriteBatch batch = db.batch();
                                batch.set(db.collection("listaPadarias").document(state).collection(state).document(user.getUid()), newBakery);
                                batch.set( db.collection("usersPro").document(user.getUid()), newUser);
                                batch.set(db.collection("listaInfo").document(user.getUid()), newListaInfo);
                                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(TelaCadastrar.this, MainActivity.class));
                                        finishAffinity();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(this, getString(R.string.canceladoo), Toast.LENGTH_SHORT).show();
        } else {
            // Handle any other error codes.
            Toast.makeText(this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
        }
    }
}
