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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {

    private Button btnAgregarClientes;
    private RecyclerView recyclerClientes;
    private ClienteAdapter adapter;
    private ClientesViewModel viewModel;
    private MaterialButton btnVolver;
    private EditText etBuscarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        // 🔹 Inicializar vistas
        btnVolver = findViewById(R.id.btnVolver);
        btnAgregarClientes = findViewById(R.id.btnAgregarCliente);
        recyclerClientes = findViewById(R.id.recyclerClientes);
        etBuscarCliente = findViewById(R.id.etBuscarCliente);

        btnVolver.setOnClickListener(v -> finish());

        // 🔹 ViewModel
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // 🔹 Adapter con botones de editar y eliminar
        adapter = new ClienteAdapter(new ArrayList<>(), new ClienteAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Cliente cliente) {
                Intent intent = new Intent(ClientesActivity.this, EditarClienteActivity.class);
                intent.putExtra("clienteId", cliente.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Cliente cliente) {
                // Inflar el diseño personalizado del diálogo
                android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_eliminar_cliente, null);

                // Obtener referencias a los elementos del layout
                android.widget.TextView tvTitulo = dialogView.findViewById(R.id.tvDialogTitulo);
                android.widget.TextView tvMensaje = dialogView.findViewById(R.id.tvDialogMensaje);
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDialog);
                Button btnEliminar = dialogView.findViewById(R.id.btnEliminarDialog);

                // Personalizar texto dinámico
                tvMensaje.setText("¿Seguro que deseas eliminar a " + cliente.getNombre() + "?");

                // Crear y mostrar el diálogo
                AlertDialog dialog = new AlertDialog.Builder(ClientesActivity.this)
                        .setView(dialogView)
                        .create();

                dialog.show();

                // Acciones de los botones
                btnCancelar.setOnClickListener(v -> dialog.dismiss());

                btnEliminar.setOnClickListener(v -> {
                    viewModel.eliminarCliente(cliente.getId());
                    Toast.makeText(ClientesActivity.this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        });

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClientes.setAdapter(adapter);

        // 🔹 Observar lista de clientes
        viewModel.getClientes().observe(this, clientes -> adapter.setClientes(clientes));

        // 🔹 Botón agregar cliente
        btnAgregarClientes.setOnClickListener(v -> {
            Intent intent = new Intent(ClientesActivity.this, AgregarCliente.class);
            startActivity(intent);
        });

        // 🔹 Buscador en tiempo real
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