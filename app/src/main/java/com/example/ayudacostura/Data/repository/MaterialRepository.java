package com.example.ayudacostura.Data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ayudacostura.Data.model.Material;
import com.google.firebase.database.*;

import java.util.*;

public class MaterialRepository {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("materiales");

    public void agregarMaterial(String nombre, String cantidad, String descripcion, OnMaterialAgregadoListener listener) {
        String id = UUID.randomUUID().toString();
        Material material = new Material(id, nombre, cantidad, descripcion);

        ref.child(id).setValue(material)
                .addOnSuccessListener(aVoid -> listener.onExito("Material agregado correctamente"))
                .addOnFailureListener(e -> listener.onError("Error al agregar material: " + e.getMessage()));
    }

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
                // Nada especial
            }
        });

        return materialesLiveData;
    }

    public interface OnMaterialAgregadoListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }
}
