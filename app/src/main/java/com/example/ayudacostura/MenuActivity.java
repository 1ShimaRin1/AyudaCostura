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

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    private Button btnPedido,btnCliente,btnMaterial;
    private Button btnCerrarSesion;


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

            btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
        });
    }
}