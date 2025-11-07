package com.example.ayudacostura.ui.materiales;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MaterialesActivity extends AppCompatActivity {

    private Button btnAgregarMaterial;
    private MaterialButton btnVolver;
    private RecyclerView recyclerMateriales;
    private MaterialAdapter adapter;
    private MaterialViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materiales);

        // Inicializar vistas
        btnAgregarMaterial = findViewById(R.id.btnAgregarMaterial);
        btnVolver = findViewById(R.id.btnVolver);
        recyclerMateriales = findViewById(R.id.recyclerMateriales);

        // Configurar RecyclerView
        adapter = new MaterialAdapter(new ArrayList<>());
        recyclerMateriales.setLayoutManager(new LinearLayoutManager(this));
        recyclerMateriales.setAdapter(adapter);

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);
        viewModel.getMateriales().observe(this, materiales -> adapter.setMateriales(materiales));

        // Botón para agregar material
        btnAgregarMaterial.setOnClickListener(v -> {
            Intent intent = new Intent(MaterialesActivity.this, AgregarMaterial.class);
            Toast.makeText(MaterialesActivity.this, "Iniciando Crear Material", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        // Botón para volver atrás
        btnVolver.setOnClickListener(v -> finish());
    }
}