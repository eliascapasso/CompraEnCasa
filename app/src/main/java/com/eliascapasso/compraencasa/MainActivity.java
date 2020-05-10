package com.eliascapasso.compraencasa;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.eliascapasso.compraencasa.model.Ciudad;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    private Button mBtnComprar, mBtnVender;
    private VideoView vvFondo;
    private MediaPlayer mediaPlayer;
    private TextView tvConsulta;
    private String nombreCiudadActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnVender = (Button)findViewById(R.id.btnVender);
        mBtnComprar = (Button)findViewById(R.id.btnComprar);
        tvConsulta = (TextView)findViewById(R.id.tvConsulta);
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

        permisos();

        mBtnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nombreCiudadActual != null){
                    Intent empresas = new Intent(MainActivity.this, EmpresasActivity.class);
                    empresas.putExtra("nombreCiudadActual", nombreCiudadActual);
                    startActivity(empresas);
                }
                else{
                    Intent ciudades = new Intent(MainActivity.this, CiudadesActivity.class);
                    startActivity(ciudades);
                }
            }
        });

        mBtnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        tvConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirWhatsApp();
            }
        });
    }

    private void permisos(){
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);

        //UBICACION
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
        else{
            obtenerUbicacionActual();
        }

        //LLAMADAS
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        2);
            }
        }
    }

    private void obtenerUbicacionActual(){
        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> direcciones = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (direcciones.size() > 0)
                        System.out.println(direcciones.get(0).getLocality());
                    nombreCiudadActual = direcciones.get(0).getLocality();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void AbrirWhatsApp()
    {
        String telefono = "3434705899";

        Intent _intencion = new Intent("android.intent.action.MAIN");
        _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
        _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators("549" + telefono)+"@s.whatsapp.net");
        startActivity(_intencion);
    }

    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }
}
