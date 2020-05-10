package com.eliascapasso.compraencasa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.eliascapasso.compraencasa.model.Empresa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog mPdLogin;
    private EditText mEditUsu, mEditCon;
    private Button mBtnAcceder, mBtnRegistar;
    private VideoView vvFondo;
    private MediaPlayer mediaPlayer;

    // Variables para enlazar a la base de datos
    private ProgressDialog progress;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tblLogin);

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
        vvFondo = (VideoView)findViewById(R.id.vvFondo);

        Uri uriFondo = Uri.parse("android.resource://"
                + getPackageName()
                + "/"
                + R.raw.videofondo1);
        vvFondo.setVideoURI(uriFondo);
        vvFondo.start();

        vvFondo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });

        mPdLogin = new ProgressDialog(this);

        mBtnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrarPerfil();
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

    private void entrarPerfil(){
        final String nombreLogin = mEditUsu.getText().toString();
        String passwordLogin = mEditCon.getText().toString();

        progress.setMessage("Cargando..");
        progress.show();
        databaseReference.child("USUARIOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean usuarioValido = false;
                Empresa empresaIngresada = new Empresa();
                ArrayList<Empresa> empresas = new ArrayList<Empresa>();

                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    //Obtiene el objeto empresa
                    Empresa empresa = objSnapShot.getValue(Empresa.class);

                    if(empresa.getNombreEmpresa().equals(nombreLogin) || empresa.getNombreEncargado().equals(nombreLogin) && !nombreLogin.isEmpty()){
                        usuarioValido = true;
                        empresaIngresada = empresa;
                        break;
                    }
                }

                if(usuarioValido){
                    Intent entrar = new Intent(LoginActivity.this, VendedorActivity.class);
                    entrar.putExtra("categoria", empresaIngresada.getCategoria());
                    entrar.putExtra("ciudad", empresaIngresada.getCiudad());
                    entrar.putExtra("correo", empresaIngresada.getCorreo());
                    entrar.putExtra("direccion", empresaIngresada.getDireccion());
                    entrar.putExtra("nombreEncargado", empresaIngresada.getNombreEncargado());
                    entrar.putExtra("nombreEmpresa", empresaIngresada.getNombreEmpresa());
                    entrar.putExtra("telefonoFijo", empresaIngresada.getTelefonoFijo());
                    entrar.putExtra("telefonoMovil", empresaIngresada.getTelefonoMovil());
                    entrar.putExtra("tieneWsp", empresaIngresada.getTieneWsp());
                    entrar.putExtra("tieneWsp", empresaIngresada.getTieneWsp());
                    entrar.putExtra("urlFacebook", empresaIngresada.getUrlFacebook());
                    entrar.putExtra("urlInstagram", empresaIngresada.getUrlInstagram());
                    entrar.putExtra("sitioWeb", empresaIngresada.getSitioWeb());
                    entrar.putExtra("imagenLogo", empresaIngresada.getImagenLogo());
                    startActivity(entrar);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Datos ingresados inválidos", Toast.LENGTH_SHORT).show();
                }

                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress.dismiss();
            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //mAuth = FirebaseAuth.getInstance();
    }
}
