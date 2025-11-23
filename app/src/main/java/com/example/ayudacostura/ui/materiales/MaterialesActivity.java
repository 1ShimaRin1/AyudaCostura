package com.example.ayudacostura.ui.materiales;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MaterialesActivity extends AppCompatActivity {

    private MaterialButton btnVolver;
    private Button btnAgregarMaterial;
    private RecyclerView recyclerMateriales;
    private MaterialAdapter adapter;
    private MaterialViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materiales);

        // Inicializar vistas
        btnVolver = findViewById(R.id.btnVolver);
        btnAgregarMaterial = findViewById(R.id.btnAgregarMaterial);
        recyclerMateriales = findViewById(R.id.recyclerMateriales);

        // Botón volver simplemente cierra la Activity
        btnVolver.setOnClickListener(v -> finish());

        // ViewModel
        viewModel = new ViewModelProvider(this).get(MaterialViewModel.class);

        // Adapter con acciones de editar y eliminar
        adapter = new MaterialAdapter(new ArrayList<>(), new MaterialAdapter.OnItemClickListener() {
            @Override
            public void onEditar(Material material) {
                // Ir a pantalla EditarMaterialActivity
                Intent intent = new Intent(MaterialesActivity.this, EditarMaterialActivity.class);
                intent.putExtra("materialId", material.getId());
                startActivity(intent);
            }

            @Override
            public void onEliminar(Material material) {
                // Mostrar diálogo personalizado
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_eliminar_cliente, null);

                android.widget.TextView tvTitulo = dialogView.findViewById(R.id.tvDialogTitulo);
                android.widget.TextView tvMensaje = dialogView.findViewById(R.id.tvDialogMensaje);
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDialog);
                Button btnEliminar = dialogView.findViewById(R.id.btnEliminarDialog);

                tvTitulo.setText("Eliminar Material");
                tvMensaje.setText("¿Seguro que deseas eliminar el material \"" + material.getNombre() + "\"?");

                AlertDialog dialog = new AlertDialog.Builder(MaterialesActivity.this)
                        .setView(dialogView)
                        .create();

                dialog.show();

                btnCancelar.setOnClickListener(v -> dialog.dismiss());

                btnEliminar.setOnClickListener(v -> {
                    viewModel.eliminarMaterial(material.getId());
                    Toast.makeText(MaterialesActivity.this, "Material eliminado", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        });

        // Configuración del RecyclerView
        recyclerMateriales.setLayoutManager(new LinearLayoutManager(this));
        recyclerMateriales.setAdapter(adapter);

        // Observar datos desde Firebase
        viewModel.getMateriales().observe(this, materiales -> adapter.actualizarLista(materiales));

        // Botón para agregar nuevo material
        btnAgregarMaterial.setOnClickListener(v -> {
            Intent intent = new Intent(MaterialesActivity.this, AgregarMaterial.class);
            startActivity(intent);
        });

        // Búsqueda en tiempo real
        EditText searchView = findViewById(R.id.searchViewMateriales);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
