package com.example.ayudacostura.ui.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ayudacostura.Data.model.Pedido;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ayudacostura.R;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos;

    public PedidoAdapter(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void setPedidos(List<Pedido> nuevosPedidos) {
        this.pedidos = nuevosPedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.tvNombre.setText(pedido.getNombre());
        holder.tvDescripcion.setText(pedido.getDescripcion());
        holder.tvTipoCostura.setText("Tipo: " + pedido.getTipoCostura());
        holder.tvEstado.setText("Estado: " + pedido.getEstado());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvTipoCostura, tvEstado;

        PedidoViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePedido);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPedido);
            tvTipoCostura = itemView.findViewById(R.id.tvTipoCostura);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
