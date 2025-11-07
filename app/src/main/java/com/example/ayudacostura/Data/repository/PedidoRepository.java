package com.example.ayudacostura.Data.repository;

import androidx.annotation.NonNull;

import com.example.ayudacostura.Data.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PedidoRepository {
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pedidos");

    // 🔹 Agregar pedido
    public void agregarPedido(Pedido pedido, OnSuccessListener success, OnFailureListener failure) {
        String id = pedido.getId() != null ? pedido.getId() : UUID.randomUUID().toString();
        pedido.setId(id);

        ref.child(id).setValue(pedido)
                .addOnSuccessListener(aVoid -> success.onSuccess("Pedido agregado correctamente"))
                .addOnFailureListener(e -> failure.onFailure(e));
    }

    // 🔹 Obtener pedidos desde Firebase
    public void obtenerPedidos(OnPedidosCargadosListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Pedido> lista = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Pedido pedido = child.getValue(Pedido.class);
                    lista.add(pedido);
                }
                listener.onPedidosCargados(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    // 🔹 Interfaces para los callbacks
    public interface OnPedidosCargadosListener {
        void onPedidosCargados(List<Pedido> pedidos);
        void onError(Exception e);
    }

    public interface OnSuccessListener {
        void onSuccess(String mensaje);
    }

    public interface OnFailureListener {
        void onFailure(Exception e);
    }
}