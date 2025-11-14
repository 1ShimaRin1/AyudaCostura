package com.example.ayudacostura.ui.pedidos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarPedido extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText etNombre, etDescripcion, etMedidas;
    private Spinner spnTipoCostura, spnCliente;
    private CheckBox chkMedidas;
    private Button btnGuardar, btnTomarFoto;
    private MaterialButton btnVolver;
    private ImageView imgPrenda;
    private Uri imageUri;
    private List<String> listaClientes = new ArrayList<>();

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pedido);

        // 🔹 Vincular vistas
        etNombre = findViewById(R.id.etNombrePedido);
        etDescripcion = findViewById(R.id.etDescripcionPedido);
        spnTipoCostura = findViewById(R.id.spnTipoCostura);
        chkMedidas = findViewById(R.id.chkMedidas);
        etMedidas = findViewById(R.id.etMedidas);
        btnGuardar = findViewById(R.id.btnGuardarPedido);
        btnVolver = findViewById(R.id.btnVolver);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        imgPrenda = findViewById(R.id.imgPrenda);
        spnCliente = findViewById(R.id.spnCliente); // 🧩 FALTABA ESTO

        etMedidas.setEnabled(false);
        chkMedidas.setOnCheckedChangeListener((b, isChecked) -> etMedidas.setEnabled(isChecked));

        // 🔹 Spinner Tipo de Costura
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipos_costura, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoCostura.setAdapter(adapter);

        // 🔹 Conexión a Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("pedidos");

        // 🔹 Cargar clientes desde Firebase
        cargarClientes();

        // 🔹 Botones
        btnTomarFoto.setOnClickListener(v -> verificarPermisosYTomarFoto());
        btnGuardar.setOnClickListener(v -> guardarPedido());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarClientes() {
        DatabaseReference clientesRef = FirebaseDatabase.getInstance().getReference("clientes");
        clientesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaClientes.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String nombreCliente = child.child("nombre").getValue(String.class);
                    if (nombreCliente != null) listaClientes.add(nombreCliente);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AgregarPedido.this,
                        android.R.layout.simple_spinner_item,
                        listaClientes
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCliente.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void verificarPermisosYTomarFoto() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
        } else {
            abrirCamara();
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File fotoArchivo = crearArchivoImagen();
            if (fotoArchivo != null) {
                imageUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        fotoArchivo
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File crearArchivoImagen() {
        String nombreArchivo = "pedido_" + System.currentTimeMillis();
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(nombreArchivo, ".jpg", directorio);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && imageUri != null) {
            imgPrenda.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarPedido() {
        // 🧩 Previene NullPointer
        if (spnCliente == null || spnCliente.getSelectedItem() == null) {
            Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String clienteNombre = spnCliente.getSelectedItem().toString();
        String descripcion = etDescripcion.getText().toString().trim();
        String tipo = spnTipoCostura.getSelectedItem().toString();
        String medidas = chkMedidas.isChecked() ? etMedidas.getText().toString().trim() : "";
        String estado = "Pendiente";

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Crear y guardar pedido
        String id = dbRef.push().getKey();
        Pedido pedido = new Pedido(id, nombre, descripcion, tipo, medidas, estado, clienteNombre);

        if (imageUri != null) pedido.setImagenUrl(imageUri.toString());

        if (id != null) {
            dbRef.child(id).setValue(pedido)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Pedido agregado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}