package com.example.lab2_20213849;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TeleGame extends AppCompatActivity {

    private String nombreJugador;
    private ArrayList<String> listaJuegos;
    private ArrayList<String> listaPalabras = new ArrayList<>();
    private String palabraElegida;
    private int contadorErrores;
    private int contadorLetras;
    private int contadorJuegos;
    private Long tiempoInicio;
    private String estadoJuego;

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
        listaPalabras.add("FIBRA");
        listaPalabras.add("REDES");
        listaPalabras.add("ANTENA");
        listaPalabras.add("PROPA");
        listaPalabras.add("CLOUD");
        listaPalabras.add("TELECO");

        if(intent.getIntExtra("contadorJuegos",0)!=0){
            contadorJuegos = intent.getIntExtra("contadorJuegos",0);
        }else{
            contadorJuegos = 0;
        }

        crearJuego();
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

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            Intent datos = o.getData();
            nombreJugador = datos.getStringExtra("nombreJugador");
            listaJuegos = datos.getStringArrayListExtra("listaJuegos");
            contadorJuegos = datos.getIntExtra("contadorJuegos",0);
        }
    });

    public void verEstadisticas(){
        Intent intent = new Intent(TeleGame.this, Estadisticas.class);
        intent.putExtra("nombreJugador",nombreJugador);
        intent.putExtra("listaJuegos",listaJuegos);
        intent.putExtra("contadorJuegos",contadorJuegos);
        launcher.launch(intent);
    }

    public void iniciarNuevoJuego(View view){
        if(estadoJuego.equals("Jugando")){
            listaJuegos.add("Juego "+contadorJuegos+": Canceló");
            contadorJuegos++;
        }
        crearJuego();
    }

    public void crearJuego(){
        estadoJuego = "Jugando";
        contadorErrores = 0;
        contadorLetras = 0;
        tiempoInicio = System.currentTimeMillis();

        reiniciarBotones();
        reiniciarImagenes();
        TextView resultado = findViewById(R.id.resultado);
        resultado.setText("");

        int numeroAleatorio = new Random().nextInt(listaPalabras.size());
        palabraElegida = listaPalabras.get(numeroAleatorio);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        linearLayout.removeAllViews();

        for (int i=0; i<palabraElegida.length(); i++){
            LinearLayout nuevoCampo = new LinearLayout(this);
            LinearLayout.LayoutParams parametrosDelCampo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            parametrosDelCampo.weight = 1;
            parametrosDelCampo.setMargins(3,0,3,0);
            nuevoCampo.setLayoutParams(parametrosDelCampo);
            nuevoCampo.setOrientation(LinearLayout.VERTICAL);
            nuevoCampo.setGravity(Gravity.CENTER);

            TextView campoDeTexto = new TextView(this);
            LinearLayout.LayoutParams parametrosCampoTexto = new LinearLayout.LayoutParams(50,35);

            parametrosCampoTexto.weight = 1;
            campoDeTexto.setLayoutParams(parametrosCampoTexto);
            campoDeTexto.setEms(10);
            campoDeTexto.setGravity(Gravity.CENTER);
            campoDeTexto.setId(i+100);
            campoDeTexto.setText(palabraElegida.split("")[i]);
            campoDeTexto.setAlpha(0);
            campoDeTexto.setTextColor(Color.BLACK);
            campoDeTexto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campoDeTexto.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

            TextView linea = new TextView(this);
            LinearLayout.LayoutParams parametrosLinea = new LinearLayout.LayoutParams(50,5);
            linea.setLayoutParams(parametrosLinea);
            linea.setGravity(Gravity.CENTER);
            linea.setBackgroundColor(Color.BLACK);
            linea.setText("");

            nuevoCampo.addView(campoDeTexto);
            nuevoCampo.addView(linea);

            linearLayout.addView(nuevoCampo);
        }
    }

    public void elegirLetra(View view){
        boolean letraAcertada = false;
        Button botonElegido = findViewById((view.getId()));
        String letra = botonElegido.getText().toString();
        botonElegido.getBackground().setAlpha(128);
        botonElegido.setClickable(false);
        for(int i=0; i<palabraElegida.length(); i++){
            if(palabraElegida.split("")[i].equals(letra)){
                TextView campoElegido = findViewById(i+100);
                campoElegido.setAlpha(1);
                letraAcertada = true;
                contadorLetras++;
                if(contadorLetras==palabraElegida.length()){
                    Long tiempoFin = System.currentTimeMillis();
                    bloquearBotones();
                    Long duracion = (tiempoFin - tiempoInicio)/1000;
                    TextView resultado = findViewById(R.id.resultado);
                    String mensaje = "Ganó / Terminó en " + duracion + "s";
                    resultado.setText(mensaje);
                    contadorJuegos++;
                    listaJuegos.add("Juego "+contadorJuegos+": Terminó en " + duracion + "s");
                    estadoJuego = "Terminado";
                }
            }
        }

        if(!letraAcertada){
            if(contadorErrores==0){
                ImageView imagenCabeza = findViewById(R.id.cabeza);
                imagenCabeza.setVisibility(View.VISIBLE);
            }else if(contadorErrores==1){
                ImageView imagenTorso = findViewById(R.id.torso);
                imagenTorso.setVisibility(View.VISIBLE);
            }else if(contadorErrores==2){
                ImageView imagenBrazoD = findViewById(R.id.brazoD);
                imagenBrazoD.setVisibility(View.VISIBLE);
            }else if(contadorErrores==3){
                ImageView imagenBrazoI = findViewById(R.id.brazoI);
                imagenBrazoI.setVisibility(View.VISIBLE);
            }else if(contadorErrores==4){
                ImageView imagenPiernaI = findViewById(R.id.piernaI);
                imagenPiernaI.setVisibility(View.VISIBLE);
            }else if(contadorErrores==5){
                ImageView imagenPiernaD = findViewById(R.id.piernaD);
                imagenPiernaD.setVisibility(View.VISIBLE);
            }

            contadorErrores++;

            if(contadorErrores==6){
                Long tiempoFin = System.currentTimeMillis();
                bloquearBotones();
                Long duracion = (tiempoFin - tiempoInicio)/1000;
                TextView resultado = findViewById(R.id.resultado);
                String mensaje = "Perdió / Terminó en " + duracion + "s";
                resultado.setText(mensaje);
                contadorJuegos++;
                listaJuegos.add("Juego "+contadorJuegos+": Terminó en " + duracion + "s");
                estadoJuego = "Terminado";
            }
        }
    }

    public void reiniciarImagenes(){
        ImageView imagenCabeza = findViewById(R.id.cabeza);
        imagenCabeza.setVisibility(View.INVISIBLE);

        ImageView imagenTorso = findViewById(R.id.torso);
        imagenTorso.setVisibility(View.INVISIBLE);

        ImageView imagenBrazoD = findViewById(R.id.brazoD);
        imagenBrazoD.setVisibility(View.INVISIBLE);

        ImageView imagenBrazoI = findViewById(R.id.brazoI);
        imagenBrazoI.setVisibility(View.INVISIBLE);

        ImageView imagenPiernaI = findViewById(R.id.piernaI);
        imagenPiernaI.setVisibility(View.INVISIBLE);

        ImageView imagenPiernaD = findViewById(R.id.piernaD);
        imagenPiernaD.setVisibility(View.INVISIBLE);
    }

    public void bloquearBotones(){
        Button botonA = findViewById(R.id.buttonA);
        botonA.getBackground().setAlpha(128);
        botonA.setClickable(false);

        Button botonB = findViewById(R.id.buttonB);
        botonB.getBackground().setAlpha(128);
        botonB.setClickable(false);

        Button botonC = findViewById(R.id.buttonC);
        botonC.getBackground().setAlpha(128);
        botonC.setClickable(false);

        Button botonD = findViewById(R.id.buttonD);
        botonD.getBackground().setAlpha(128);
        botonD.setClickable(false);

        Button botonE = findViewById(R.id.buttonE);
        botonE.getBackground().setAlpha(128);
        botonE.setClickable(false);

        Button botonF = findViewById(R.id.buttonF);
        botonF.getBackground().setAlpha(128);
        botonF.setClickable(false);

        Button botonG = findViewById(R.id.buttonG);
        botonG.getBackground().setAlpha(128);
        botonG.setClickable(false);

        Button botonH = findViewById(R.id.buttonH);
        botonH.getBackground().setAlpha(128);
        botonH.setClickable(false);

        Button botonI = findViewById(R.id.buttonI);
        botonI.getBackground().setAlpha(128);
        botonI.setClickable(false);

        Button botonJ = findViewById(R.id.buttonJ);
        botonJ.getBackground().setAlpha(128);
        botonJ.setClickable(false);

        Button botonK = findViewById(R.id.buttonK);
        botonK.getBackground().setAlpha(128);
        botonK.setClickable(false);

        Button botonL = findViewById(R.id.buttonL);
        botonL.getBackground().setAlpha(128);
        botonL.setClickable(false);

        Button botonM = findViewById(R.id.buttonM);
        botonM.getBackground().setAlpha(128);
        botonM.setClickable(false);

        Button botonN = findViewById(R.id.buttonN);
        botonN.getBackground().setAlpha(128);
        botonN.setClickable(false);

        Button botonO = findViewById(R.id.buttonO);
        botonO.getBackground().setAlpha(128);
        botonO.setClickable(false);

        Button botonP = findViewById(R.id.buttonP);
        botonP.getBackground().setAlpha(128);
        botonP.setClickable(false);

        Button botonQ = findViewById(R.id.buttonQ);
        botonQ.getBackground().setAlpha(128);
        botonQ.setClickable(false);

        Button botonR = findViewById(R.id.buttonR);
        botonR.getBackground().setAlpha(128);
        botonR.setClickable(false);

        Button botonS = findViewById(R.id.buttonS);
        botonS.getBackground().setAlpha(128);
        botonS.setClickable(false);

        Button botonT = findViewById(R.id.buttonT);
        botonT.getBackground().setAlpha(128);
        botonT.setClickable(false);

        Button botonU = findViewById(R.id.buttonU);
        botonU.getBackground().setAlpha(128);
        botonU.setClickable(false);

        Button botonV = findViewById(R.id.buttonV);
        botonV.getBackground().setAlpha(128);
        botonV.setClickable(false);

        Button botonW = findViewById(R.id.buttonW);
        botonW.getBackground().setAlpha(128);
        botonW.setClickable(false);

        Button botonX = findViewById(R.id.buttonX);
        botonX.getBackground().setAlpha(128);
        botonX.setClickable(false);

        Button botonY = findViewById(R.id.buttonY);
        botonY.getBackground().setAlpha(128);
        botonY.setClickable(false);

        Button botonZ = findViewById(R.id.buttonZ);
        botonZ.getBackground().setAlpha(128);
        botonZ.setClickable(false);
    }

    public void reiniciarBotones(){
        Button botonA = findViewById(R.id.buttonA);
        botonA.getBackground().setAlpha(255);
        botonA.setClickable(true);

        Button botonB = findViewById(R.id.buttonB);
        botonB.getBackground().setAlpha(255);
        botonB.setClickable(true);

        Button botonC = findViewById(R.id.buttonC);
        botonC.getBackground().setAlpha(255);
        botonC.setClickable(true);

        Button botonD = findViewById(R.id.buttonD);
        botonD.getBackground().setAlpha(255);
        botonD.setClickable(true);

        Button botonE = findViewById(R.id.buttonE);
        botonE.getBackground().setAlpha(255);
        botonE.setClickable(true);

        Button botonF = findViewById(R.id.buttonF);
        botonF.getBackground().setAlpha(255);
        botonF.setClickable(true);

        Button botonG = findViewById(R.id.buttonG);
        botonG.getBackground().setAlpha(255);
        botonG.setClickable(true);

        Button botonH = findViewById(R.id.buttonH);
        botonH.getBackground().setAlpha(255);
        botonH.setClickable(true);

        Button botonI = findViewById(R.id.buttonI);
        botonI.getBackground().setAlpha(255);
        botonI.setClickable(true);

        Button botonJ = findViewById(R.id.buttonJ);
        botonJ.getBackground().setAlpha(255);
        botonJ.setClickable(true);

        Button botonK = findViewById(R.id.buttonK);
        botonK.getBackground().setAlpha(255);
        botonK.setClickable(true);

        Button botonL = findViewById(R.id.buttonL);
        botonL.getBackground().setAlpha(255);
        botonL.setClickable(true);

        Button botonM = findViewById(R.id.buttonM);
        botonM.getBackground().setAlpha(255);
        botonM.setClickable(true);

        Button botonN = findViewById(R.id.buttonN);
        botonN.getBackground().setAlpha(255);
        botonN.setClickable(true);

        Button botonO = findViewById(R.id.buttonO);
        botonO.getBackground().setAlpha(255);
        botonO.setClickable(true);

        Button botonP = findViewById(R.id.buttonP);
        botonP.getBackground().setAlpha(255);
        botonP.setClickable(true);

        Button botonQ = findViewById(R.id.buttonQ);
        botonQ.getBackground().setAlpha(255);
        botonQ.setClickable(true);

        Button botonR = findViewById(R.id.buttonR);
        botonR.getBackground().setAlpha(255);
        botonR.setClickable(true);

        Button botonS = findViewById(R.id.buttonS);
        botonS.getBackground().setAlpha(255);
        botonS.setClickable(true);

        Button botonT = findViewById(R.id.buttonT);
        botonT.getBackground().setAlpha(255);
        botonT.setClickable(true);

        Button botonU = findViewById(R.id.buttonU);
        botonU.getBackground().setAlpha(255);
        botonU.setClickable(true);

        Button botonV = findViewById(R.id.buttonV);
        botonV.getBackground().setAlpha(255);
        botonV.setClickable(true);

        Button botonW = findViewById(R.id.buttonW);
        botonW.getBackground().setAlpha(255);
        botonW.setClickable(true);

        Button botonX = findViewById(R.id.buttonX);
        botonX.getBackground().setAlpha(255);
        botonX.setClickable(true);

        Button botonY = findViewById(R.id.buttonY);
        botonY.getBackground().setAlpha(255);
        botonY.setClickable(true);

        Button botonZ = findViewById(R.id.buttonZ);
        botonZ.getBackground().setAlpha(255);
        botonZ.setClickable(true);
    }
}