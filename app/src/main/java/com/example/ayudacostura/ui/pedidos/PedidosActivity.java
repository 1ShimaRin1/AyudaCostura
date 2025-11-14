package com.example.ayudacostura.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private PedidoAdapter adapter;
    private PedidoViewModel viewModel;

    private Button btnPendientes, btnCompletados, btnAgregarPedido;
    private MaterialButton btnVolver;

    private String filtroActual = "Pendientes"; // Filtro por defecto
    private List<Pedido> listaOriginal = new ArrayList<>(); // Lista sin filtrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // -------------------------
        // VISTAS
        // -------------------------
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        btnAgregarPedido = findViewById(R.id.btnAgregarPedido);
        btnVolver = findViewById(R.id.btnVolver);

        btnPendientes = findViewById(R.id.btnPendientes);
        btnCompletados = findViewById(R.id.btnCompletados);

        btnVolver.setOnClickListener(v -> finish());

        // -------------------------
        // ADAPTER
        // -------------------------
        adapter = new PedidoAdapter(new ArrayList<>(), new PedidoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Pedido pedido) {
                Intent intent = new Intent(PedidosActivity.this, EditarPedidoActivity.class);
                intent.putExtra("pedidoId", pedido.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Pedido pedido) {
                new AlertDialog.Builder(PedidosActivity.this)
                        .setTitle("Eliminar pedido")
                        .setMessage("¿Seguro que deseas eliminar este pedido?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            viewModel.eliminarPedido(pedido.getId());
                            Toast.makeText(PedidosActivity.this, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onCompletarClick(Pedido pedido) {
                viewModel.actualizarPedido(pedido); // Guarda estado en Firebase
            }
        });

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setAdapter(adapter);

        // -------------------------
        // VIEWMODEL
        // -------------------------
        viewModel = new ViewModelProvider(this).get(PedidoViewModel.class);

        viewModel.getPedidos().observe(this, pedidos -> {
            listaOriginal = pedidos;     // Guardamos la lista completa
            aplicarFiltro();             // Aplicamos el filtro activo al iniciar
        });

        // -------------------------
        // BOTÓN AGREGAR
        // -------------------------
        btnAgregarPedido.setOnClickListener(v ->
                startActivity(new Intent(this, AgregarPedido.class))
        );

        // -------------------------
        // BOTONES DE FILTRO
        // -------------------------
        btnPendientes.setOnClickListener(v -> {
            filtroActual = "Pendientes";
            aplicarFiltro();
        });

        btnCompletados.setOnClickListener(v -> {
            filtroActual = "Completados";
            aplicarFiltro();
        });
    }

    // -------------------------
    // FILTRO GENERAL
    // -------------------------
    private void aplicarFiltro() {
        List<Pedido> filtrados = new ArrayList<>();

        for (Pedido p : listaOriginal) {
            if (filtroActual.equals("Pendientes") &&
                    !"Completado".equalsIgnoreCase(p.getEstado())) {
                filtrados.add(p);
            }

            if (filtroActual.equals("Completados") &&
                    "Completado".equalsIgnoreCase(p.getEstado())) {
                filtrados.add(p);
            }
        }

        adapter.setPedidos(filtrados);
    }
}
