package com.eliascapasso.compraencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.eliascapasso.compraencasa.model.Ciudad;
import com.eliascapasso.compraencasa.model.Empresa;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CiudadesActivity extends AppCompatActivity {

    private ListView lstCiudades;
    private EditText txtBuscador;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudades);

        lstCiudades = (ListView)findViewById(R.id.lstCiudades);
        txtBuscador = (EditText)findViewById(R.id.txtBuscadorCiudad);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Ciudad.getCiudades());
        lstCiudades.setAdapter(adapter);
        txtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.tblCiudades);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lstCiudades.setClickable(true);
        lstCiudades.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String nombreCiudad = (String) lstCiudades.getItemAtPosition(position);

                Intent empresas = new Intent(CiudadesActivity.this, EmpresasActivity.class);
                empresas.putExtra("nombreCiudadActual", nombreCiudad);
                startActivity(empresas);
            }
        });
    }
}
