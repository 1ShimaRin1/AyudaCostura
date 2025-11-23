package com.example.ayudacostura.ui.clientes;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.R;

import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    // Lista mostrada y lista original para filtros
    private List<Cliente> clientes;
    private List<Cliente> clientesOriginal;

    // Callbacks para editar y eliminar
    public interface OnItemClickListener {
        void onEditClick(Cliente cliente);
        void onDeleteClick(Cliente cliente);
    }

    private final OnItemClickListener listener;

    public ClienteAdapter(List<Cliente> clientes, OnItemClickListener listener) {
        this.clientes = clientes;
        this.clientesOriginal = new ArrayList<>(clientes); // copia completa
        this.listener = listener;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        this.clientesOriginal = new ArrayList<>(clientes);
        notifyDataSetChanged();
    }

    // ---------------------------------------------------------
    // FILTRO DE BÚSQUEDA (nombre o teléfono)
    // ---------------------------------------------------------
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

    // ---------------------------------------------------------
    // CREAR VIEWHOLDER
    // ---------------------------------------------------------
    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    // ---------------------------------------------------------
    // BIND DE DATOS POR CADA ITEM
    // ---------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);

        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText(cliente.getTelefono());

        // ---------------------------------------------------------
        // LLAMAR CLIENTE (acepta +56XXXXXXXX o 9XXXXXXXX)
        // ---------------------------------------------------------
        holder.btnLlamarCliente.setOnClickListener(v -> {
            String telefono = cliente.getTelefono();

            if (telefono.startsWith("+56") || telefono.matches("^9\\d{8}$")) {

                // Si viene sin +56 se agrega
                if (telefono.matches("^9\\d{8}$")) {
                    telefono = "+56" + telefono;
                }

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telefono));
                v.getContext().startActivity(intent);

            } else {
                Toast.makeText(v.getContext(),
                        "Número de teléfono inválido",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ---------------------------------------------------------
        // EDITAR CLIENTE
        // ---------------------------------------------------------
        holder.btnEditarCliente.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(cliente);
        });

        // ---------------------------------------------------------
        // ELIMINAR CLIENTE
        // ---------------------------------------------------------
        holder.btnEliminarCliente.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(cliente);
        });
    }

    @Override
    public int getItemCount() {
        return clientes != null ? clientes.size() : 0;
    }

    // ---------------------------------------------------------
    // VIEWHOLDER (referencias a vistas)
    // ---------------------------------------------------------
    public static class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvTelefono;
        TextView btnEditarCliente, btnEliminarCliente;
        View btnLlamarCliente;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);

            btnEditarCliente = itemView.findViewById(R.id.btnEditarCliente);
            btnEliminarCliente = itemView.findViewById(R.id.btnEliminarCliente);
            btnLlamarCliente = itemView.findViewById(R.id.btnLlamarCliente);
        }
    }
}
