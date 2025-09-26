package com.example.ayudacostura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MaterialesActivity extends AppCompatActivity {
    private Button btnCrearMateriales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materiales);

        btnCrearMateriales = findViewById(R.id.btnAgregarPedido);

        btnCrearMateriales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MaterialesActivity.this, AgregarMaterial.class);
                Toast.makeText(MaterialesActivity.this, "Iniciando Crear Materiales", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}