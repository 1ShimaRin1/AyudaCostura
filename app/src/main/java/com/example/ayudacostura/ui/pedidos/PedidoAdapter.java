package com.example.ayudacostura.ui.pedidos;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ayudacostura.Data.model.Pedido;
import com.example.ayudacostura.R;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    // ----------------------------------------------------
    //  Interface de eventos
    // ----------------------------------------------------
    public interface OnItemClickListener {
        void onEditClick(Pedido pedido);
        void onDeleteClick(Pedido pedido);
        void onCompletarClick(Pedido pedido);
    }

    private List<Pedido> pedidos;
    private final OnItemClickListener listener;

    public PedidoAdapter(List<Pedido> pedidos, OnItemClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    public void setPedidos(List<Pedido> nuevosPedidos) {
        this.pedidos = nuevosPedidos;
        notifyDataSetChanged();
    }

    // ----------------------------------------------------
    //  Crear ViewHolder
    // ----------------------------------------------------
    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    // ----------------------------------------------------
    //  Vincular datos al ViewHolder
    // ----------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {

        Pedido pedido = pedidos.get(position);

        // ------ Datos ------
        holder.tvNombre.setText(pedido.getNombre());
        holder.tvDescripcion.setText(pedido.getDescripcion());
        holder.tvTipoCostura.setText("Tipo: " + pedido.getTipoCostura());
        holder.tvEstado.setText("Estado: " + pedido.getEstado());
        holder.tvMedidas.setText("Medidas: " +
                (pedido.getMedidas() != null ? pedido.getMedidas() : "N/A"));

        // ------ Imagen ------
        if (pedido.getImagenUrl() != null && !pedido.getImagenUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(pedido.getImagenUrl()))
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imgPrenda);
        } else {
            holder.imgPrenda.setImageResource(R.drawable.placeholder_image);
        }

        // ------ Mostrar/Ocultar Botones según estado ------
        boolean completado = "Completado".equalsIgnoreCase(pedido.getEstado());
        holder.btnCompletar.setVisibility(completado ? View.GONE : View.VISIBLE);
        holder.btnMensaje.setVisibility(completado ? View.VISIBLE : View.GONE);

        // ------ Botón Completar ------
        holder.btnCompletar.setOnClickListener(v -> {
            pedido.setEstado("Completado");
            listener.onCompletarClick(pedido); // se avisa al ViewModel/Activity
            notifyItemChanged(position);

            Toast.makeText(holder.itemView.getContext(),
                    "Pedido marcado como completado",
                    Toast.LENGTH_SHORT).show();
        });

        // ------ Botón Mensaje (copiar) ------
        holder.btnMensaje.setOnClickListener(v -> {

            // Mensaje tipo WhatsApp (solo con el nombre del pedido/cliente)
            String mensaje = "Hola " + pedido.getClienteNombre() +
                    ", tu pedido ya está completado 😊.";

            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager)
                            holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

            android.content.ClipData clip =
                    android.content.ClipData.newPlainText("pedido", mensaje);

            clipboard.setPrimaryClip(clip);

            Toast.makeText(holder.itemView.getContext(),
                    "Mensaje copiado", Toast.LENGTH_SHORT).show();
        });

        // ------ Botones Editar / Eliminar ------
        holder.btnEditar.setOnClickListener(v -> listener.onEditClick(pedido));
        holder.btnEliminar.setOnClickListener(v -> listener.onDeleteClick(pedido));
    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    // ----------------------------------------------------
    //  ViewHolder
    // ----------------------------------------------------
    static class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvDescripcion, tvTipoCostura, tvEstado, tvMedidas;
        ImageView imgPrenda;
        Button btnCompletar, btnEditar, btnEliminar, btnMensaje;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombrePedido);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPedido);
            tvTipoCostura = itemView.findViewById(R.id.tvTipoCostura);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvMedidas = itemView.findViewById(R.id.tvMedidasPedido);
            imgPrenda = itemView.findViewById(R.id.imgPrendaPedido);

            btnCompletar = itemView.findViewById(R.id.btnCompletarPedido);
            btnEditar = itemView.findViewById(R.id.btnEditarPedido);
            btnEliminar = itemView.findViewById(R.id.btnEliminarPedido);
            btnMensaje = itemView.findViewById(R.id.btnMensajePedido);
        }
    }
}
