package com.example.ayudacostura.ui.materiales;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AgregarMaterial extends AppCompatActivity {

    private EditText etNombre, etCantidad, etDescripcion;
    private ImageView imgPreview;
    private MaterialButton btnImagen, btnGuardar, btnVolver;
    private MaterialViewModel viewModel;

    // Uri temporal de la imagen seleccionada
    private Uri imagenUri = null;

    // Código para identificar selección de imagen
    private final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_material);

        // Referencias UI
        etNombre = findViewById(R.id.etNombreMaterial);
        etCantidad = findViewById(R.id.etCantidadMaterial);
        etDescripcion = findViewById(R.id.etDescripcionMaterial);
        imgPreview = findViewById(R.id.imgPreviewMaterial);
        btnImagen = findViewById(R.id.btnSeleccionarImagen);
        btnGuardar = findViewById(R.id.btnGuardarMaterial);
        btnVolver = findViewById(R.id.btnVolver);

        // ViewModel que maneja la lógica y Firebase
        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);

        // Abrir galería para seleccionar imagen
        btnImagen.setOnClickListener(v -> abrirGaleria());

        // Guardar el material en Firebase
        btnGuardar.setOnClickListener(v -> guardarMaterial());

        // Botón para volver atrás
        btnVolver.setOnClickListener(v -> finish());
    }

    private void abrirGaleria() {
        // Abre la galería para elegir imágenes
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Comprueba si se seleccionó una imagen
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();

            // Copia la imagen al almacenamiento interno de la app
            imagenUri = copiarImagenLocalmente(selectedUri);

            // Previsualiza la imagen seleccionada
            Glide.with(this).load(imagenUri).centerCrop().into(imgPreview);
        }
    }

    private Uri copiarImagenLocalmente(Uri uri) {
        // Copia la imagen escogida desde la galería al almacenamiento interno
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), "imagen_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;

            // Copia en bloques de bytes
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.close();
            input.close();

            return Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarMaterial() {
        // Obtiene datos de los campos
        String nombre = etNombre.getText().toString().trim();
        String cantidad = etCantidad.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        // Convierte la imagen a String para almacenar
        String imagen = imagenUri != null ? imagenUri.toString() : "";

        // Validación simple
        if (nombre.isEmpty() || cantidad.isEmpty()) {
            Toast.makeText(this, "Nombre y cantidad son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea el objeto Material
        Material material = new Material(nombre, cantidad, descripcion, imagen);

        // Envía el material al ViewModel
        viewModel.agregarMaterial(material);

        // Confirma al usuario
        Toast.makeText(this, "Material agregado correctamente", Toast.LENGTH_SHORT).show();

        // Cierra la Activity
        finish();
    }
}
