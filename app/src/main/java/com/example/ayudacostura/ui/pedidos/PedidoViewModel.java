package com.example.ayudacostura.ui.pedidos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.Data.repository.PedidoRepository;
import com.example.ayudacostura.R;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PedidoViewModel extends ViewModel {

    // LiveData donde se almacenan todos los pedidos que se mostrarán en la UI
    private final MutableLiveData<List<Pedido>> pedidosLiveData = new MutableLiveData<>();

    // LiveData para enviar mensajes de éxito o error a la vista
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    // Repositorio encargado de comunicar el ViewModel con Firebase
    private final PedidoRepository repository = new PedidoRepository();

    // -------------------------------
    // OBTENER TODOS LOS PEDIDOS
    // -------------------------------
    public LiveData<List<Pedido>> getPedidos() {
        cargarPedidos(); // Siempre que se llama, se refrescan los datos de Firebase
        return pedidosLiveData;
    }

    // -------------------------------
    // OBTENER UN PEDIDO POR SU ID
    // -------------------------------
    public LiveData<Pedido> obtenerPedidoPorId(String id) {
        MutableLiveData<Pedido> pedidoLiveData = new MutableLiveData<>();

        // Llamada al repositorio para buscar el pedido concreto
        repository.obtenerPedidoPorId(id, new PedidoRepository.OnPedidoCargadoListener() {
            @Override
            public void onPedidoCargado(Pedido pedido) {
                pedidoLiveData.postValue(pedido); // Se envía el pedido a la UI
            }

            @Override
            public void onError(Exception e) {
                mensaje.postValue("Error al cargar pedido: " + e.getMessage());
            }
        });

        return pedidoLiveData;
    }

    // -------------------------------
    // CARGAR LISTA COMPLETA DE PEDIDOS
    // -------------------------------
    public void cargarPedidos() {
        repository.obtenerPedidos(new PedidoRepository.OnPedidosCargadosListener() {
            @Override
            public void onPedidosCargados(List<Pedido> pedidos) {
                pedidosLiveData.postValue(pedidos); // Actualiza LiveData observada por la Activity
            }

            @Override
            public void onError(Exception e) {
                mensaje.postValue("Error al listar pedidos: " + e.getMessage());
            }
        });
    }

    // -------------------------------
    // FILTRAR PEDIDOS POR ESTADO
    // (Pendientes, Completados, etc.)
    // -------------------------------
    public LiveData<List<Pedido>> getPedidosPorEstado(String estado) {
        MutableLiveData<List<Pedido>> filtrados = new MutableLiveData<>();

        // Se vuelve a obtener la lista completa y luego se filtra
        repository.obtenerPedidos(new PedidoRepository.OnPedidosCargadosListener() {
            @Override
            public void onPedidosCargados(List<Pedido> pedidos) {

                List<Pedido> resultado = new ArrayList<>();

                // Filtra según el estado solicitado
                for (Pedido p : pedidos) {
                    if (p.getEstado().equalsIgnoreCase(estado)) {
                        resultado.add(p);
                    }
                }

                filtrados.postValue(resultado);
            }

            @Override
            public void onError(Exception e) {
                mensaje.postValue("Error al filtrar: " + e.getMessage());
            }
        });

        return filtrados;
    }

    // -------------------------------
    // AGREGAR PEDIDO NUEVO
    // -------------------------------
    public void agregarPedido(Pedido pedido) {
        // El repositorio devuelve mensaje de éxito o error
        repository.agregarPedido(
                pedido,
                mensaje::postValue,
                e -> mensaje.postValue(e.getMessage())
        );
    }

    // -------------------------------
    // ACTUALIZAR PEDIDO EXISTENTE
    // (Por ejemplo, marcar como "Completado")
    // -------------------------------
    public void actualizarPedido(Pedido pedido) {
        repository.actualizarPedido(
                pedido,
                mensaje::postValue,
                e -> mensaje.postValue(e.getMessage())
        );
    }

    // -------------------------------
    // ELIMINAR PEDIDO POR ID
    // -------------------------------
    public void eliminarPedido(String id) {
        repository.eliminarPedido(
                id,
                mensaje::postValue,
                e -> mensaje.postValue(e.getMessage())
        );
    }

    // LiveData observado por la UI para mostrar mensajes Toast
    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // -------------------------------
    // VIEW HOLDER (opcional)
    // No afecta al ViewModel, pero queda aquí como utilidad
    // -------------------------------
    static class PedidoViewHolder extends RecyclerView.ViewHolder {

        // Referencias a las vistas del item del RecyclerView
        TextView tvNombre, tvDescripcion, tvTipoCostura, tvEstado, btnCompletar;
        ImageView imgPrenda;
        PedidoViewHolder(View itemView) {
            super(itemView);

            // Inicialización de vistas del layout de cada pedido
            tvNombre = itemView.findViewById(R.id.tvNombrePedido);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPedido);
            tvTipoCostura = itemView.findViewById(R.id.tvTipoCostura);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            imgPrenda = itemView.findViewById(R.id.imgPrendaPedido);
            btnCompletar = itemView.findViewById(R.id.btnCompletarPedido);
        }
    }
}

