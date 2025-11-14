package com.example.ayudacostura.ui.clientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

public class AgregarCliente extends AppCompatActivity {

    private EditText txtNombre, txtTelefono;
    private Button btnGuardar;
    private ClientesViewModel viewModel;
    private MaterialButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);


        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Vincular ViewModel
        viewModel = new ViewModelProvider(this).get(ClientesViewModel.class);

        // Observa mensajes desde el ViewModel
        viewModel.getMensaje().observe(this, mensaje -> {
            Toast.makeText(AgregarCliente.this, mensaje, Toast.LENGTH_SHORT).show();

            // 🔹 Si se agregó correctamente, volver a la pantalla anterior
            if (mensaje.toLowerCase().contains("agregado correctamente")) {
                finish();
            }
        });

        // Evento del botón
        btnGuardar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString().trim();
            String telefono = txtTelefono.getText().toString().trim();
            viewModel.agregarCliente(nombre, telefono);
        });
    }
}
