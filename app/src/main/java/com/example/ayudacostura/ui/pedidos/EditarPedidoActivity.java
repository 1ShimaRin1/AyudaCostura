package com.example.ayudacostura.ui.pedidos;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.Data.repository.PedidoRepository;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditarPedidoActivity extends AppCompatActivity {

    // Inputs principales
    private TextInputEditText txtNombre, txtDescripcion, txtMedidas;
    private CheckBox chkMedidas;

    // Botones
    private MaterialButton btnGuardar;
    private MaterialButton btnVolver;

    // Firebase y modelo
    private PedidoRepository repository;
    private Pedido pedido;
    private String pedidoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        // Repositorio para Firebase
        repository = new PedidoRepository();

        // ID recibido desde el intent
        pedidoId = getIntent().getStringExtra("pedidoId");

        // Referencias UI
        txtNombre = findViewById(R.id.etNombrePedido);
        txtDescripcion = findViewById(R.id.etDescripcionPedido);
        txtMedidas = findViewById(R.id.etMedidas);
        chkMedidas = findViewById(R.id.chkMedidas);
        btnGuardar = findViewById(R.id.btnGuardarPedido);
        btnVolver = findViewById(R.id.btnVolver);

        // Botón volver
        btnVolver.setOnClickListener(v -> finish());

        // Por defecto las medidas están desactivadas
        txtMedidas.setEnabled(false);

        // ✔️ Si el checkbox cambia, habilita o limpia el campo de medidas
        chkMedidas.setOnCheckedChangeListener((buttonView, isChecked) -> {
            txtMedidas.setEnabled(isChecked);
            if (!isChecked) txtMedidas.setText("");
        });

        // ✔️ Obtener datos del pedido desde Firebase
        repository.obtenerPedidoPorId(pedidoId, new PedidoRepository.OnPedidoCargadoListener() {
            @Override
            public void onPedidoCargado(Pedido p) {
                pedido = p;
                mostrarDatos();  // Cargar valores en la UI
            }

            @Override
            public void onError(Exception e) {
                // Error al cargar datos (puede loguearse si se desea)
            }
        });

        // ✔️ Guardar cambios del pedido
        btnGuardar.setOnClickListener(v -> {
            if (pedido != null) {

                // Actualizar campos principales
                pedido.setNombre(txtNombre.getText().toString());
                pedido.setDescripcion(txtDescripcion.getText().toString());

                // Manejo lógico del checkbox
                if (chkMedidas.isChecked()) {
                    pedido.setMedidas(txtMedidas.getText().toString());
                } else {
                    pedido.setMedidas("");
                }

                // Guardar en Firebase
                repository.actualizarPedido(
                        pedido,
                        mensaje -> finish(),   // Éxito → cerrar pantalla
                        error -> {}            // Error → puedes loguearlo si quieres
                );
            }
        });
    }

    // ✔️ Cargar datos del pedido en los campos de la UI
    private void mostrarDatos() {
        if (pedido == null) return;

        txtNombre.setText(pedido.getNombre());
        txtDescripcion.setText(pedido.getDescripcion());

        boolean tieneMedidas = pedido.getMedidas() != null && !pedido.getMedidas().isEmpty();

        // ✔️ Manejo del checkbox y habilitación del campo
        chkMedidas.setChecked(tieneMedidas);
        txtMedidas.setEnabled(tieneMedidas);

        txtMedidas.setText(tieneMedidas ? pedido.getMedidas() : "");
    }
}
