package com.example.ayudacostura;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AgregarPedido extends AppCompatActivity {
    private Spinner spinnerOpciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pedido);


        spinnerOpciones = findViewById(R.id.spinnerOpciones);

        String[] opciones = {"En espera", "En Proceso"};

        // Adaptador que conecta los datos con el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opciones);

        // Estilo para cuando se despliega el menú
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asignar el adaptador al Spinner
        spinnerOpciones.setAdapter(adapter);
    }
}