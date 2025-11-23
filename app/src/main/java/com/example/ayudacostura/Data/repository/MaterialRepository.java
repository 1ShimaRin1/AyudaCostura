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

/**
 * Repository encargado de manejar la información de Materiales
 * usando Firebase Realtime Database.
 */
public class MaterialRepository {

    // Referencia a la rama "materiales" en Firebase
    private final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("materiales");

    /**
     * Agregar un material.
     * Si el material no trae un ID, se genera uno automáticamente.
     */
    public void agregarMaterial(Material material, OnMaterialAgregadoListener listener) {

        String id = material.getId();

        // Si el material no tiene ID, se crea uno nuevo
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
            material.setId(id);
        }

        ref.child(id).setValue(material)
                .addOnSuccessListener(aVoid -> listener.onExito("Material agregado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al agregar material: " + e.getMessage()));
    }

    /**
     * Editar un material completo.
     * Se sobreescribe todo el nodo correspondiente al ID.
     */
    public void editarMaterial(Material material, OnMaterialEditadoListener listener) {

        ref.child(material.getId()).setValue(material)
                .addOnSuccessListener(aVoid -> listener.onExito("Material actualizado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al actualizar: " + e.getMessage()));
    }

    /**
     * Eliminar un material por su ID.
     */
    public void eliminarMaterial(String id, OnMaterialEliminadoListener listener) {

        ref.child(id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onExito("Material eliminado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al eliminar: " + e.getMessage()));
    }

    /**
     * Obtener lista de materiales en tiempo real.
     * LiveData se actualiza automáticamente cada vez que Firebase cambia.
     */
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
                // No se hace nada para evitar crasheos
            }
        });

        return materialesLiveData;
    }

    /**
     * Obtener un material específico por su ID.
     * Solo se lee una vez (no se queda escuchando cambios).
     */
    public LiveData<Material> obtenerMaterialPorId(String id) {

        MutableLiveData<Material> materialLiveData = new MutableLiveData<>();

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Material material = snapshot.getValue(Material.class);
                materialLiveData.setValue(material);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // No se requiere manejo adicional
            }
        });

        return materialLiveData;
    }

    // Interfaces de callbacks para comunicar éxito o error

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
