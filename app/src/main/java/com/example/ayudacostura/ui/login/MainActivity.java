package com.example.ayudacostura.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayudacostura.ui.menu.MenuActivity;
import com.example.ayudacostura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencias UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Botón iniciar sesión
        btnLogin.setOnClickListener(v -> loginUser());

        // Botón ir al registro (se vuelve a asignar correctamente en onStart)
        tvRegister.setOnClickListener(v -> {
        });
    }

    private void loginUser() {

        // Obtener datos ingresados
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validaciones de campos vacíos
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Ingresa tu correo");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Ingresa tu contraseña");
            return;
        }

        // Autenticación con Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    // Inicio de sesión correcto
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this,
                                "Bienvenido " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                        finish();
                    }
                    // Error en el login
                    else {
                        Toast.makeText(MainActivity.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Si el usuario ya está logeado, saltar la pantalla de login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
            finish();
        }

        // Botón para ir a la Activity de registro
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    com.example.ayudacostura.ui.registros.RegistroActivity.class);
            startActivity(intent);
        });
    }
}
