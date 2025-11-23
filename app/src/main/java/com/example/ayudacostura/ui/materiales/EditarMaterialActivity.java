package com.example.ayudacostura.ui.materiales;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditarMaterialActivity extends AppCompatActivity {

    private EditText etNombreMaterial, etCantidadMaterial, etDescripcionMaterial;
    private Button btnGuardarCambios, btnCancelar, btnSeleccionarImagen;
    private ImageView imgPreviewMaterial;
    private MaterialViewModel viewModel;

    private String materialId;
    private Uri imagenUri = null;
    private final int PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_material);

        // inicializa vistas
        etNombreMaterial = findViewById(R.id.etNombreMaterialEdit);
        etCantidadMaterial = findViewById(R.id.etCantidadMaterialEdit);
        etDescripcionMaterial = findViewById(R.id.etDescripcionMaterialEdit);
        imgPreviewMaterial = findViewById(R.id.imgPreviewMaterialEdit);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagenEdit);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambiosMaterial);
        btnCancelar = findViewById(R.id.btnCancelarMaterial);

        // ViewModel para manejar datos
        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);

        // obtiene el ID recibido
        materialId = getIntent().getStringExtra("materialId");
        if (materialId == null) {
            Toast.makeText(this, "Error: ID de material no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // carga datos del material desde ViewModel/Firebase
        viewModel.obtenerMaterialPorId(materialId).observe(this, material -> {
            if (material != null) {
                etNombreMaterial.setText(material.getNombre());
                etCantidadMaterial.setText(material.getCantidad());
                etDescripcionMaterial.setText(material.getDescripcion());

                // carga imagen previa si existe
                if (material.getImagenUrl() != null && !material.getImagenUrl().isEmpty()) {
                    Glide.with(this)
                            .load(Uri.parse(material.getImagenUrl()))
                            .centerCrop()
                            .into(imgPreviewMaterial);

                    imagenUri = Uri.parse(material.getImagenUrl());
                }
            }
        });

        // boton para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());

        // guardar cambios
        btnGuardarCambios.setOnClickListener(v -> {
            String nombre = etNombreMaterial.getText().toString().trim();
            String cantidad = etCantidadMaterial.getText().toString().trim();
            String descripcion = etDescripcionMaterial.getText().toString().trim();

            if (nombre.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // crea objeto actualizado
            Material materialActualizado = new Material(
                    materialId,
                    nombre,
                    cantidad,
                    descripcion,
                    imagenUri != null ? imagenUri.toString() : ""
            );

            // actualiza en Firebase
            viewModel.editarMaterial(materialActualizado);

            Toast.makeText(this, "Material actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        // cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }

    // abre la galería
    private void abrirGaleria() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    // recibe imagen seleccionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();

            // se copia localmente para evitar problemas de permisos
            imagenUri = copiarImagenLocalmente(selectedUri);

            Glide.with(this)
                    .load(imagenUri)
                    .centerCrop()
                    .into(imgPreviewMaterial);
        }
    }

    // copia imagen a memoria interna → evita acceso denegado al reiniciar app
    private Uri copiarImagenLocalmente(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);

            File file = new File(getFilesDir(), "imagen_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            input.close();
            output.close();

            return Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
