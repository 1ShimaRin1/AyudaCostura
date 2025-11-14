package com.example.ayudacostura.ui.materiales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.R;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    public interface OnItemClickListener {
        void onEditClick(Material material);
        void onDeleteClick(Material material);
    }

    private List<Material> materiales;
    private final OnItemClickListener listener;

    public MaterialAdapter(List<Material> materiales, OnItemClickListener listener) {
        this.materiales = materiales;
        this.listener = listener;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material m = materiales.get(position);
        holder.tvNombre.setText(m.getNombre());
        holder.tvCantidad.setText("Cantidad: " + m.getCantidad());
        holder.tvDescripcion.setText(m.getDescripcion());

        holder.btnEditar.setOnClickListener(v -> listener.onEditClick(m));
        holder.btnEliminar.setOnClickListener(v -> listener.onDeleteClick(m));
    }

    @Override
    public int getItemCount() {
        return materiales != null ? materiales.size() : 0;
    }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvDescripcion;
        Button btnEditar, btnEliminar;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreMaterial);
            tvCantidad = itemView.findViewById(R.id.tvCantidadMaterial);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionMaterial);
            btnEditar = itemView.findViewById(R.id.btnEditarMaterial);
            btnEliminar = itemView.findViewById(R.id.btnEliminarMaterial);
        }
    }
}
