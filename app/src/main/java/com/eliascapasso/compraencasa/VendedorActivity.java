package com.eliascapasso.compraencasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VendedorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Boolean FragmentTransaction = false;
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    fragment = new PerfilFragment();
                    FragmentTransaction = true;
                    break;
                case R.id.navigation_products:
                    fragment = new ProductosFragment();
                    FragmentTransaction = true;
                    break;
            }

            if (FragmentTransaction) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_default, fragment)
                        .commit();
                item.setCheckable(true);
            }
            return true;
        }

    };

    public void btnsalir(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
