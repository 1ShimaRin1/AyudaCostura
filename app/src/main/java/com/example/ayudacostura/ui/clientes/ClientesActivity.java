package com.example.ayudacostura.ui.clientes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.R;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {

    private Button btnAgregarClientes;
    private RecyclerView recyclerClientes;
    private ClienteAdapter adapter;
    private ClientesViewModel viewModel;
    private Button btnVolver;
    private EditText etBuscarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        // ------------------------------------------------------------------
        // VISTAS
        // ------------------------------------------------------------------
        btnVolver = findViewById(R.id.btnVolver);
        btnAgregarClientes = findViewById(R.id.btnAgregarCliente);
        recyclerClientes = findViewById(R.id.recyclerClientes);
        etBuscarCliente = findViewById(R.id.etBuscarCliente);

        btnVolver.setOnClickListener(v -> finish());

        // ------------------------------------------------------------------
        // VIEWMODEL
        // ------------------------------------------------------------------
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // ------------------------------------------------------------------
        // ADAPTER (con editar y eliminar)
        // ------------------------------------------------------------------
        adapter = new ClienteAdapter(new ArrayList<>(), new ClienteAdapter.OnItemClickListener() {

            @Override
            public void onEditClick(Cliente cliente) {
                Intent intent = new Intent(ClientesActivity.this, EditarClienteActivity.class);
                intent.putExtra("clienteId", cliente.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Cliente cliente) {

                // Inflar diseño del diálogo
                android.view.View dialogView = getLayoutInflater()
                        .inflate(R.layout.dialog_eliminar_cliente, null);

                // Referencias
                android.widget.TextView tvMensaje = dialogView.findViewById(R.id.tvDialogMensaje);
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDialog);
                Button btnEliminar = dialogView.findViewById(R.id.btnEliminarDialog);

                // Texto dinámico
                tvMensaje.setText("¿Seguro que deseas eliminar a " + cliente.getNombre() + "?");

                // Crear diálogo
                AlertDialog dialog = new AlertDialog.Builder(ClientesActivity.this)
                        .setView(dialogView)
                        .create();

                dialog.show();

                // Cancelar
                btnCancelar.setOnClickListener(v -> dialog.dismiss());

                // Confirmar eliminación
                btnEliminar.setOnClickListener(v -> {
                    viewModel.eliminarCliente(cliente.getId());
                    Toast.makeText(ClientesActivity.this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        });

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClientes.setAdapter(adapter);

        // ------------------------------------------------------------------
        // OBSERVAR CLIENTES EN TIEMPO REAL
        // ------------------------------------------------------------------
        viewModel.getClientes().observe(this, clientes -> adapter.setClientes(clientes));

        // ------------------------------------------------------------------
        // AGREGAR CLIENTE
        // ------------------------------------------------------------------
        btnAgregarClientes.setOnClickListener(v -> {
            Intent intent = new Intent(ClientesActivity.this, AgregarCliente.class);
            startActivity(intent);
        });

        // ------------------------------------------------------------------
        // BUSCADOR DE CLIENTES (tiempo real)
        // ------------------------------------------------------------------
        etBuscarCliente.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
}
