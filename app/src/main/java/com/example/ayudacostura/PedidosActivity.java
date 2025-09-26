package com.example.ayudacostura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PedidosActivity extends AppCompatActivity {
    private Button btnAgregarPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pedidos);

        btnAgregarPedidos = findViewById(R.id.btnAgregarPedido);

        btnAgregarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidosActivity.this, AgregarPedido.class);
                Toast.makeText(PedidosActivity.this, "Iniciando Crear Pedidos", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }
}