package com.example.lab2_20213849;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerForContextMenu((TextView) findViewById(R.id.textView));
        Button boton = findViewById(R.id.button);
        boton.setClickable(false);
        boton.getBackground().setAlpha(128);
        EditText nombre = findViewById(R.id.editTextText2);
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Button botonJugar = findViewById(R.id.button);
                if(charSequence.toString().trim().isEmpty()){
                    botonJugar.setClickable(false);
                    botonJugar.getBackground().setAlpha(128);
                }else{
                    botonJugar.setClickable(true);
                    botonJugar.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_main,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.colorVerde){
            cambiarColorAVerde();
            return true;
        }else if (item.getItemId()==R.id.colorRojo){
            cambiarColorARojo();
            return true;
        }else if (item.getItemId()==R.id.colorMorado){
            cambiarColorAMorado();
            return true;
        }else {
            return super.onContextItemSelected(item);
        }
    }

    public void cambiarColorAVerde(){
        TextView titulo = findViewById(R.id.textView);
        titulo.setTextColor(Color.GREEN);
    }

    public void cambiarColorARojo(){
        TextView titulo = findViewById(R.id.textView);
        titulo.setTextColor(Color.RED);
    }

    public void cambiarColorAMorado(){
        TextView titulo = findViewById(R.id.textView);
        titulo.setTextColor(Color.parseColor("#800080"));
    }

    public void iniciarJuego(View view){
        Intent intent = new Intent(MainActivity.this, TeleGame.class);
        EditText textoNombre = findViewById(R.id.editTextText2);
        intent.putExtra("nombreJugador",textoNombre.getText().toString());
        startActivity(intent);
    }
}