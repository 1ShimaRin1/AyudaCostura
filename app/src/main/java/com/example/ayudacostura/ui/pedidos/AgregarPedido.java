package com.example.ayudacostura.ui.pedidos;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarPedido extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etMedidas;
    private Spinner spnTipoCostura;
    private CheckBox chkMedidas;
    private Button btnGuardar;
    private MaterialButton btnVolver;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pedido);

        // 🔹 Referencias UI
        etNombre = findViewById(R.id.etNombrePedido);
        etDescripcion = findViewById(R.id.etDescripcionPedido);
        spnTipoCostura = findViewById(R.id.spnTipoCostura);
        chkMedidas = findViewById(R.id.chkMedidas);
        etMedidas = findViewById(R.id.etMedidas);
        btnGuardar = findViewById(R.id.btnGuardarPedido);
        btnVolver = findViewById(R.id.btnVolver);

        // 🔹 Desactivar campo medidas si no está marcado
        etMedidas.setEnabled(false);
        chkMedidas.setOnCheckedChangeListener((buttonView, isChecked) -> etMedidas.setEnabled(isChecked));

        // 🔹 Configurar Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipos_costura,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoCostura.setAdapter(adapter);

        // 🔹 Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("pedidos");

        // 🔹 Eventos
        btnGuardar.setOnClickListener(v -> guardarPedido());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void guardarPedido() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String tipo = spnTipoCostura.getSelectedItem().toString();
        String medidas = chkMedidas.isChecked() ? etMedidas.getText().toString().trim() : "";
        String estado = "Pendiente";

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Crear y guardar el pedido
        String id = dbRef.push().getKey();
        Pedido pedido = new Pedido(id, nombre, descripcion, tipo, medidas, estado);

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
