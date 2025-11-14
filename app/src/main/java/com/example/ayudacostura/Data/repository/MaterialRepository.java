package com.example.ayudacostura.Data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ayudacostura.Data.model.Material;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaterialRepository {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("materiales");

    // 🔹 Agregar material
    public void agregarMaterial(String nombre, String cantidad, String descripcion, OnMaterialAgregadoListener listener) {
        String id = UUID.randomUUID().toString();
        Material material = new Material(id, nombre, cantidad, descripcion);

        ref.child(id).setValue(material)
                .addOnSuccessListener(aVoid -> listener.onExito("Material agregado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al agregar material: " + e.getMessage()));
    }

    // 🔹 Editar material
    public void editarMaterial(Material material, OnMaterialEditadoListener listener) {
        ref.child(material.getId()).setValue(material)
                .addOnSuccessListener(aVoid -> listener.onExito("Material actualizado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al actualizar: " + e.getMessage()));
    }

    // 🔹 Eliminar material
    public void eliminarMaterial(String id, OnMaterialEliminadoListener listener) {
        ref.child(id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onExito("Material eliminado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al eliminar: " + e.getMessage()));
    }

    // 🔹 Obtener todos los materiales
    public LiveData<List<Material>> obtenerMateriales() {
        MutableLiveData<List<Material>> materialesLiveData = new MutableLiveData<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Material> lista = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Material m = item.getValue(Material.class);
                    lista.add(m);
                }
                materialesLiveData.setValue(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error si se desea
            }
        });

        return materialesLiveData;
    }

    // 🔹 Obtener material por ID
    public LiveData<Material> obtenerMaterialPorId(String id) {
        MutableLiveData<Material> materialLiveData = new MutableLiveData<>();
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Material material = snapshot.getValue(Material.class);
                materialLiveData.setValue(material);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return materialLiveData;
    }

    // Interfaces para callbacks
    public interface OnMaterialAgregadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }

    public interface OnMaterialEditadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }

    public interface OnMaterialEliminadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }
}
