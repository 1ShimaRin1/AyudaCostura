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

public class MenuActivity extends AppCompatActivity {
    private Button btnPedido,btnCliente,btnMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        btnMaterial = findViewById(R.id.btnMateriales);
        btnPedido= findViewById(R.id.btnPedido);
        btnCliente = findViewById(R.id.btnClientes);



        btnPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PedidosActivity.class);
                Toast.makeText(MenuActivity.this, "Iniciando Menu Pedidos", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ClientesActivity.class);
                Toast.makeText(MenuActivity.this, "Iniciando Menu Clientes", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
        btnMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,MaterialesActivity.class);
                Toast.makeText(MenuActivity.this, "Iniciando Menu Materiales", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}