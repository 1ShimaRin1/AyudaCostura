package com.example.ayudacostura;

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
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializar ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tvUbicacion = findViewById(R.id.tvUbicacion);

        // Botones
        btnMaterial = findViewById(R.id.btnMateriales);
        btnPedido = findViewById(R.id.btnPedido);
        btnCliente = findViewById(R.id.btnClientes);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Mostrar ubicación si usuario ya está logueado
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            solicitarPermisosYUbicacion();
        }

        // Click botones
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

        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void solicitarPermisosYUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            iniciarActualizacionUbicacion();
        }
    }

    private void iniciarActualizacionUbicacion() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    obtenerCiudad(location.getLatitude(), location.getLongitude());
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void obtenerCiudad(double latitud, double longitud) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(latitud, longitud, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                String ciudad = direcciones.get(0).getLocality();
                String pais = direcciones.get(0).getCountryName();

                // Mostrar en pantalla
                tvUbicacion.setText("Ubicación: " + ciudad + ", " + pais);

                // Guardar en Firebase
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    databaseReference.child(user.getUid()).child("ubicacion").setValue(ciudad + ", " + pais);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarActualizacionUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
