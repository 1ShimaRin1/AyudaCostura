package com.example.ayudacostura.ui.materiales;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.Data.repository.MaterialRepository;

import java.util.List;

public class MaterialViewModel extends ViewModel {

    private final MaterialRepository repository = new MaterialRepository();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final LiveData<List<Material>> materiales = repository.obtenerMateriales();

    // Obtener lista de materiales
    public LiveData<List<Material>> getMateriales() {
        return materiales;
    }

    // Obtener mensajes de operación
    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // Agregar material
    public void agregarMaterial(String nombre, String cantidad, String descripcion) {
        if (nombre.isEmpty() || cantidad.isEmpty() || descripcion.isEmpty()) {
            mensaje.setValue("Completa todos los campos");
            return;
        }

        repository.agregarMaterial(nombre, cantidad, descripcion, new MaterialRepository.OnMaterialAgregadoListener() {
            @Override
            public void onExito(String m) {
                mensaje.setValue(m);
            }

            @Override
            public void onError(String m) {
                mensaje.setValue(m);
            }
        });
    }

    // Eliminar material
    public void eliminarMaterial(String id) {
        repository.eliminarMaterial(id, new MaterialRepository.OnMaterialEliminadoListener() {
            @Override
            public void onExito(String m) {
                mensaje.setValue(m);
            }

            @Override
            public void onError(String m) {
                mensaje.setValue(m);
            }
        });
    }

    // Editar material
    public void editarMaterial(Material materialActualizado) {
        repository.editarMaterial(materialActualizado, new MaterialRepository.OnMaterialEditadoListener() {
            @Override
            public void onExito(String m) {
                mensaje.setValue(m);
            }

            @Override
            public void onError(String m) {
                mensaje.setValue(m);
            }
        });
    }

    // Obtener material por ID (opcional, si necesitas editar un material)
    public LiveData<Material> obtenerMaterialPorId(String id) {
        return repository.obtenerMaterialPorId(id);
    }
}
