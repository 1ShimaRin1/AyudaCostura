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

public class ClientesActivity extends AppCompatActivity {
    private Button btnAgregarClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        btnAgregarClientes = findViewById(R.id.btnAgregarCliente);

        btnAgregarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientesActivity.this, AgregarCliente.class);
                Toast.makeText(ClientesActivity.this, "Iniciando Crear Cliente", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}