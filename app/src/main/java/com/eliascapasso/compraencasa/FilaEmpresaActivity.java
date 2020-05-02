package com.eliascapasso.compraencasa;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eliascapasso.compraencasa.adaptadores.EmpresaHolder;

public class FilaEmpresaActivity extends LinearLayout {
    public TextView tvNombre;
    public ImageView ivFoto;
    private EmpresaHolder empresaHolder;

    public FilaEmpresaActivity(Context context){
        super(context);
        inflate(context, R.layout.list_item_empresa, this);
    }

    public void createViews() {
        //Instanciamos los elementos de la vista
        empresaHolder = (EmpresaHolder) this.getTag();
        tvNombre = empresaHolder.tvNombre;
        ivFoto = empresaHolder.ivFotoEmpresa;
    }
}
