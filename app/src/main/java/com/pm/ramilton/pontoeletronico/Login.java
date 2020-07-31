package com.pm.ramilton.pontoeletronico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login extends AppCompatActivity {

    private Button botaoEntrar;
    private EditText EdtLogin, EdtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        botaoEntrar = (Button) findViewById(R.id.buttonEntrarID);
        EdtLogin = (EditText) findViewById(R.id.editTextMatriculaID);
        EdtSenha = (EditText) findViewById(R.id.editTextSenhaID);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(EdtLogin.getText())) {
                    EdtLogin.setError("O campo matrícula não pode ficar em branco!!!");
                } else if (TextUtils.isEmpty(EdtSenha.getText())) {
                    EdtSenha.setError("O campo senha não pode ficar em branco!!!");
                } else {
                    ParseUser.logInInBackground(EdtLogin.getText().toString(), EdtSenha.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Login.this, "Login realizado com sucesso!!!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                user.logOut();
                                Toast.makeText(Login.this, "Erro ao realizar login!!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }


        });

    }


}
