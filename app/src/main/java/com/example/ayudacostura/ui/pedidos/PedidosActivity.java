package com.example.ayudacostura.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private PedidoAdapter adapter;
    private PedidoViewModel viewModel;
    private Button btnAgregarPedido;
    private MaterialButton btnVolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        btnAgregarPedido = findViewById(R.id.btnAgregarPedido);

        adapter = new PedidoAdapter(new ArrayList<>());
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
        viewModel.getPedidos().observe(this, pedidos -> adapter.setPedidos(pedidos));

        btnAgregarPedido.setOnClickListener(v -> {
            Toast.makeText(PedidosActivity.this, "Iniciando Crear Pedidos", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AgregarPedido.class));

        });
    }
}
