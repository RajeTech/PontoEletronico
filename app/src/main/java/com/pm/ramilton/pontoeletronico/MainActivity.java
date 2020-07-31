package com.pm.ramilton.pontoeletronico;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;
import com.pm.ramilton.pontoeletronico.Adaptador.ViewPagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView TvUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TvUsuario = (TextView) findViewById(R.id.txtUsuario);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new fragment_PontoEletronico(),"PONTO ELETRÔNICO");
        adapter.AddFragment(new fragment_Historico(),"HISTÓRICO");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ParseUser usuarioatual = ParseUser.getCurrentUser();
        String usuario = usuarioatual.getUsername();
        TvUsuario.setText("Seja bem Vindo Colaborado(a)r "+usuario);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.informacao:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.sair:
                ParseUser.logOut();
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
