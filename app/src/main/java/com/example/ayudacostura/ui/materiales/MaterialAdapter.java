package com.example.ayudacostura.ui.materiales;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.R;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    // Lista completa (todos los materiales)
    private List<Material> listaOriginal;

    // Lista visible (aplica filtro)
    private List<Material> listaFiltrada;

    private OnItemClickListener listener;

    // Eventos de editar y eliminar
    public interface OnItemClickListener {
        void onEditar(Material material);
        void onEliminar(Material material);
    }

    // Constructor: recibe la lista inicial y los listeners
    public MaterialAdapter(List<Material> lista, OnItemClickListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    // Actualiza toda la lista cuando Firebase cambia
    public void actualizarLista(List<Material> nuevaLista) {
        listaOriginal.clear();
        listaOriginal.addAll(nuevaLista);

        listaFiltrada.clear();
        listaFiltrada.addAll(nuevaLista);

        notifyDataSetChanged();
    }

    // Filtro de búsqueda sencillo por nombre
    public void filtrar(String texto) {
        texto = texto.toLowerCase();
        listaFiltrada.clear();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (Material m : listaOriginal) {
                if (m.getNombre().toLowerCase().contains(texto)) {
                    listaFiltrada.add(m);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada item
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new MaterialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material m = listaFiltrada.get(position);

        // Carga imagen con Glide (si no hay imagen, usa placeholder básico)
        if (m.getImagenUrl() != null && !m.getImagenUrl().isEmpty()) {
            Glide.with(holder.imgMaterial.getContext())
                    .load(Uri.parse(m.getImagenUrl()))
                    .centerCrop()
                    .into(holder.imgMaterial);
        } else {
            holder.imgMaterial.setImageResource(R.drawable.ic_launcher_background);
        }

        // Asigna textos
        holder.tvNombre.setText(m.getNombre());
        holder.tvCantidad.setText(m.getCantidad());
        holder.tvDescripcion.setText(m.getDescripcion());

        // Botón de editar → pasa el objeto completo
        holder.btnEditar.setOnClickListener(v -> listener.onEditar(m));

        // Botón de eliminar
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(m));
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMaterial;
        TextView tvNombre, tvCantidad, tvDescripcion;
        TextView btnEditar, btnEliminar;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);

            // Referencias a las vistas del item
            imgMaterial = itemView.findViewById(R.id.imgMaterial);
            tvNombre = itemView.findViewById(R.id.tvNombreMaterial);
            tvCantidad = itemView.findViewById(R.id.tvCantidadMaterial);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionMaterial);
            btnEditar = itemView.findViewById(R.id.btnEditarMaterial);
            btnEliminar = itemView.findViewById(R.id.btnEliminarMaterial);
        }
    }
}
