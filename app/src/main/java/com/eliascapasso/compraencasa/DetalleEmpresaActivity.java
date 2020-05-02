package com.eliascapasso.compraencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetalleEmpresaActivity extends AppCompatActivity {

    private String ciudad;
    private String rubro;
    private String correo;
    private String ubicacion;
    private String nombreEncargado;
    private String nombreEmpresa;
    private String telefonoFijo;
    private String telefonoMovil;
    private String urlFacebook;
    private String urlInstagram;
    private Uri imagen;
    private boolean tieneWsp;
    private String nombreTitulo;

    private Toolbar toolbar;

    private TextView tvNombreTitulo, tvUbicacion;
    private Button btnEnviarWsp, btnLlamarFijo, btnLlamarMovil, btnFacebook, btnInstagram;
    private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_empresa);

        tvNombreTitulo = (TextView)findViewById(R.id.tvNombreTitulo);
        tvUbicacion = (TextView)findViewById(R.id.tvUbicacion);
        btnLlamarFijo = (Button)findViewById(R.id.btnLlamarFijo);
        btnLlamarMovil = (Button)findViewById(R.id.btnLlamarMovil);
        btnEnviarWsp = (Button)findViewById(R.id.btnEnviarWsp);
        btnFacebook = (Button)findViewById(R.id.btnFacebook);
        btnInstagram = (Button)findViewById(R.id.btnInstagram);
        imgLogo = (ImageView)findViewById(R.id.imgFotoDetalle);

        recibirDatos();

        inicilizarComponentes();

        btnEnviarWsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirWhatsApp(telefonoMovil);
            }
        });

        btnLlamarMovil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Llamar(telefonoMovil);
            }
        });

        btnLlamarFijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Llamar(telefonoFijo);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriFacebook = Uri.parse(urlFacebook);
                Intent ventanaFacebook = new Intent(Intent.ACTION_VIEW, uriFacebook);
                startActivity(ventanaFacebook);
            }
        });

        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriInstagram = Uri.parse(urlInstagram);
                Intent ventanaIg = new Intent(Intent.ACTION_VIEW, uriInstagram);
                startActivity(ventanaIg);
            }
        });

        toolbar = (Toolbar)findViewById(R.id.tblDetalleEmpresa);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitle(nombreTitulo);
    }

    private void inicilizarComponentes(){
        if(nombreEmpresa.isEmpty()){
            nombreTitulo = nombreEncargado;
        }
        else if(nombreEncargado.isEmpty()){
            nombreTitulo = nombreEmpresa;
        }
        else{
            nombreTitulo = nombreEmpresa + " (" + nombreEncargado + ")";
        }

        tvNombreTitulo.setText("   " + nombreTitulo);
        ubicacion = "   " + ciudad + ", " + ubicacion;
        tvUbicacion.setText(ubicacion);

        //CELULAR
        if(telefonoMovil.isEmpty() || telefonoMovil == null){
            btnLlamarMovil.setVisibility(View.GONE);
        }
        else{
            btnLlamarMovil.setVisibility(View.VISIBLE);

            //WSP
            if(tieneWsp){
                btnEnviarWsp.setVisibility(View.VISIBLE);
            }
            else{
                btnEnviarWsp.setVisibility(View.GONE);
            }
        }

        //FIJO
        if(telefonoFijo.isEmpty() || telefonoFijo == null){
            btnLlamarFijo.setVisibility(View.GONE);
        }
        else{
            btnLlamarFijo.setVisibility(View.VISIBLE);
        }

        //FACEBOOK
        if(urlFacebook.isEmpty() || urlFacebook == null){
            btnFacebook.setVisibility(View.GONE);
        }
        else{
            btnFacebook.setVisibility(View.VISIBLE);
        }

        //INSTAGRAM
        if(urlInstagram.isEmpty() || urlInstagram == null){
            btnInstagram.setVisibility(View.GONE);
        }
        else{
            btnInstagram.setVisibility(View.VISIBLE);
        }
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        ciudad = extras.getString("ciudad");
        rubro = extras.getString("categoria");
        correo = extras.getString("correo");
        nombreEncargado = extras.getString("nombreEncargado");
        ubicacion = extras.getString("direccion");
        nombreEmpresa = extras.getString("nombreEmpresa");
        telefonoFijo = extras.getString("telefonoFijo");
        telefonoMovil = extras.getString("telefonoMovil");
        tieneWsp = extras.getBoolean("tieneWsp");
        urlFacebook = extras.getString("urlFacebook");
        urlInstagram = extras.getString("urlInstagram");
        imagen = Uri.parse(extras.getString("imagenLogo"));

        if(imagen != null || imagen != Uri.parse("")){
            Glide.with(DetalleEmpresaActivity.this)
                    .load(imagen)
                    .fitCenter()
                    .into(imgLogo);
        }
    }

    private void AbrirWhatsApp(String telefono)
    {
        Intent _intencion = new Intent("android.intent.action.MAIN");
        _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));

        if(nombreEncargado != null || !nombreEncargado.isEmpty()){
            _intencion.putExtra(Intent.EXTRA_TEXT, "Hola " + nombreEncargado + ", me gustaría comprar ");
        }
        else{
            _intencion.putExtra(Intent.EXTRA_TEXT, "Hola, me gustaría comprar ");
        }

        _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators("549" + telefono)+"@s.whatsapp.net");
        startActivity(_intencion);
    }

    private void Llamar(String telefono){
        if(!TextUtils.isEmpty(telefono)) {
            String dial = "tel:" + telefono;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }
    }
}
