package com.eliascapasso.compraencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.eliascapasso.compraencasa.model.Categoria;

public class CategoriasActivity extends AppCompatActivity {

    private ListView lstCategorias;
    private EditText txtBuscador;
    ArrayAdapter<String> adapter;
    private String nombreCiudadActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        recibirDatos();

        lstCategorias = (ListView)findViewById(R.id.lstCategorias);
        txtBuscador = (EditText)findViewById(R.id.txtBuscadorCategoria);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Categoria.getCategorias());
        lstCategorias.setAdapter(adapter);
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

        Toolbar toolbar = (Toolbar)findViewById(R.id.tblCategorias);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lstCategorias.setClickable(true);
        lstCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String categoria = (String) lstCategorias.getItemAtPosition(position);

                Intent empresas = new Intent(CategoriasActivity.this, EmpresasActivity.class);
                empresas.putExtra("nombreCiudadActual", nombreCiudadActual);
                empresas.putExtra("categoria", categoria);
                startActivity(empresas);
            }
        });
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        nombreCiudadActual = extras.getString("nombreCiudadActual");
    }
}
