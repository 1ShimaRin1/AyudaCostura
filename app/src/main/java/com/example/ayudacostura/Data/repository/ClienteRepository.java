package com.example.ayudacostura.Data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ayudacostura.Data.model.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository encargado de manejar operaciones CRUD sobre los clientes usando Firebase Realtime Database.
 */
public class ClienteRepository {

    // Referencia a la rama "clientes" en Firebase
    private final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("clientes");

    /**
     * Agregar un nuevo cliente a Firebase.
     * Se genera un UUID como identificador único.
     */
    public void agregarCliente(String nombre, String telefono, OnClienteAgregadoListener listener) {
        String id = UUID.randomUUID().toString();
        Cliente cliente = new Cliente(id, nombre, telefono);

        ref.child(id).setValue(cliente)
                .addOnSuccessListener(aVoid -> listener.onExito("Cliente agregado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al agregar cliente: " + e.getMessage()));
    }

    /**
     * Obtener lista de clientes en tiempo real.
     * El LiveData se actualiza automáticamente al cambiar cualquier dato en Firebase.
     */
    public LiveData<List<Cliente>> obtenerClientes() {
        MutableLiveData<List<Cliente>> clientesLiveData = new MutableLiveData<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Cliente> lista = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Cliente cliente = child.getValue(Cliente.class);
                    lista.add(cliente);
                }

                clientesLiveData.setValue(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Si Firebase falla, se devuelve lista vacía para evitar crasheos
                clientesLiveData.setValue(new ArrayList<>());
            }
        });

        return clientesLiveData;
    }

    /**
     * Eliminar cliente según su ID en Firebase.
     */
    public void eliminarCliente(String clienteId, OnClienteEliminadoListener listener) {
        ref.child(clienteId).removeValue()
                .addOnSuccessListener(aVoid -> listener.onExito("Cliente eliminado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al eliminar cliente: " + e.getMessage()));
    }

    /**
     * Actualizar los campos de un cliente específico.
     * Solo actualiza nombre y teléfono.
     */
    public void actualizarCliente(String clienteId, String nombre, String telefono,
                                  OnClienteActualizadoListener listener) {

        ref.child(clienteId).child("nombre").setValue(nombre);
        ref.child(clienteId).child("telefono").setValue(telefono)
                .addOnSuccessListener(aVoid -> listener.onExito("Cliente actualizado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al actualizar cliente: " + e.getMessage()));
    }

    // Callbacks para manejar respuestas del Repository
    public interface OnClienteAgregadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }

    public interface OnClienteEliminadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }

    public interface OnClienteActualizadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }
}