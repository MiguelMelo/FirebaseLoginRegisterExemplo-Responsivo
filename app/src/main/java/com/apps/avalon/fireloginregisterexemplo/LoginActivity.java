package com.apps.avalon.fireloginregisterexemplo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Classe FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //Chamando Itens do Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Button loginbtn, registerbtn;
    EditText campoEmail, campoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializando FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Usuário logado
                    Log.d("Log", "Logado:" + user.getUid());
                } else {
                    //Usuário deslogado
                    Log.d("Log", "Deslogado!");
                }
            }
        };

        loginbtn = (Button)findViewById(R.id.loginbtn);
        registerbtn = (Button)findViewById(R.id.registerbtn);

        campoEmail = (EditText)findViewById(R.id.campoEmail);
        campoSenha = (EditText)findViewById(R.id.campoSenha);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Falha no login!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Conectado com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Falha no registro!", Toast.LENGTH_SHORT).show();
                                }else{
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    ref.child("email").setValue(campoEmail.getText().toString());
                                    ref.child("uId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
