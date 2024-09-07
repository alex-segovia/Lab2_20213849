package com.example.lab2_20213849;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Estadisticas extends AppCompatActivity {

    private String nombreJugador;
    private ArrayList<String> listaJuegos;
    private int contadorJuegos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        Intent intent = getIntent();
        nombreJugador = intent.getStringExtra("nombreJugador");
        listaJuegos = new ArrayList<>();
        listaJuegos = intent.getStringArrayListExtra("listaJuegos");
        contadorJuegos = intent.getIntExtra("contadorJuegos",0);

        TextView textoJugador = findViewById(R.id.textoJugador);
        String nuevoTexto = "Jugador: " + nombreJugador;
        textoJugador.setText(nuevoTexto);

        Log.d("contador",String.valueOf(contadorJuegos));

        LinearLayout linearLayout = findViewById(R.id.linearLayoutEstadisticas);
        linearLayout.removeAllViews();

        for(int i=0;i<listaJuegos.size();i++){
            LinearLayout.LayoutParams parametrosTexto = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            parametrosTexto.setMargins(0,10,0,10);

            TextView nuevoElemento = new TextView(this);

            nuevoElemento.setLayoutParams(parametrosTexto);

            nuevoElemento.setTextSize(20);
            nuevoElemento.setTextColor(Color.BLACK);
            nuevoElemento.setText(listaJuegos.get(i));

            linearLayout.addView(nuevoElemento);
        }

        if(listaJuegos.isEmpty()){

            linearLayout.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams parametrosTexto = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            parametrosTexto.setMargins(0,10,0,10);

            TextView nuevoElemento2 = new TextView(this);

            nuevoElemento2.setLayoutParams(parametrosTexto);

            nuevoElemento2.setTextSize(20);
            nuevoElemento2.setTextColor(Color.BLACK);
            String mensaje = "El jugador todavía no ha realizado ningún juego";
            nuevoElemento2.setText(mensaje);

            linearLayout.addView(nuevoElemento2);
        }

        Button botonNuevoJuego = findViewById(R.id.buttonNuevoJuego2);
        botonNuevoJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoJuego(true);
            }
        });
    }


    //Esto lo encontré en ChatGPT porque ya no podía más con ese Up Navigation, de verdad
    //me había quedado hasta las 4am intentado hacer que funcione y no podía, así que tuve
    //que recurrir a ChatGPT por salud mental.
    //Lo que le pregunté fue si el Up Navigation tiene un ID por defecto y me dio este método
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            nuevoJuego(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void nuevoJuego(boolean esNuevoJuego){
        Intent intent = new Intent();
        String nombre = nombreJugador;
        ArrayList<String> lista = new ArrayList<>();
        lista = listaJuegos;
        int contador = contadorJuegos;
        intent.putExtra("nombreJugador",nombre);
        intent.putExtra("listaJuegos",lista);
        intent.putExtra("contadorJuegos",contador);
        intent.putExtra("esNuevoJuego",esNuevoJuego);
        setResult(RESULT_OK,intent);
        finish();
    }
}