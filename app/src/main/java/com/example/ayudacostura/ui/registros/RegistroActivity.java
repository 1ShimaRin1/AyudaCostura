package com.example.ayudacostura.ui.registros;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ayudacostura.R;
import com.example.ayudacostura.ui.login.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    // Campos de texto para ingresar email y contraseñas
    private TextInputEditText etEmail, etPassword, etConfirmPassword;

    // Botón para completar el registro
    private MaterialButton btnRegistrar;

    // Instancia de Firebase Authentication para registrar usuarios
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializa FirebaseAuth para poder registrar usuarios
        auth = FirebaseAuth.getInstance();

        // Referencias a vistas de la interfaz
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Enlace para volver a la pantalla de login
        findViewById(R.id.tvLogin).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Cierra esta actividad para evitar volver con el botón atrás
        });

        // Acción principal: registrar usuario
        btnRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    // Método que valida los datos y registra el usuario en Firebase
    private void registrarUsuario() {

        // Obtiene los textos ingresados
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        // Verifica que no haya campos vacíos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica que ambas contraseñas coincidan
        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase requiere mínimo 6 caracteres para la contraseña
        if (pass.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea el usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    // Si todo sale bien
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        // Envia al login después de crear la cuenta
                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    } else {
                        // Si ocurre un error lo muestra al usuario
                        Toast.makeText(this, "Error: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

