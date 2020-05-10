package com.eliascapasso.compraencasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eliascapasso.compraencasa.model.Categoria;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VendedorActivity extends AppCompatActivity {

    private String ciudad;
    private String rubro;
    private String correo;
    private String direccion;
    private String nombreEncargado;
    private String nombreEmpresa;
    private String telefonoFijo;
    private String telefonoMovil;
    private String urlFacebook;
    private String urlInstagram;
    private String sitioWeb;
    private Uri imagen;
    private boolean tieneWsp;
    private String nombreTitulo;

    private Toolbar toolbar;
    private TextView tvNombreTitulo, tvCiudades, tvDireccion;
    private Button btnEditar;
    private ImageView imgLogo;
    private ScrollView scrwVendedor;
    private LinearLayout llVendedor;

    // Variables para enlazar a la base de datos
    private ProgressDialog progress;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tblVendedor);

        inicializarFirebase();

        recibirDatos();

        inicializarComponentes();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgLogo = (ImageView)findViewById(R.id.imgFotoVen);
    }

    private void inicializarComponentes() {
        scrwVendedor = (ScrollView) findViewById(R.id.scrwVendedor);
        llVendedor = (LinearLayout) findViewById(R.id.llVendedor);

        int codigo = 1;

        //TITULO
        TextView tvTitulo = new TextView(getApplicationContext());

        if(nombreEmpresa.isEmpty()){
            nombreTitulo = nombreEncargado;
        }
        else if(nombreEncargado.isEmpty()){
            nombreTitulo = nombreEmpresa;
        }
        else{
            nombreTitulo = nombreEmpresa + " (" + nombreEncargado + ")";
        }

        tvTitulo.setText("   " + nombreTitulo);
        tvTitulo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edificio, 0, 0, 0);
        tvTitulo.setId(codigo);
        codigo++;
        tvTitulo.setTextColor(Color.BLACK);

        llVendedor.addView(tvTitulo);

        Space espacio = new Space(getApplicationContext());
        espacio.setMinimumHeight(15);
        llVendedor.addView(espacio);

        //CIUDADES
        TextView tvCiudades = new TextView(getApplicationContext());

        tvCiudades.setText("   " + ciudad);
        tvCiudades.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ubicacion, 0, 0, 0);
        tvCiudades.setId(codigo);
        codigo++;
        tvCiudades.setTextColor(Color.BLACK);

        llVendedor.addView(tvCiudades);

        Space espacio1 = new Space(getApplicationContext());
        espacio1.setMinimumHeight(15);
        llVendedor.addView(espacio1);

        //DIRECCION
        TextView tvDir = new TextView(getApplicationContext());

        tvDir.setText("   " + direccion);
        tvDir.setCompoundDrawablesWithIntrinsicBounds(R.drawable.direccion, 0, 0, 0);
        tvDir.setId(codigo);
        codigo++;
        tvDir.setTextColor(Color.BLACK);

        llVendedor.addView(tvDir);

        Space espacio2 = new Space(getApplicationContext());
        espacio2.setMinimumHeight(15);
        llVendedor.addView(espacio2);

        //WSP
        if(tieneWsp){
            TextView tvWsp = new TextView(getApplicationContext());

            tvWsp.setText("   Tiene Whatsapp");
            tvWsp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.whatsapp, 0, 0, 0);
            tvWsp.setId(codigo);
            codigo++;
            tvWsp.setTextColor(Color.BLACK);

            llVendedor.addView(tvWsp);

            Space espacio3 = new Space(getApplicationContext());
            espacio3.setMinimumHeight(15);
            llVendedor.addView(espacio3);
        }

        //CELULAR
        if(!telefonoMovil.isEmpty()){
            TextView tvCelular = new TextView(getApplicationContext());

            tvCelular.setText("   " + telefonoMovil);
            tvCelular.setCompoundDrawablesWithIntrinsicBounds(R.drawable.llamada, 0, 0, 0);
            tvCelular.setId(codigo);
            codigo++;
            tvCelular.setTextColor(Color.BLACK);

            llVendedor.addView(tvCelular);

            Space espacio4 = new Space(getApplicationContext());
            espacio4.setMinimumHeight(15);
            llVendedor.addView(espacio4);
        }

        //FIJO
        if(!telefonoFijo.isEmpty()){
            TextView tvFijo = new TextView(getApplicationContext());

            tvFijo.setText("   " + telefonoFijo);
            tvFijo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.telefono, 0, 0, 0);
            tvFijo.setId(codigo);
            codigo++;
            tvFijo.setTextColor(Color.BLACK);

            llVendedor.addView(tvFijo);

            Space espacio5 = new Space(getApplicationContext());
            espacio5.setMinimumHeight(15);
            llVendedor.addView(espacio5);
        }

        //FACEBOOK
        if(!urlFacebook.isEmpty()){
            TextView tvTFacebook = new TextView(getApplicationContext());

            tvTFacebook.setText("   " + urlFacebook);
            tvTFacebook.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook, 0, 0, 0);
            tvTFacebook.setId(codigo);
            codigo++;
            tvTFacebook.setTextColor(Color.BLACK);

            llVendedor.addView(tvTFacebook);

            Space espacio6 = new Space(getApplicationContext());
            espacio6.setMinimumHeight(15);
            llVendedor.addView(espacio6);
        }

        //INSTAGRAM
        if(!urlInstagram.isEmpty()){
            TextView tvIg = new TextView(getApplicationContext());

            tvIg.setText("   " + urlInstagram);
            tvIg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.instagram, 0, 0, 0);
            tvIg.setId(codigo);
            codigo++;
            tvIg.setTextColor(Color.BLACK);

            llVendedor.addView(tvIg);

            Space espacio7 = new Space(getApplicationContext());
            espacio7.setMinimumHeight(15);
            llVendedor.addView(espacio7);
        }

        //SITIO WEB
        if(!sitioWeb.isEmpty()){
            TextView tvWeb = new TextView(getApplicationContext());

            tvWeb.setText("   " + sitioWeb);
            tvWeb.setCompoundDrawablesWithIntrinsicBounds(R.drawable.web, 0, 0, 0);
            tvWeb.setId(codigo);
            codigo++;
            tvWeb.setTextColor(Color.BLACK);

            llVendedor.addView(tvWeb);

            Space espacio8 = new Space(getApplicationContext());
            espacio8.setMinimumHeight(15);
            llVendedor.addView(espacio8);
        }
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        ciudad = extras.getString("ciudad");
        rubro = extras.getString("categoria");
        correo = extras.getString("correo");
        nombreEncargado = extras.getString("nombreEncargado");
        direccion = extras.getString("direccion");
        nombreEmpresa = extras.getString("nombreEmpresa");
        telefonoFijo = extras.getString("telefonoFijo");
        telefonoMovil = extras.getString("telefonoMovil");
        tieneWsp = extras.getBoolean("tieneWsp");
        urlFacebook = extras.getString("urlFacebook");
        urlInstagram = extras.getString("urlInstagram");
        sitioWeb = extras.getString("sitioWeb");
        String urlLogo = extras.getString("imagenLogo");
        /*imagen = Uri.parse(urlLogo);

        if(!urlLogo.isEmpty() && urlLogo != null){
            Glide.with(VendedorActivity.this)
                    .load(imagen)
                    .fitCenter()
                    .into(imgLogo);
        }*/
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
