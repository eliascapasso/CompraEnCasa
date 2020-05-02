package com.eliascapasso.compraencasa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.Toast;

import com.eliascapasso.compraencasa.model.Categoria;
import com.eliascapasso.compraencasa.model.Ciudad;
import com.eliascapasso.compraencasa.model.Empresa;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    // Varibales de registo
    private EditText txtNombreEmpresa, txtNombreDuenio, txtDireccion, txtMovil, txtFijo, txtFacebook, txtInstagram, txtCorreo, txtCiudades, txtCategorias;
    private Switch swTieneWsp;
    private Button mBtnReg;
    private ImageView mImgPerfil;
    private ScrollView scrollCiudades, scrollCategorias;
    private LinearLayout llCiudades, llCategorias;
    private static final int GALLERY_REGUEST = 1;
    private Uri uriLogo;
    private String ciudadesSeleccionada = "", categoriasSelecionada = "";


    // Variables para enlazar a la base de datos
    private ProgressDialog progress;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mRecerenceST;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tblreg);
        //setSupportActionBar(toolbar);

        inicializarFirebase();

        txtNombreEmpresa = (EditText)findViewById(R.id.txtNombreEmpresa);
        txtNombreDuenio = (EditText)findViewById(R.id.txtNombreDuenio);
        txtDireccion = (EditText)findViewById(R.id.txtDireccion);
        txtMovil = (EditText)findViewById(R.id.txtTelefonoMovil);
        txtFijo = (EditText)findViewById(R.id.txtTelefonoFijo);
        txtFacebook = (EditText)findViewById(R.id.txtFacebook);
        txtInstagram = (EditText)findViewById(R.id.txtInstagram);
        txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtCiudades = (EditText)findViewById(R.id.txtCiudades);
        txtCategorias = (EditText)findViewById(R.id.txtCategorias);

        swTieneWsp = (Switch)findViewById(R.id.swTieneWsp);

        mBtnReg = (Button)findViewById(R.id.btnregistro);
        mImgPerfil = (ImageView)findViewById(R.id.imgLogoReg);

        progress = new ProgressDialog(this);

        categorias();

        ciudades();

        // Se Accede a la galeria para elegir una imagen
        mImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentimg = new Intent(Intent.ACTION_GET_CONTENT);
                intentimg.setType("image/*");
                startActivityForResult(intentimg, GALLERY_REGUEST);
            }
        });

        mBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    startRegistar();
                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startRegistar() {
        //IMAGEN
        if(uriLogo != null){
            final StorageReference filePath;
            if(!txtNombreEmpresa.getText().toString().isEmpty()){
                filePath = mRecerenceST.child("fotos/" + txtNombreEmpresa.getText().toString()).child(uriLogo.getLastPathSegment());
            }
            else{
                filePath = mRecerenceST.child("fotos/" + txtNombreDuenio.getText().toString()).child(uriLogo.getLastPathSegment());
            }

            UploadTask uploadTask = filePath.putFile(uriLogo);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();

                        guardar(downloadURL);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
        else{
            guardar("");
        }
    }

    private void guardar(String downloadURL){
        /*progress.setMessage("Registrando...");
        progress.show();
        progress.setCanceledOnTouchOutside(false);*/

        String id_usu = UUID.randomUUID().toString();

        // Creamos una referencia a donde guardaremos los datos
        final DatabaseReference referencia = databaseReference.child("PRODUCTOS").child(id_usu);

        // Guardamos los datos
        String urlInstagram = txtInstagram.getText().toString();
        if(urlInstagram.indexOf(0) == '@'){
            urlInstagram = urlInstagram.substring(1, urlInstagram.length());
        }
        if(!urlInstagram.contains("www.instagram.com")){
            urlInstagram = "https://www.instagram.com/" + urlInstagram;
        }


        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa(txtNombreEmpresa.getText().toString());
        empresa.setNombreEncargado(txtNombreDuenio.getText().toString());
        empresa.setDireccion(txtDireccion.getText().toString());
        empresa.setTelefonoMovil(txtMovil.getText().toString());
        empresa.setTieneWsp(swTieneWsp.isChecked());
        empresa.setTelefonoFijo(txtFijo.getText().toString());
        empresa.setCorreo(txtCorreo.getText().toString());
        empresa.setCategoria(txtCategorias.getText().toString());
        empresa.setCiudad(txtCiudades.getText().toString());
        empresa.setUrlFacebook(txtFacebook.getText().toString());
        empresa.setUrlInstagram(urlInstagram);
        empresa.setImagenLogo(uriLogo.toString()); //no se usa

        referencia.child("idEmpresa").setValue(id_usu);
        referencia.child("nombreEmpresa").setValue(empresa.getNombreEmpresa());
        referencia.child("nombreEncargado").setValue(empresa.getNombreEncargado());
        referencia.child("direccion").setValue(empresa.getDireccion());
        referencia.child("ciudad").setValue(empresa.getCiudad());
        referencia.child("correo").setValue(empresa.getCorreo());
        referencia.child("tieneWsp").setValue(empresa.getTieneWsp());
        referencia.child("telefonoFijo").setValue(empresa.getTelefonoFijo());
        referencia.child("telefonoMovil").setValue(empresa.getTelefonoMovil());
        referencia.child("categoria").setValue(empresa.getCategoria());
        referencia.child("urlFacebook").setValue(empresa.getUrlFacebook());
        referencia.child("urlInstagram").setValue(empresa.getUrlInstagram());

        referencia.child("imagenLogo").setValue(downloadURL);
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mRecerenceST = FirebaseStorage.getInstance().getReference();
    }

    private void inicializarCiudades(View view){
        ciudadesSeleccionada = "";
        scrollCiudades = (ScrollView) view.findViewById(R.id.scrollCiudades);
        llCiudades = (LinearLayout) view.findViewById(R.id.llCiudades);

        int codigo = 1;
        for(final String ciudad:Ciudad.getCiudades()){
            CheckBox cb = new CheckBox(getApplicationContext());
            Space espacio = new Space(getApplicationContext());

            cb.setText(ciudad);
            cb.setId(codigo);
            codigo++;
            cb.setTextColor(Color.BLACK);
            espacio.setMinimumHeight(15);

            llCiudades.addView(cb);
            llCiudades.addView(espacio);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!ciudadesSeleccionada.contains(ciudad)){
                        ciudadesSeleccionada += ciudad + " - ";
                    }

                    //TODO: Solucionar cuando deselecciona el item (ya que todavia sigue insertado en la cadena)
                }
            });
        }
    }

    private void ciudades(){
        txtCiudades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaCiudades();
            }
        });
    }

    private void ventanaCiudades(){
        ciudadesSeleccionada = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ciudades, null);

        inicializarCiudades(dialogView);

        builder.setCancelable(false);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        dialog.setButton(Dialog.BUTTON_POSITIVE,"Aceptar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!ciudadesSeleccionada.isEmpty()){
                    ciudadesSeleccionada = ciudadesSeleccionada.substring(0, ciudadesSeleccionada.length() - 2);
                }

                txtCiudades.setText(ciudadesSeleccionada);

                dialog.cancel();
            }
        });

        dialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void categorias(){
        txtCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaCategorias();
            }
        });
    }

    private void ventanaCategorias(){
        categoriasSelecionada = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_categorias, null);

        inicializarCategorias(dialogView);

        builder.setCancelable(false);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        dialog.setButton(Dialog.BUTTON_POSITIVE,"Aceptar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!categoriasSelecionada.isEmpty()){
                    categoriasSelecionada = categoriasSelecionada.substring(0, categoriasSelecionada.length() - 2);
                }

                txtCategorias.setText(categoriasSelecionada);

                dialog.cancel();
            }
        });

        dialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void inicializarCategorias(View view){
        categoriasSelecionada = "";
        scrollCategorias = (ScrollView) view.findViewById(R.id.scrollCategorias);
        llCategorias = (LinearLayout) view.findViewById(R.id.llCategorias);

        int codigo = 1;
        for(final String categoria:Categoria.getCategorias()){
            CheckBox cb = new CheckBox(getApplicationContext());
            Space espacio = new Space(getApplicationContext());

            cb.setText(categoria);
            cb.setId(codigo);
            codigo++;
            cb.setTextColor(Color.BLACK);
            espacio.setMinimumHeight(15);

            llCategorias.addView(cb);
            llCategorias.addView(espacio);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!categoriasSelecionada.contains(categoria)){
                        categoriasSelecionada += categoria + " - ";
                    }

                    //TODO: Solucionar cuando deselecciona el item (ya que todavia sigue insertado en la cadena)
                }
            });
        }
    }

    private boolean validarCampos(){

        //NOMBRE EMPRESA Y NOMBRE ENCARGADO
        if(txtNombreEmpresa.getText().toString().isEmpty() && txtNombreDuenio.getText().toString().isEmpty()){
            Toast.makeText(RegistroActivity.this, "Debe ingresar el nombre de la empresa o del encargado", Toast.LENGTH_LONG).show();
            return false;
        }

        //DIRECCION
        if(txtDireccion.getText().toString().isEmpty()){
            Toast.makeText(RegistroActivity.this, "Debe ingresar la dirección en donde está ubicada la empresa", Toast.LENGTH_LONG).show();
            return false;
        }

        //TELEFONO CELULAR
        if(txtMovil.getText().toString().isEmpty() && txtFijo.getText().toString().isEmpty()){
            Toast.makeText(RegistroActivity.this, "Debe ingresar almenos un número de teléfono", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if(!movilValido(txtMovil.getText().toString())){
                Toast.makeText(RegistroActivity.this, "El teléfono móvil ingresado no es válido", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //CORREO
        if(txtCorreo.getText().toString().isEmpty()){
            Toast.makeText(RegistroActivity.this, "Debe ingresar un correo eléctronico", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!correoValido(txtCorreo.getText().toString())){
            Toast.makeText(RegistroActivity.this, "Correo inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        //CIUDADES
        if(txtCiudades.getText().toString().isEmpty()){
            Toast.makeText(RegistroActivity.this, "Seleccione almenos una ciudad", Toast.LENGTH_SHORT).show();
            return false;
        }

        //CATEGORIAS
        if(txtCategorias.getText().toString().isEmpty()){
            Toast.makeText(RegistroActivity.this, "Seleccione almenos un rubro", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean movilValido(String movil){
       return movil.length() == 10 || movil.length() == 11;
    }

    private boolean correoValido(String email){
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");

        Matcher mather = pattern.matcher(email);

        return mather.find();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REGUEST && resultCode == RESULT_OK) {
            uriLogo = data.getData();
            mImgPerfil.setImageURI(uriLogo);

            try{
                Bitmap bitmap = BitmapFactory.decodeFile(uriLogo.toString());
                ExifInterface exif = new ExifInterface(uriLogo.toString());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                    default:
                        break;
                }

                mImgPerfil.setImageBitmap(bitmap);
                bitmap.recycle();
            }catch (Exception e){

            }
        }
    }
}
