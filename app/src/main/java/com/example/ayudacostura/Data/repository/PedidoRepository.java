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

    // Referencia principal al nodo "pedidos" en Firebase
    private final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("pedidos");

    // ---------------------------------------------------------
    // AGREGAR PEDIDO
    // Genera ID si no existe y lo guarda en Firebase
    // ---------------------------------------------------------
    public void agregarPedido(Pedido pedido, OnSuccessListener success, OnFailureListener failure) {

        String id = (pedido.getId() != null)
                ? pedido.getId()
                : UUID.randomUUID().toString();

        pedido.setId(id);

        ref.child(id).setValue(pedido)
                .addOnSuccessListener(aVoid ->
                        success.onSuccess("Pedido agregado correctamente"))
                .addOnFailureListener(failure::onFailure);
    }

    // ---------------------------------------------------------
    // ACTUALIZAR PEDIDO
    // Evita actualizar si el ID es nulo
    // ---------------------------------------------------------
    public void actualizarPedido(Pedido pedido, OnSuccessListener success, OnFailureListener failure) {

        if (pedido.getId() == null) {
            failure.onFailure(new Exception("ID del pedido es nulo"));
            return;
        }

        ref.child(pedido.getId()).setValue(pedido)
                .addOnSuccessListener(aVoid ->
                        success.onSuccess("Pedido actualizado correctamente"))
                .addOnFailureListener(failure::onFailure);
    }

    // ---------------------------------------------------------
    // OBTENER PEDIDO POR ID (solo una vez)
    // ---------------------------------------------------------
    public void obtenerPedidoPorId(String id, OnPedidoCargadoListener listener) {

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pedido pedido = snapshot.getValue(Pedido.class);
                listener.onPedidoCargado(pedido);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    // ---------------------------------------------------------
    // OBTENER TODOS LOS PEDIDOS (actualizaciones en tiempo real)
    // ---------------------------------------------------------
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

    // ---------------------------------------------------------
    // ELIMINAR PEDIDO
    // ---------------------------------------------------------
    public void eliminarPedido(String id, OnSuccessListener success, OnFailureListener failure) {

        ref.child(id).removeValue()
                .addOnSuccessListener(aVoid ->
                        success.onSuccess("Pedido eliminado correctamente"))
                .addOnFailureListener(failure::onFailure);
    }

    // ---------------------------------------------------------
    // INTERFACES DE CALLBACK
    // ---------------------------------------------------------
    public interface OnPedidosCargadosListener {
        void onPedidosCargados(List<Pedido> pedidos);
        void onError(Exception e);
    }

    public interface OnPedidoCargadoListener {
        void onPedidoCargado(Pedido pedido);
        void onError(Exception e);
    }

    public interface OnSuccessListener {
        void onSuccess(String mensaje);
    }

    public interface OnFailureListener {
        void onFailure(Exception e);
    }
}
