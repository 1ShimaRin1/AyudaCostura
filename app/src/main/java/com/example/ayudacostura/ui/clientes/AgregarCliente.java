package com.example.ayudacostura.ui.clientes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AgregarCliente extends AppCompatActivity {

    // Vistas del formulario
    private TextInputEditText txtNombre, txtTelefono;
    private MaterialButton btnGuardar, btnVolver;

    // ViewModel encargado de lógica de clientes
    private ClientesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        // ---------------------------------------------------------
        // INICIALIZAR VISTAS
        // ---------------------------------------------------------
        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);

        // Volver cierra la pantalla
        btnVolver.setOnClickListener(v -> finish());

        // Obtener ViewModel
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // ---------------------------------------------------------
        // OBSERVAR MENSAJES DEL VIEWMODEL (resultado de guardar)
        // Cierra actividad si el cliente fue "agregado"
        // ---------------------------------------------------------
        viewModel.getMensaje().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            if (mensaje.toLowerCase().contains("agregado")) {
                finish();
            }
        });

        // ---------------------------------------------------------
        // GUARDAR CLIENTE
        // Valida que el teléfono tenga 8 dígitos y agrega +569
        // ---------------------------------------------------------
        btnGuardar.setOnClickListener(v -> {

            String nombre = txtNombre.getText().toString().trim();
            String telefonoInput = txtTelefono.getText().toString().trim();

            if (telefonoInput.length() != 8) {
                Toast.makeText(this,
                        "Ingresa los 8 dígitos del teléfono",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Chile: siempre se guarda como +569XXXXXXXX
            String telefonoCompleto = "+569" + telefonoInput;

            viewModel.agregarCliente(nombre, telefonoCompleto);
        });
    }
}