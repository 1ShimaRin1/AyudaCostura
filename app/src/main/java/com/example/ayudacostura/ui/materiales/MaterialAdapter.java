package com.example.ayudacostura.ui.materiales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.R;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private List<Material> materiales;

    public MaterialAdapter(List<Material> materiales) {
        this.materiales = materiales;
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
    }

    @Override
    public int getItemCount() {
        return materiales.size();
    }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvDescripcion;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreMaterial);
            tvCantidad = itemView.findViewById(R.id.tvCantidadMaterial);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionMaterial);
        }
    }
}
