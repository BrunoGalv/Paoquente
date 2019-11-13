package com.realvibedev.paoquente;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaConfigNotificacoeseOutros extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtSalvar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private Switch aSwitchNotificacoes;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText editText, editText2;
    private RadioButton radioButton, radioButton2;
    private TextView txtAs, txthorarios;
    private Boolean auxb = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_config_notificacoese_outros);

        imgBack = (ImageView) findViewById(R.id.imageView171);
        txtSalvar = (TextView) findViewById(R.id.textView188);
        aSwitchNotificacoes = (Switch) findViewById(R.id.switch1);
        editText = (EditText) findViewById(R.id.editText36);
        editText2 = (EditText) findViewById(R.id.editText37);
        radioButton = (RadioButton) findViewById(R.id.radioButton2);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton3);
        txtAs = (TextView) findViewById(R.id.textView193);
        txthorarios = (TextView) findViewById(R.id.textView192);

        radioButton.setChecked(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        if (preferences.getBoolean("notificacoes", true)) {
            aSwitchNotificacoes.setChecked(true);
            txthorarios.setVisibility(View.VISIBLE);
            radioButton.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            txtAs.setVisibility(View.VISIBLE);
        }else {
            aSwitchNotificacoes.setChecked(false);
            txthorarios.setVisibility(View.INVISIBLE);
            radioButton.setVisibility(View.INVISIBLE);
            radioButton2.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
            editText2.setVisibility(View.INVISIBLE);
            txtAs.setVisibility(View.INVISIBLE);
        }

        if (!preferences.getBoolean("notificacoesHora", true)) {
            radioButton.setChecked(true);
            editText.setVisibility(View.INVISIBLE);
            editText2.setVisibility(View.INVISIBLE);
            txtAs.setVisibility(View.INVISIBLE);
        }else {
            radioButton.setChecked(false);
            radioButton2.setChecked(true);
        }

        editText.setText(preferences.getString("notificacoesHora1", ""));
        editText2.setText(preferences.getString("notificacoesHora2", ""));


        aSwitchNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitchNotificacoes.isChecked()){
                    txthorarios.setVisibility(View.VISIBLE);
                    radioButton.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    editText2.setVisibility(View.VISIBLE);
                    txtAs.setVisibility(View.VISIBLE);
                }else {
                    txthorarios.setVisibility(View.INVISIBLE);
                    radioButton.setVisibility(View.INVISIBLE);
                    radioButton2.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    editText2.setVisibility(View.INVISIBLE);
                    txtAs.setVisibility(View.INVISIBLE);
                }
            }
        });

        
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(View.INVISIBLE);
                editText2.setVisibility(View.INVISIBLE);
                txtAs.setVisibility(View.INVISIBLE);
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                txtAs.setVisibility(View.VISIBLE);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            String old = "";
            String mask = "##:##";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignored
            }

            // This is the callback we're interested in. Any time
            // your user edits what is in the EditText, this method
            // is called. We'll use it listen for when a user tries
            // to delete the default display text.
            @Override
            public void afterTextChanged(Editable s) {
                // if the user attempts to delete and of the default
                // string displayed, just put it back
                final String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            String old = "";
            String mask = "##:##";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignored
            }

            // This is the callback we're interested in. Any time
            // your user edits what is in the EditText, this method
            // is called. We'll use it listen for when a user tries
            // to delete the default display text.
            @Override
            public void afterTextChanged(Editable s) {
                // if the user attempts to delete and of the default
                // string displayed, just put it back
                final String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText2.setText(mascara);
                editText2.setSelection(mascara.length());
            }
        });

        txtSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = preferences.edit();
                if (aSwitchNotificacoes.isChecked()) {
                    if (radioButton2.isChecked() && (editText.getText().toString().equals("") || editText2.getText().toString().equals(""))) {
                        Toast.makeText(TelaConfigNotificacoeseOutros.this, "Para horário programado é necessário colocar as horas", Toast.LENGTH_SHORT).show();
                        auxb = false;
                    }
                    if (radioButton2.isChecked() && !editText.getText().toString().equals("") && !editText2.getText().toString().equals("")) {
                        if (editText.length() == 5 && editText2.length() == 5) {
                            String hora = editText.getText().toString();
                            String hora2 = editText2.getText().toString();
                            int horaInt = Integer.parseInt(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1)) + String.valueOf(hora.charAt(3)) + String.valueOf(hora.charAt(4)));
                            int horaInt2 = Integer.parseInt(String.valueOf(hora2.charAt(0)) + String.valueOf(hora2.charAt(1)) + String.valueOf(hora2.charAt(3)) + String.valueOf(hora2.charAt(4)));
                            if (horaInt < 2400 && horaInt2 < 2400) {
                                editor.putBoolean("notificacoesHora", true);
                                editor.putString("notificacoesHora1", hora);
                                editor.putString("notificacoesHora2", hora2);
                                auxb = true;
                            } else {
                                Toast.makeText(TelaConfigNotificacoeseOutros.this, "Digite a hora no formato correto, XX:XX", Toast.LENGTH_SHORT).show();
                                auxb = false;
                            }
                        }else {
                            Toast.makeText(TelaConfigNotificacoeseOutros.this, "Digite a hora no formato correto, XX:XX", Toast.LENGTH_SHORT).show();
                            auxb = false;
                        }
                    } else {
                        editor.putBoolean("notificacoesHora", false);
                        editor.putString("notificacoesHora1", "");
                        editor.putString("notificacoesHora2", "");
                        auxb = true;
                    }
                    editor.putBoolean("notificacoes", true);
                }else {
                    auxb = true;
                    editor.putBoolean("notificacoes", false);
                }
                editor.apply();
                if (auxb) {
                    Toast.makeText(TelaConfigNotificacoeseOutros.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                }
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelaConfigNotificacoeseOutros.super.onBackPressed();
            }
        });
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }
}
