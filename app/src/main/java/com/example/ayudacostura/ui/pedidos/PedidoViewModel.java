package com.example.ayudacostura.ui.pedidos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.Data.repository.PedidoRepository;

import java.util.List;

public class PedidoViewModel extends ViewModel {

    private final MutableLiveData<List<Pedido>> pedidosLiveData = new MutableLiveData<>();
    private final PedidoRepository repository = new PedidoRepository();

    // 🔹 Obtener los pedidos en vivo
    public LiveData<List<Pedido>> getPedidos() {
        cargarPedidos();
        return pedidosLiveData;
    }

    // 🔹 Cargar pedidos desde Firebase
    public void cargarPedidos() {
        repository.obtenerPedidos(new PedidoRepository.OnPedidosCargadosListener() {
            @Override
            public void onPedidosCargados(List<Pedido> pedidos) {
                pedidosLiveData.postValue(pedidos);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 🔹 Agregar nuevo pedido
    public void agregarPedido(Pedido pedido, PedidoRepository.OnSuccessListener success, PedidoRepository.OnFailureListener failure) {
        repository.agregarPedido(pedido, success, failure);
    }
}