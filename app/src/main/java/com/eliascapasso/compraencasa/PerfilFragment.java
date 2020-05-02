package com.eliascapasso.compraencasa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private String mUser;

    private TextView txtNombreEmpresa;
    private ImageView mImgPer;
    private String mCategoria;
    private ProgressBar mProgInicio;
    private View mPeopleRV;

    public PerfilFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_perfil, container, false);


        mReference = FirebaseDatabase.getInstance().getReference().child("empresas");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid().trim();

        // Llamamos los datos de la empresa logueda
        mReference.child(mUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    String url = null, nombreEmpresa, categoria;
                    txtNombreEmpresa = (TextView) view.findViewById(R.id.txtNombreEmpresaV);
                    //spCategoria = (Spinner)view.findViewById(R.id.spCategoriaV);
                    //mImgPer = (ImageView)view.findViewById(R.id.imgpp);


                    nombreEmpresa = dataSnapshot.child("nombreEmpresa").getValue().toString();
                    categoria = dataSnapshot.child("categoria").getValue().toString();
                    //url = dataSnapshot.child("imgp").getValue().toString().trim();

                    // Cargamos los datos
                    if (url != null){
                        //Glide.with(getContext()).load(url).bitmapTransform(new CircleTransform(getContext())).into(mImgPer);
                    }

                    txtNombreEmpresa.setText(nombreEmpresa);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
