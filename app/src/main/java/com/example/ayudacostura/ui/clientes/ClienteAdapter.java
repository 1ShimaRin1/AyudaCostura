package com.example.ayudacostura.ui.clientes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.R;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;

    public ClienteAdapter(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText(cliente.getTelefono());
    }

    @Override
    public int getItemCount() {
        return clientes != null ? clientes.size() : 0;
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
        }
    }
}