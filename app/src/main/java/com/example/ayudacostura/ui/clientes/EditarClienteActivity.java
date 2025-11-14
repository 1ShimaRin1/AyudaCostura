package com.example.ayudacostura.ui.clientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ayudacostura.R;
import com.example.ayudacostura.Data.model.Cliente;
import com.google.android.material.button.MaterialButton;

public class EditarClienteActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono;
    private Button btnGuardarCambios, btnCancelar;
    private ClientesViewModel viewModel;
    private String clienteId;
    private MaterialButton btnVolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_cliente);

        // 🔹 Inicializar componentes
        etNombre = findViewById(R.id.etNombreEditar);
        etTelefono = findViewById(R.id.etTelefonoEditar);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());


        // 🔹 Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // 🔹 Recibir ID del cliente desde el intent
        clienteId = getIntent().getStringExtra("clienteId");

        if (clienteId == null || clienteId.isEmpty()) {
            Toast.makeText(this, "Error: cliente no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 🔹 Observar lista de clientes para encontrar el que se va a editar
        viewModel.getClientes().observe(this, clientes -> {
            for (Cliente c : clientes) {
                if (c.getId().equals(clienteId)) {
                    etNombre.setText(c.getNombre());
                    etTelefono.setText(c.getTelefono());
                    break;
                }
            }
        });

        // 🔹 Botón Guardar Cambios
        btnGuardarCambios.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString().trim();
            String nuevoTelefono = etTelefono.getText().toString().trim();

            viewModel.editarCliente(clienteId, nuevoNombre, nuevoTelefono);

            // Mostrar mensaje en base a resultado
            viewModel.getMensaje().observe(this, mensaje -> {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                if (mensaje.contains("actualizado")) {
                    finish(); // Cierra la pantalla si se actualiza correctamente
                }
            });
        });

        // 🔹 Botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }
}
