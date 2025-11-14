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

public class EditarMaterialActivity extends AppCompatActivity {

    private EditText etNombreMaterial, etCantidadMaterial, etDescripcionMaterial;
    private Button btnGuardarCambios, btnCancelar;
    private MaterialViewModel viewModel;
    private String materialId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_material);

        // 🔹 Inicializar vistas
        etNombreMaterial = findViewById(R.id.etNombreMaterial);
        etCantidadMaterial = findViewById(R.id.etCantidadMaterial);
        etDescripcionMaterial = findViewById(R.id.etDescripcionMaterial);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambiosMaterial);
        btnCancelar = findViewById(R.id.btnCancelarMaterial);

        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);

        // 🔹 Obtener el ID del material a editar
        materialId = getIntent().getStringExtra("materialId");
        if (materialId == null) {
            Toast.makeText(this, "Error: ID de material no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 🔹 Cargar los datos actuales del material
        viewModel.obtenerMaterialPorId(materialId).observe(this, material -> {
            if (material != null) {
                etNombreMaterial.setText(material.getNombre());
                etCantidadMaterial.setText(String.valueOf(material.getCantidad()));
                etDescripcionMaterial.setText(material.getDescripcion());
            }
        });

        // 🔹 Botón para guardar cambios
        btnGuardarCambios.setOnClickListener(v -> {
            String nombre = etNombreMaterial.getText().toString().trim();
            String cantidad = etCantidadMaterial.getText().toString().trim();
            String descripcion = etDescripcionMaterial.getText().toString().trim();

            if (nombre.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            Material materialActualizado = new Material(materialId, nombre, cantidad, descripcion);
            viewModel.editarMaterial(materialActualizado);

            Toast.makeText(this, "Material actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        // 🔹 Botón para cancelar y volver atrás
        btnCancelar.setOnClickListener(v -> finish());
    }
}
