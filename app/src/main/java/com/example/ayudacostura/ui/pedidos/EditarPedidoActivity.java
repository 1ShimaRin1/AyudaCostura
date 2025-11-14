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

    private TextInputEditText txtNombre, txtDescripcion, txtMedidas;
    private CheckBox chkMedidas;
    private MaterialButton btnGuardar;

    private PedidoRepository repository;
    private Pedido pedido;
    private String pedidoId;
    private MaterialButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        repository = new PedidoRepository();

        // Recibir ID
        pedidoId = getIntent().getStringExtra("pedidoId");

        // Referencias UI
        txtNombre = findViewById(R.id.etNombrePedido);
        txtDescripcion = findViewById(R.id.etDescripcionPedido);
        txtMedidas = findViewById(R.id.etMedidas);
        chkMedidas = findViewById(R.id.chkMedidas);
        btnGuardar = findViewById(R.id.btnGuardarPedido);

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Desactivar campo medidas por defecto
        txtMedidas.setEnabled(false);

        // Listener del checkbox
        chkMedidas.setOnCheckedChangeListener((buttonView, isChecked) -> {
            txtMedidas.setEnabled(isChecked);
            if (!isChecked) txtMedidas.setText(""); // Si desmarcan, se limpian medidas
        });

        // Cargar el pedido desde Firebase
        repository.obtenerPedidoPorId(pedidoId, new PedidoRepository.OnPedidoCargadoListener() {
            @Override
            public void onPedidoCargado(Pedido p) {
                pedido = p;
                mostrarDatos();
            }

            @Override
            public void onError(Exception e) {
                // Manejo de error si lo deseas
            }
        });

        // Guardar cambios
        btnGuardar.setOnClickListener(v -> {
            if (pedido != null) {
                pedido.setNombre(txtNombre.getText().toString());
                pedido.setDescripcion(txtDescripcion.getText().toString());

                if (chkMedidas.isChecked()) {
                    pedido.setMedidas(txtMedidas.getText().toString());
                } else {
                    pedido.setMedidas(""); // si está desmarcado, eliminar medidas
                }

                repository.actualizarPedido(
                        pedido,
                        mensaje -> finish(),   // Éxito: cerrar activity
                        error -> {}            // Manejo error
                );
            }
        });
    }

    private void mostrarDatos() {
        if (pedido == null) return;

        txtNombre.setText(pedido.getNombre());
        txtDescripcion.setText(pedido.getDescripcion());

        boolean tieneMedidas = pedido.getMedidas() != null && !pedido.getMedidas().isEmpty();

        chkMedidas.setChecked(tieneMedidas);
        txtMedidas.setEnabled(tieneMedidas);

        if (tieneMedidas) {
            txtMedidas.setText(pedido.getMedidas());
        } else {
            txtMedidas.setText("");
        }
    }
}
