package com.eliascapasso.compraencasa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.eliascapasso.compraencasa.adaptadores.AdaptadorEmpresas;
import com.eliascapasso.compraencasa.model.Empresa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EmpresasActivity extends AppCompatActivity {

    private ListView lstEmpresas;
    private EditText txtBuscador;
    private AdaptadorEmpresas adapter;
    private String nombreCiudadActual;
    private Toolbar toolbar;
    private ArrayList<Empresa> listaEmpresas = new ArrayList<Empresa>();

    // Variables para enlazar a la base de datos
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        recibirDatos();

        inicializarFirebase();

        progressDialog = new ProgressDialog(this);

        lstEmpresas = (ListView)findViewById(R.id.lstEmpresas);
        txtBuscador = (EditText)findViewById(R.id.txtBuscadorEmpresa);

        listarDatos();

        txtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(txtBuscador.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar = (Toolbar)findViewById(R.id.tblEmpresas);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitle("En " + nombreCiudadActual);

        lstEmpresas.setClickable(true);
        lstEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Empresa empresa = (Empresa) lstEmpresas.getItemAtPosition(position);

                Intent detalleEmpresa = new Intent(EmpresasActivity.this, DetalleEmpresaActivity.class);
                detalleEmpresa.putExtra("categoria", empresa.getCategoria());
                detalleEmpresa.putExtra("ciudad", empresa.getCiudad());
                detalleEmpresa.putExtra("correo", empresa.getCorreo());
                detalleEmpresa.putExtra("direccion", empresa.getDireccion());
                detalleEmpresa.putExtra("nombreEncargado", empresa.getNombreEncargado());
                detalleEmpresa.putExtra("nombreEmpresa", empresa.getNombreEmpresa());
                detalleEmpresa.putExtra("telefonoFijo", empresa.getTelefonoFijo());
                detalleEmpresa.putExtra("telefonoMovil", empresa.getTelefonoMovil());
                detalleEmpresa.putExtra("tieneWsp", empresa.getTieneWsp());
                detalleEmpresa.putExtra("tieneWsp", empresa.getTieneWsp());
                detalleEmpresa.putExtra("urlFacebook", empresa.getUrlFacebook());
                detalleEmpresa.putExtra("urlInstagram", empresa.getUrlInstagram());
                detalleEmpresa.putExtra("sitioWeb", empresa.getSitioWeb());
                detalleEmpresa.putExtra("imagenLogo", empresa.getImagenLogo());
                startActivity(detalleEmpresa);
            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void listarDatos(){
        progressDialog.setMessage("Cargando..");
        progressDialog.show();
        databaseReference.child("USUARIOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Empresa> empresas = new ArrayList<Empresa>();

                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    //Obtiene el objeto empresa
                    Empresa empresa = objSnapShot.getValue(Empresa.class);
                    listaEmpresas.add(empresa);

                    //Muestra empresas filtradas por ciudad y categoria
                    if(empresa.getCiudad() != null){
                        if(empresa.getCiudad().contains(nombreCiudadActual)){

                            empresas.add(empresa);

                            adapter = new AdaptadorEmpresas(EmpresasActivity.this, empresas);
                            lstEmpresas.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        nombreCiudadActual = extras.getString("nombreCiudadActual");
    }
}
