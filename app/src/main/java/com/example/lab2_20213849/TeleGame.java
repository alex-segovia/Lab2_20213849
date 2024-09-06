package com.example.lab2_20213849;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class TeleGame extends AppCompatActivity {

    private String nombreJugador;
    private ArrayList<String> listaJuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_game);

        Intent intent = getIntent();
        nombreJugador = intent.getStringExtra("nombreJugador");
        listaJuegos = new ArrayList<>();
        if(intent.getStringArrayListExtra("listaJuegos")!=null){
            listaJuegos = intent.getStringArrayListExtra("listaJuegos");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_telegame,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.estadisticas){
            verEstadisticas();
        }
        return super.onOptionsItemSelected(item);
    }

    public void verEstadisticas(){
        Intent intent = new Intent(TeleGame.this, Estadisticas.class);
        intent.putExtra("nombreJugador",nombreJugador);
        intent.putExtra("listaJuegos",listaJuegos);
        startActivity(intent);
    }
}