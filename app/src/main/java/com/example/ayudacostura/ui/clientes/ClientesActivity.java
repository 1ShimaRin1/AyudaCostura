package com.example.ayudacostura.ui.clientes;

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
import com.example.ayudacostura.Data.model.Cliente;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {

    private Button btnAgregarClientes;
    private RecyclerView recyclerClientes;
    private ClienteAdapter adapter;
    private ClientesViewModel viewModel;
    private MaterialButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // 🔹 Referencias UI
        btnAgregarClientes = findViewById(R.id.btnAgregarCliente);
        recyclerClientes = findViewById(R.id.recyclerClientes);

        // 🔹 Configurar RecyclerView
        adapter = new ClienteAdapter(new ArrayList<>());
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClientes.setAdapter(adapter);

        // 🔹 Conectar ViewModel
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // 🔹 Observar lista de clientes desde Firebase
        viewModel.getClientes().observe(this, clientes -> {
            adapter.setClientes(clientes);
        });

        // 🔹 Botón para ir a pantalla de agregar cliente
        btnAgregarClientes.setOnClickListener(view -> {
            Intent intent = new Intent(ClientesActivity.this, AgregarCliente.class);
            Toast.makeText(ClientesActivity.this, "Iniciando Crear Cliente", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }
}
