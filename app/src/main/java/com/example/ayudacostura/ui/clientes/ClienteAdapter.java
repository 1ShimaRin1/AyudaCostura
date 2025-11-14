package com.example.ayudacostura.ui.clientes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.R;

import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private List<Cliente> clientesOriginal; // ✅ Lista completa sin filtrar

    public interface OnItemClickListener {
        void onEditClick(Cliente cliente);
        void onDeleteClick(Cliente cliente);
    }

    private OnItemClickListener listener;

    public ClienteAdapter(List<Cliente> clientes, OnItemClickListener listener) {
        this.clientes = clientes;
        this.clientesOriginal = new ArrayList<>(clientes); // copia original
        this.listener = listener;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        this.clientesOriginal = new ArrayList<>(clientes); // actualiza original
        notifyDataSetChanged();
    }

    // ✅ Nuevo método para filtrar clientes
    public void filtrar(String texto) {
        List<Cliente> filtrados = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) {
            filtrados.addAll(clientesOriginal);
        } else {
            for (Cliente c : clientesOriginal) {
                if (c.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        c.getTelefono().contains(texto)) {
                    filtrados.add(c);
                }
            }
        }
        this.clientes = filtrados;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText(cliente.getTelefono());

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(cliente);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(cliente);
        });
    }

    @Override
    public int getItemCount() {
        return clientes != null ? clientes.size() : 0;
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono;
        Button btnEditar, btnEliminar;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            btnEditar = itemView.findViewById(R.id.btnEditarCliente);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCliente);
        }
    }
}