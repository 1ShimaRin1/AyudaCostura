package com.example.ayudacostura.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

    // Filtro activo
    private String filtroActual = "Pendientes";

    // Lista completa de pedidos (antes de filtrar)
    private List<Pedido> listaOriginal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // ----------------------------------------------------
        // REFERENCIAS UI
        // ----------------------------------------------------
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        btnAgregarPedido = findViewById(R.id.btnAgregarPedido);
        btnVolver = findViewById(R.id.btnVolver);

        btnPendientes = findViewById(R.id.btnPendientes);
        btnCompletados = findViewById(R.id.btnCompletados);

        btnVolver.setOnClickListener(v -> finish());

        // ----------------------------------------------------
        // ADAPTER + LISTENERS
        // ----------------------------------------------------
        adapter = new PedidoAdapter(new ArrayList<>(), new PedidoAdapter.OnItemClickListener() {

            // --- Editar pedido ---
            @Override
            public void onEditClick(Pedido pedido) {
                Intent intent = new Intent(PedidosActivity.this, EditarPedidoActivity.class);
                intent.putExtra("pedidoId", pedido.getId());
                startActivity(intent);
            }

            // --- Eliminar pedido con diálogo ---
            @Override
            public void onDeleteClick(Pedido pedido) {

                View dialogView = getLayoutInflater()
                        .inflate(R.layout.dialog_eliminar_cliente, null);

                TextView titulo = dialogView.findViewById(R.id.tvDialogTitulo);
                TextView mensaje = dialogView.findViewById(R.id.tvDialogMensaje);

                // Ajuste específico para pedidos
                titulo.setText("Eliminar pedido");
                mensaje.setText("¿Seguro que deseas eliminar este pedido?");

                MaterialButton btnCancelar = dialogView.findViewById(R.id.btnCancelarDialog);
                MaterialButton btnEliminar = dialogView.findViewById(R.id.btnEliminarDialog);

                AlertDialog dialog = new AlertDialog.Builder(PedidosActivity.this)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                btnCancelar.setOnClickListener(v -> dialog.dismiss());

                // Acción de eliminar
                btnEliminar.setOnClickListener(v -> {
                    viewModel.eliminarPedido(pedido.getId());   // Elimina en Firebase
                    Toast.makeText(PedidosActivity.this,
                            "Pedido eliminado", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

                dialog.show();
            }

            // --- Completar pedido ---
            @Override
            public void onCompletarClick(Pedido pedido) {
                viewModel.actualizarPedido(pedido);  // Actualiza el estado en Firebase
            }
        });

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setAdapter(adapter);

        // ----------------------------------------------------
        // VIEWMODEL: escucha cambios en Firebase
        // ----------------------------------------------------
        viewModel = new ViewModelProvider(this).get(PedidoViewModel.class);

        viewModel.getPedidos().observe(this, pedidos -> {
            listaOriginal = pedidos;   // Lista total
            aplicarFiltro();           // Aplica el filtro actual (pendientes/completados)
        });

        // ----------------------------------------------------
        // AGREGAR NUEVO PEDIDO
        // ----------------------------------------------------
        btnAgregarPedido.setOnClickListener(v ->
                startActivity(new Intent(this, AgregarPedido.class))
        );

        // ----------------------------------------------------
        // BOTONES DE FILTRO
        // ----------------------------------------------------
        btnPendientes.setOnClickListener(v -> {
            filtroActual = "Pendientes";
            aplicarFiltro();   // Muestra solo los NO completados
        });

        btnCompletados.setOnClickListener(v -> {
            filtroActual = "Completados";
            aplicarFiltro();   // Muestra solo los marcados como completados
        });
    }

    // ----------------------------------------------------
    // FILTRO: construye una lista según el estado del pedido
    // ----------------------------------------------------
    private void aplicarFiltro() {
        List<Pedido> filtrados = new ArrayList<>();

        for (Pedido p : listaOriginal) {

            // Muestra pedidos que NO están completados
            if (filtroActual.equals("Pendientes") &&
                    !"Completado".equalsIgnoreCase(p.getEstado())) {
                filtrados.add(p);
            }

            // Muestra pedidos completados
            if (filtroActual.equals("Completados") &&
                    "Completado".equalsIgnoreCase(p.getEstado())) {
                filtrados.add(p);
            }
        }

        adapter.setPedidos(filtrados);  // Actualiza la lista mostrada en pantalla
    }
}

