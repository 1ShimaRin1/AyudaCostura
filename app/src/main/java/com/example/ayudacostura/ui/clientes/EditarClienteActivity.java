package com.example.ayudacostura.ui.clientes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditarClienteActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etTelefono;
    private MaterialButton btnGuardarCambios, btnCancelar, btnVolver;
    private ClientesViewModel viewModel;
    private String clienteId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_cliente);

        // -------------------------------------------------------------
        // Inicializar vistas
        // -------------------------------------------------------------
        etNombre = findViewById(R.id.etNombreEditar);
        etTelefono = findViewById(R.id.etTelefonoEditar);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnVolver = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> finish());

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // -------------------------------------------------------------
        // Obtener ID del cliente enviado desde ClientesActivity
        // -------------------------------------------------------------
        clienteId = getIntent().getStringExtra("clienteId");

        if (clienteId == null || clienteId.isEmpty()) {
            Toast.makeText(this, "Error: cliente no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // -------------------------------------------------------------
        // Cargar datos del cliente seleccionado
        // Observa la lista de clientes y busca el que coincida por ID
        // -------------------------------------------------------------
        viewModel.getClientes().observe(this, clientes -> {
            for (Cliente c : clientes) {
                if (c.getId().equals(clienteId)) {

                    etNombre.setText(c.getNombre());

                    // Mostrar solo los últimos 8 dígitos del teléfono
                    String telefono = c.getTelefono();
                    if (telefono.length() >= 8) {
                        etTelefono.setText(telefono.substring(telefono.length() - 8));
                    } else {
                        etTelefono.setText(telefono); // fallback por seguridad
                    }
                    break;
                }
            }
        });

        // -------------------------------------------------------------
        // Guardar cambios del cliente
        // -------------------------------------------------------------
        btnGuardarCambios.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString().trim();
            String telefonoInput = etTelefono.getText().toString().trim();

            // Validar formato del teléfono
            if (telefonoInput.length() != 8) {
                Toast.makeText(this, "Ingresa los 8 dígitos del teléfono", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reconstruir teléfono para guardar en Firebase
            String nuevoTelefono = "+569" + telefonoInput;

            // Ejecutar actualización en el ViewModel
            viewModel.editarCliente(clienteId, nuevoNombre, nuevoTelefono);

            // Escuchar respuesta del repositorio
            viewModel.getMensaje().observe(this, mensaje -> {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

                // Si se actualizó correctamente, cerrar la Activity
                if (mensaje.contains("actualizado")) finish();
            });
        });

        // Botón cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }
}

