package com.example.ayudacostura.ui.materiales;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

public class AgregarMaterial extends AppCompatActivity {

    private EditText etNombre, etCantidad, etDescripcion;
    private Button btnGuardar;
    private MaterialViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_material);

        etNombre = findViewById(R.id.etNombreMaterial);
        etCantidad = findViewById(R.id.etCantidadMaterial);
        etDescripcion = findViewById(R.id.etDescripcionMaterial);
        btnGuardar = findViewById(R.id.btnGuardarMaterial);

        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String cantidad = etCantidad.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            viewModel.agregarMaterial(nombre, cantidad, descripcion);
        });

        viewModel.getMensaje().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );
        MaterialButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            finish();
        });

    }
}
