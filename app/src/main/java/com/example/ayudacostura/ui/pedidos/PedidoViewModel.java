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

    private final MutableLiveData<List<Pedido>> pedidosLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final PedidoRepository repository = new PedidoRepository();

    // 🔹 Obtener todos los pedidos
    public LiveData<List<Pedido>> getPedidos() {
        cargarPedidos();
        return pedidosLiveData;
    }

    // 🔹 Obtener un pedido por ID
    public LiveData<Pedido> obtenerPedidoPorId(String id) {
        MutableLiveData<Pedido> pedidoLiveData = new MutableLiveData<>();

        repository.obtenerPedidoPorId(id, new PedidoRepository.OnPedidoCargadoListener() {
            @Override
            public void onPedidoCargado(Pedido pedido) {
                pedidoLiveData.postValue(pedido);
            }

            @Override
            public void onError(Exception e) {
                mensaje.postValue("❌ Error al cargar pedido: " + e.getMessage());
            }
        });

        return pedidoLiveData;
    }

    // 🔹 Cargar pedidos
    public void cargarPedidos() {
        repository.obtenerPedidos(new PedidoRepository.OnPedidosCargadosListener() {
            @Override
            public void onPedidosCargados(List<Pedido> pedidos) {
                pedidosLiveData.postValue(pedidos);
            }

            @Override
            public void onError(Exception e) {
                mensaje.postValue("❌ Error al listar pedidos: " + e.getMessage());
            }
        });
    }

    // 🔹 Obtener pedidos filtrados por estado
    public LiveData<List<Pedido>> getPedidosPorEstado(String estado) {
        MutableLiveData<List<Pedido>> filtrados = new MutableLiveData<>();

        repository.obtenerPedidos(new PedidoRepository.OnPedidosCargadosListener() {
            @Override
            public void onPedidosCargados(List<Pedido> pedidos) {
                List<Pedido> resultado = new ArrayList<>();
                for (Pedido p : pedidos) {
                    if (p.getEstado().equalsIgnoreCase(estado)) {
                        resultado.add(p);
                    }
                }
                filtrados.postValue(resultado);
            }

            @Override
            public void onError(Exception e) {
                mensaje.postValue("❌ Error al filtrar: " + e.getMessage());
            }
        });

        return filtrados;
    }

    // 🔹 Agregar pedido
    public void agregarPedido(Pedido pedido) {
        repository.agregarPedido(pedido, mensaje::postValue, e -> mensaje.postValue(e.getMessage()));
    }

    // 🔹 Actualizar pedido
    public void actualizarPedido(Pedido pedido) {
        repository.actualizarPedido(pedido, mensaje::postValue, e -> mensaje.postValue(e.getMessage()));
    }

    // 🔹 Eliminar pedido
    public void eliminarPedido(String id) {
        repository.eliminarPedido(id, mensaje::postValue, e -> mensaje.postValue(e.getMessage()));
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // 🔹 ViewHolder (opcional)
    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvTipoCostura, tvEstado;
        ImageView imgPrenda;
        Button btnCompletar;

        PedidoViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePedido);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPedido);
            tvTipoCostura = itemView.findViewById(R.id.tvTipoCostura);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            imgPrenda = itemView.findViewById(R.id.imgPrendaPedido);
            btnCompletar = itemView.findViewById(R.id.btnCompletarPedido);
        }
    }
}
