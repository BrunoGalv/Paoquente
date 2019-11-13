package com.realvibedev.paoquente.Padaria;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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
import com.realvibedev.paoquente.Common;
import com.realvibedev.paoquente.R;
import com.realvibedev.paoquente.Remote.APIService;
import com.realvibedev.paoquente.model.Data;
import com.realvibedev.paoquente.model.MyResponse;
import com.realvibedev.paoquente.model.Sender;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PainelAdministrativo extends AppCompatActivity implements PurchasesUpdatedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView txtQuantPromo, txtFornada, txtFornada2, txtFornada3;
    private Button btnNotificarHora, btnSaiuAgr, btnPromo, btnEnviarMsg, btnMensagens, btnGaleria;
    private ImageView imgConfig, imgNotificacao;
    private String key, nomePadaria, auxHora, auxMinutos, state, imagem, nomePlano, orderId, currentHour, currentMinute;
    private APIService mService;
    private FirebaseUser user;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private Boolean premium;
    private BillingClient mBillingClient;
    private ArrayList arrayFornadas = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_administrativo);

        txtQuantPromo = (TextView) findViewById(R.id.textView25);
        txtFornada = (TextView) findViewById(R.id.textView71);
        txtFornada2 = (TextView) findViewById(R.id.textView75);
        txtFornada3 = (TextView) findViewById(R.id.textView77);
        btnNotificarHora = (Button) findViewById(R.id.button15);
        btnSaiuAgr = (Button) findViewById(R.id.button);
        btnEnviarMsg = (Button) findViewById(R.id.button23);
        btnMensagens = (Button) findViewById(R.id.button36);
        btnPromo= (Button) findViewById(R.id.button16);
        btnGaleria= (Button) findViewById(R.id.button29);
        imgConfig = (ImageView) findViewById(R.id.imageView30);

        arrayFornadas.add("00:00");
        arrayFornadas.add("00:00");
        arrayFornadas.add("00:00");

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        key = user.getUid();

        nomePlano = "assinatura.1.0";

        mService = Common.getFCMClient();

        mBillingClient = BillingClient.newBuilder(PainelAdministrativo.this).setListener(this).build();
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

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PainelAdministrativo.this, TelaGaleriaPadaria.class));
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the interne

            db.collection("usersPro").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        final DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            state = (String) document.getData().get("State");
                            nomePadaria = (String) document.getData().get("Nome");
                            premium = document.getBoolean("Premium");
                            orderId = document.getString("orderId");
                            if (document.getData().get("fornadas") != null) {
                                arrayFornadas = (ArrayList) document.getData().get("fornadas");
                                txtFornada.setText(arrayFornadas.get(0).toString());
                                txtFornada2.setText(arrayFornadas.get(1).toString());
                                txtFornada3.setText(arrayFornadas.get(2).toString());
                            }
                            checarOrder();
                            db.collection("listaInfo").document(user.getUid()).collection("promocao").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    txtQuantPromo.setText(String.valueOf(queryDocumentSnapshots.size()));
                                }
                            });
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( PainelAdministrativo.this,  new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    if (document.getString("Token")!=null ) {
                                        if (!document.getString("Token").equals(instanceIdResult.getToken())) {
                                            db.collection("usersPro").document(user.getUid()).update("Token", instanceIdResult.getToken());
                                            db.collection("listaInfo").document(user.getUid()).update("Token", instanceIdResult.getToken());
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(PainelAdministrativo.this, getString(R.string.erro) + " Cod: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(PainelAdministrativo.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
        }

        imgConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PainelAdministrativo.this, TelaConfigAdministrativo.class));
            }
        });

        btnNotificarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key!=null&&state!=null) {
                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork != null) { // connected to the internet
                        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(PainelAdministrativo.this, android.R.style.Theme_Holo_Light_Dialog));
                        LayoutInflater inflater = getLayoutInflater();
                        final View viewAlerta = inflater.inflate(R.layout.dialog_notificar_hora, null);
                        final EditText editText = (EditText) viewAlerta.findViewById(R.id.editText35);
                        auxHora = "";
                        auxMinutos = "";
                        alerta.setView(viewAlerta);
                        alerta.setCancelable(true);
                        alerta.setPositiveButton("Notificar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText.getText().toString().length() == 5) {
                                    db.collection("listaPadarias").document(state).collection(state).document(key).update("ultimaFornada", "Última fornada: " + auxHora + ":" + auxMinutos).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Data data = new Data(nomePadaria, "Pão quente saindo às " + editText.getText().toString().substring(0, 2) + ":" + editText.getText().toString().substring(3, 5), "", "Hora do Pão", "0", "");
                                            Sender sender = new Sender("/topics/" + key, data);
                                            mService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                    attFornadas(editText.getText().toString().substring(0, 2) + ":" + editText.getText().toString().substring(3, 5));
                                                    Toast.makeText(PainelAdministrativo.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                                    Toast.makeText(PainelAdministrativo.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PainelAdministrativo.this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(PainelAdministrativo.this, "A hora tem que ter formmato XX:XX", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alertDialog = alerta.create();
                        alertDialog.show();

                    } else {
                        Toast.makeText(PainelAdministrativo.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PainelAdministrativo.this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSaiuAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    AlertDialog.Builder alerta = new AlertDialog.Builder(PainelAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção");
                    alerta.setMessage("Deseja notificar que o pão saiu agora?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(true);
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Calendar calendar = Calendar.getInstance();
                            currentHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                            currentMinute = String.valueOf(calendar.get(Calendar.MINUTE));
                            if (currentHour.length() == 1){
                                currentHour = "0" + currentHour;
                            }
                            if (currentMinute.length() == 1){
                                currentMinute = "0" + currentMinute;
                            }
                            db.collection("listaPadarias").document(state).collection(state).document(key).update("ultimaFornada", "Última fornada: " + currentHour + ":" + currentMinute).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Data data = new Data(nomePadaria, "Pão quente saindo agora!", "", "Hora do Pão", "0", "");
                                    Sender sender = new Sender("/topics/" + key ,data);
                                    mService.sendNotification(sender) .enqueue(new Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                    attFornadas(currentHour + ":" + currentMinute);
                                                    Toast.makeText(PainelAdministrativo.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                                    Toast.makeText(PainelAdministrativo.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PainelAdministrativo.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();

                }
                else {
                    Toast.makeText(PainelAdministrativo.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEnviarMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(PainelAdministrativo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_add_servico, null);
                final EditText editText = (EditText) viewAlerta.findViewById(R.id.edtNomeCatAddServ);
                progressBar = (ProgressBar) viewAlerta.findViewById(R.id.progressBar8);
                final ImageView imgAddFoto = viewAlerta.findViewById(R.id.imageView63);
                imgNotificacao = (ImageView) viewAlerta.findViewById(R.id.imageView61);
                imgNotificacao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                        imgAddFoto.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                editText.setHint("Mensagem");
                alerta.setView(viewAlerta);
                alerta.setCancelable(false);
                alerta.setPositiveButton(getResources().getString(R.string.enviar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Data data = new Data(nomePadaria, editText.getText().toString(), "", "Notificações Padaria", "0", "");
                        if (imagem!=null){
                            data = new Data(nomePadaria, editText.getText().toString(), imagem, "Notificações Padaria", "3", user.getUid());
                        }
                        Sender sender = new Sender("/topics/" + key ,data);
                        mService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        Toast.makeText(PainelAdministrativo.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {
                                        Toast.makeText(PainelAdministrativo.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (imagem != null) {
                            StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagem);
                            ref2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                        }
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        });

        /*
        btnNotificarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(PainelAdministrativo.this, android.R.style.Theme_Holo_Light_Dialog));
                LayoutInflater inflater = getLayoutInflater();
                final View viewAlerta = inflater.inflate(R.layout.dialog_notificacao_imagem, null);
                final EditText editText = (EditText) viewAlerta.findViewById(R.id.editText42);
                progressBar = (ProgressBar) viewAlerta.findViewById(R.id.progressBar11);
                imageView = (ImageView) viewAlerta.findViewById(R.id.imageView124);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                alerta.setMessage("Enviar notificação");
                alerta.setView(viewAlerta);
                alerta.setCancelable(true);
                alerta.setPositiveButton(getResources().getString(R.string.enviar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (imagem != null) {
                            Data data = new Data(nomePadaria, editText.getText().toString(), imagem);
                            Sender sender = new Sender("/topics/" + key , data);
                            mService.sendNotification(sender)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            Toast.makeText(PainelAdministrativo.this, "Notificação enviada com sucesso!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(PainelAdministrativo.this, "Erro, tente novamente.", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(PainelAdministrativo.this, "Selecione a imagem!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alerta.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (imagem != null) {
                            StorageReference ref2 = FirebaseStorage.getInstance().getReferenceFromUrl(imagem);
                            ref2.delete();
                        }
                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();


                startActivity(new Intent(PainelAdministrativo.this, TelaNotificarImagem.class));
            }
        });
        */

        btnPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (premium){
                    startActivity(new Intent(PainelAdministrativo.this, TelaPromosPadaria.class));
                }else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(PainelAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção!");
                    alerta.setMessage(getString(R.string.seTornePremium));
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(false);
                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                    .setSku(nomePlano)
                                    .setType(BillingClient.SkuType.SUBS) // SkuType.SUB for subscription
                                    .build();
                            mBillingClient.launchBillingFlow(PainelAdministrativo.this, flowParams);
                        }
                    });
                    alerta.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }
            }
        });

        btnMensagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (premium){
                    startActivity(new Intent(PainelAdministrativo.this, TelaMensagensPadaria.class));
                }else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(PainelAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Atenção!");
                    alerta.setMessage(getString(R.string.seTornePremium));
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(false);
                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                    .setSku(nomePlano)
                                    .setType(BillingClient.SkuType.SUBS) // SkuType.SUB for subscription
                                    .build();
                            mBillingClient.launchBillingFlow(PainelAdministrativo.this, flowParams);
                        }
                    });
                    alerta.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }
            }
        });
    }


    public void attFornadas(String fornadaAtual){
        ArrayList arrayFornadasAUX = new ArrayList<String>();
        arrayFornadasAUX.addAll(arrayFornadas);
        arrayFornadas.clear();
        arrayFornadas.add(fornadaAtual);
        arrayFornadas.add(arrayFornadasAUX.get(0));
        arrayFornadas.add(arrayFornadasAUX.get(1));
        txtFornada.setText(arrayFornadas.get(0).toString());
        txtFornada2.setText(arrayFornadas.get(1).toString());
        txtFornada3.setText(arrayFornadas.get(2).toString());
        db.collection("usersPro").document(user.getUid()).update("fornadas", arrayFornadas);
    }

    private void checarOrder(){
        if (orderId!=null) {
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

            if (purchasesResult.getPurchasesList() != null) {

                if (!purchasesResult.getPurchasesList().toString().contains(orderId)) {
                    premium = false;
                    Map<String, Object> att2 = new HashMap<>();
                    att2.put("Premium", false);
                    att2.put("orderId", null);
                    db.collection("usersPro").document(user.getUid()).update(att2);
                    apagarPromos();

                    AlertDialog.Builder alerta = new AlertDialog.Builder(PainelAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    alerta.setTitle("Falha no Pagamento");
                    alerta.setMessage("Infelizmente você voltou para o plano Gratuito\nDeseja renovar o Premium?");
                    alerta.setIcon(R.mipmap.ic_info);
                    alerta.setCancelable(false);
                    alerta.setPositiveButton("Claro!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                    .setSku(nomePlano)
                                    .setType(BillingClient.SkuType.SUBS) // SkuType.SUB for subscription
                                    .build();
                            mBillingClient.launchBillingFlow(PainelAdministrativo.this, flowParams);
                        }
                    });
                    alerta.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }
            } else {
                premium = false;
                Map<String, Object> att2 = new HashMap<>();
                att2.put("Premium", false);
                att2.put("orderId", null);
                db.collection("usersPro").document(user.getUid()).update(att2);
                apagarPromos();

                AlertDialog.Builder alerta = new AlertDialog.Builder(PainelAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alerta.setTitle("Falha no Pagamento");
                alerta.setMessage("Infelizmente você voltou para o plano Gratuito\nDeseja renovar o Premium?");
                alerta.setIcon(R.mipmap.ic_info);
                alerta.setCancelable(false);
                alerta.setPositiveButton("Claro!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                .setSku(nomePlano)
                                .setType(BillingClient.SkuType.SUBS) // SkuType.SUB for subscription
                                .build();
                        mBillingClient.launchBillingFlow(PainelAdministrativo.this, flowParams);
                    }
                });
                alerta.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        }
    }

    public void apagarPromos(){
        db.collection("promos").whereEqualTo("keyPd", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    documentSnapshot.getReference().delete();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (user != null && data!=null) {
                final Uri uri = data.getData();
                StorageReference filepath = storageReference.child("ImagemNotificacoes").child(uri.getLastPathSegment());

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bmp!=null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] dataByte = baos.toByteArray();

                    filepath.putBytes(dataByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imagem = uri.toString();
                                    Glide.with(getApplicationContext()).load(imagem).apply(new RequestOptions()
                                            .centerCrop()
                                    ).into(imgNotificacao);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PainelAdministrativo.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PainelAdministrativo.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }else {
                    Toast.makeText(PainelAdministrativo.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }else {
            Toast.makeText(PainelAdministrativo.this, getResources().getString(R.string.erro), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                Map<String, Object> att = new HashMap<>();
                att.put("purchaseInfo", purchase.getOriginalJson());
                att.put("orderId", purchase.getOrderId());
                att.put("dateSubs", FieldValue.serverTimestamp());
                att.put("Premium", true);

                WriteBatch batch = db.batch();

                DocumentReference nycRef = db.collection("usersPro").document(user.getUid());
                batch.update(nycRef, att);

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(PainelAdministrativo.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        alerta.setTitle("Você é um Premium!");
                        alerta.setMessage("Parabéns agora você tem todos recursos que o Pão Quente oferece!");
                        //alerta.setIcon(R.mipmap.ic_info);
                        alerta.setCancelable(false);
                        alerta.setNeutralButton("Obrigado!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                premium = true;
                            }
                        });
                        AlertDialog alertDialog = alerta.create();
                        alertDialog.show();
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
