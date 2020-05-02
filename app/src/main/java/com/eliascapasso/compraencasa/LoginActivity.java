package com.eliascapasso.compraencasa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog mPdLogin;
    private EditText mEditUsu, mEditCon;
    private Button mBtnAcceder, mBtnRegistar;

    // Variables para enlazar a la base de datos
    private ProgressDialog progress;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tblLogin);
        //setSupportActionBar(toolbar);

        inicializarFirebase();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEditUsu = (EditText)findViewById(R.id.txtusu);
        mEditCon = (EditText)findViewById(R.id.txtcont);
        mBtnRegistar = (Button)findViewById(R.id.btnregistrar);
        mBtnAcceder = (Button)findViewById(R.id.btnacceder);

        mPdLogin = new ProgressDialog(this);

        mBtnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Autenticar();
            }
        });

        mBtnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(reg);
            }
        });
    }

    private void Autenticar(){

        String nombreEmpresa = mEditUsu.getText().toString();
        String password = mEditCon.getText().toString();


        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(nombreEmpresa)) {

            mPdLogin.setMessage("Validando datos ...");
            mPdLogin.show();

            // Validamos datos de ingreso
            mAuth.signInWithEmailAndPassword(nombreEmpresa, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Si los datos son correctos accede
                            if (task.isSuccessful()) {
                                mPdLogin.dismiss();

                                Intent mantenimiento = new Intent(LoginActivity.this, EnMantenimientoActivity.class);
                                startActivity(mantenimiento);

                                //Intent vend = new Intent(LoginActivity.this, VendedorActivity.class);
                                //startActivity(vend);
                            }
                            // Si no mensaje de error
                            else {

                                mPdLogin.dismiss();

                                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }
}
