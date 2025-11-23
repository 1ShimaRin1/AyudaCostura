package com.example.ayudacostura.ui.menu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;

import com.example.ayudacostura.ui.clientes.ClientesActivity;
import com.example.ayudacostura.ui.login.MainActivity;
import com.example.ayudacostura.ui.materiales.MaterialesActivity;
import com.example.ayudacostura.ui.pedidos.PedidosActivity;
import com.example.ayudacostura.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    private Button btnPedido, btnCliente, btnMaterial, btnCerrarSesion;
    private TextView tvUbicacion;

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    // Cliente de ubicación de Google
    private FusedLocationProviderClient fusedLocationClient;

    // Callback que recibe las actualizaciones de ubicación
    private LocationCallback locationCallback;

    // Firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Firebase inicial
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tvUbicacion = findViewById(R.id.tvUbicacion);

        // Botones
        btnMaterial = findViewById(R.id.btnMateriales);
        btnPedido = findViewById(R.id.btnPedido);
        btnCliente = findViewById(R.id.btnClientes);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Si el usuario está logueado, solicitar ubicación
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            solicitarPermisosYUbicacion();
        }

        // Navegación a actividades
        btnPedido.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, PedidosActivity.class));
            Toast.makeText(MenuActivity.this, "Iniciando Menu Pedidos", Toast.LENGTH_SHORT).show();
        });

        btnCliente.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, ClientesActivity.class));
            Toast.makeText(MenuActivity.this, "Iniciando Menu Clientes", Toast.LENGTH_SHORT).show();
        });

        btnMaterial.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, MaterialesActivity.class));
            Toast.makeText(MenuActivity.this, "Iniciando Menu Materiales", Toast.LENGTH_SHORT).show();
        });

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);

            // Limpia historial para evitar volver atrás
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });
    }

    // Solicita permiso de ubicación; si ya está otorgado, inicia actualizaciones
    private void solicitarPermisosYUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
        } else {
            iniciarActualizacionUbicacion();
        }
    }

    // Configura la frecuencia y precisión de ubicación, y activa el callback
    private void iniciarActualizacionUbicacion() {

        // Configuración de la solicitud de ubicación
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);      // Cada 10 segundos
        locationRequest.setFastestInterval(5000); // Mínimo 5 segundos
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Recibe las actualizaciones de GPS
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    obtenerCiudad(location.getLatitude(), location.getLongitude());
                }
            }
        };

        // Verificación final de permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Inicia actualizaciones de ubicación en segundo plano
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    // Convierte coordenadas en ciudad y país usando Geocoder
    private void obtenerCiudad(double latitud, double longitud) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> direcciones = geocoder.getFromLocation(latitud, longitud, 1);

            if (direcciones != null && !direcciones.isEmpty()) {

                String ciudad = direcciones.get(0).getLocality();
                String pais = direcciones.get(0).getCountryName();

                // Mostrar en UI
                tvUbicacion.setText("Ubicación: " + ciudad + ", " + pais);

                // Guardar la ubicación en Firebase
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    databaseReference.child(user.getUid())
                            .child("ubicacion")
                            .setValue(ciudad + ", " + pais);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Resultado del permiso solicitado
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {

            // Si el usuario aceptó, inicia la ubicación
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarActualizacionUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
