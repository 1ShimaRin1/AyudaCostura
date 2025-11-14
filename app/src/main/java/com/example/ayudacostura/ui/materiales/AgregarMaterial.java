package com.example.ayudacostura.ui.materiales;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

public class AgregarMaterial extends AppCompatActivity {

    private EditText etNombre, etCantidad, etDescripcion;
    private Button btnGuardar;
    private MaterialViewModel viewModel;

    private String materialId = null; // Para saber si se está editando
    private boolean esEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_material);

        etNombre = findViewById(R.id.etNombreMaterial);
        etCantidad = findViewById(R.id.etCantidadMaterial);
        etDescripcion = findViewById(R.id.etDescripcionMaterial);
        btnGuardar = findViewById(R.id.btnGuardarMaterial);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);

        // 🔹 Verificamos si hay datos para editar
        if (getIntent() != null && getIntent().hasExtra("materialId")) {
            esEdicion = true;
            materialId = getIntent().getStringExtra("materialId");
            etNombre.setText(getIntent().getStringExtra("materialNombre"));
            etCantidad.setText(getIntent().getStringExtra("materialCantidad"));
            etDescripcion.setText(getIntent().getStringExtra("materialDescripcion"));
            btnGuardar.setText("Actualizar Material");
        }

        // 🔹 Guardar o actualizar según el caso
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String cantidad = etCantidad.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (esEdicion) {
                Material material = new Material(materialId, nombre, cantidad, descripcion);
                viewModel.editarMaterial(material);
            } else {
                viewModel.agregarMaterial(nombre, cantidad, descripcion);
            }
        });

        // 🔹 Mostrar mensajes del ViewModel
        viewModel.getMensaje().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            if (msg.contains("correctamente")) finish(); // cerrar al guardar/editar bien
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
