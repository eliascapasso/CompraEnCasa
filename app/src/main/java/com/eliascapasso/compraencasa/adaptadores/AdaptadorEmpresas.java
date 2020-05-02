package com.eliascapasso.compraencasa.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eliascapasso.compraencasa.FilaEmpresaActivity;
import com.eliascapasso.compraencasa.model.Empresa;
import com.eliascapasso.compraencasa.R;

import java.util.ArrayList;

public class AdaptadorEmpresas extends BaseAdapter {
    Context contexto;
    ArrayList<Empresa> listaEmpresas;
    ArrayList<Empresa> copiaListaEmpresas = new ArrayList<Empresa>();

    public AdaptadorEmpresas(Context contexto, ArrayList<Empresa> listaEmpresas) {
        this.contexto = contexto;
        this.listaEmpresas = listaEmpresas;
        this.copiaListaEmpresas.addAll(this.listaEmpresas);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        EmpresaHolder empresaHolder;
        //Convertimos la vista por defecto en el tipo de nuestra vista personalizada
        FilaEmpresaActivity view = (FilaEmpresaActivity) convertView;
        if(view == null){
            //Instanciamos la vista y el PedidoHolder
            empresaHolder = new EmpresaHolder();
            view = new FilaEmpresaActivity(contexto);
            //Instanciamos los recursos
            empresaHolder.tvNombre = (TextView)view.findViewById(R.id.tvNombre);
            empresaHolder.ivFotoEmpresa = (ImageView) view.findViewById(R.id.imgFotoLista);
            //asignamos el viewHolder a la vista
            view.setTag(empresaHolder);
            //Al cambiar el codigo, debemos llamar nosotros al metodo createViews() de la vista
            view.createViews();
        }else{
            //Si la vista ya existe, recuperamos el viewHolder asociado
            empresaHolder = (EmpresaHolder) view.getTag();
        }

        //NOMBRE
        empresaHolder.tvNombre.setText("  " + listaEmpresas.get(i).getNombreEmpresa());

        //FOTO
        if(!listaEmpresas.get(i).getImagenLogo().isEmpty()){
            Uri imagen = Uri.parse(listaEmpresas.get(i).getImagenLogo());

            Glide.with(view)
                    .load(imagen)
                    .fitCenter()
                    .into(empresaHolder.ivFotoEmpresa);
        }
        return view;
    }

    /* Filtra los datos del adaptador */
    public void filtrar(String texto) {
        // Elimina todos los datos del ArrayList que se cargan en los
        // elementos del adaptador
        listaEmpresas.clear();

        // Si no hay texto: agrega de nuevo los datos del ArrayList copiado
        // al ArrayList que se carga en los elementos del adaptador
        if (texto.length() == 0) {
            listaEmpresas.addAll(copiaListaEmpresas);
        } else {

            // Recorre todos los elementos que contiene el ArrayList copiado
            // y dependiendo de si estos contienen el texto ingresado por el
            // usuario los agrega de nuevo al ArrayList que se carga en los
            // elementos del adaptador.
            texto = texto.toUpperCase();
            for (Empresa empresa : copiaListaEmpresas) {
                if (empresa.getNombreEmpresa().toUpperCase().contains(texto)
                || empresa.getNombreEncargado().toUpperCase().contains(texto)
                || empresa.getCategoria().toUpperCase().contains(texto)
                || empresa.getDireccion().toUpperCase().contains(texto)) {
                    listaEmpresas.add(empresa);
                }
            }
        }

        // Actualiza el adaptador para aplicar los cambios
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listaEmpresas.size();
    }

    @Override
    public Empresa getItem(int i) {
        return listaEmpresas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
