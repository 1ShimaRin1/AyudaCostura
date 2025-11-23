package com.example.ayudacostura.ui.materiales;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ayudacostura.Data.model.Material;
import com.example.ayudacostura.Data.repository.MaterialRepository;

import java.util.List;

public class MaterialViewModel extends ViewModel {

    // Repositorio encargado de comunicarse con Firebase (CRUD de materiales)
    private final MaterialRepository repository = new MaterialRepository();

    // LiveData para enviar mensajes (exitos / errores) a la UI
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    // LiveData que contiene la lista de materiales obtenida desde el repositorio
    private final LiveData<List<Material>> materiales = repository.obtenerMateriales();

    // -------------------------------------------------------------
    // GETTERS
    // -------------------------------------------------------------

    // Devuelve la lista completa de materiales en tiempo real
    public LiveData<List<Material>> getMateriales() {
        return materiales;
    }

    // Devuelve los mensajes emitidos por operaciones del CRUD
    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // -------------------------------------------------------------
    // AGREGAR MATERIAL
    // -------------------------------------------------------------

    // Agregar un material usando campos individuales ingresados por el usuario
    public void agregarMaterial(String nombre, String cantidad, String descripcion, String imagenUrl) {

        // Validación mínima antes de crear el objeto
        if (nombre.isEmpty() || cantidad.isEmpty()) {
            mensaje.setValue("Completa todos los campos obligatorios");
            return;
        }

        // Crear el objeto Material con los datos proporcionados
        Material material = new Material(nombre, cantidad, descripcion, imagenUrl);

        // Enviar el material al repositorio para guardarlo en Firebase
        repository.agregarMaterial(material, new MaterialRepository.OnMaterialAgregadoListener() {
            @Override
            public void onExito(String m) {
                mensaje.setValue(m); // Aviso a la UI que se agregó correctamente
            }

            @Override
            public void onError(String m) {
                mensaje.setValue(m); // Aviso a la UI del error
            }
        });
    }

    // Método alternativo: agregar pasando directamente un objeto Material completo
    public void agregarMaterial(Material material) {
        repository.agregarMaterial(material, new MaterialRepository.OnMaterialAgregadoListener() {
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

    // -------------------------------------------------------------
    // EDITAR MATERIAL
    // -------------------------------------------------------------

    // Permite actualizar un material existente mediante su ID
    public void editarMaterial(Material materialActualizado) {
        repository.editarMaterial(materialActualizado, new MaterialRepository.OnMaterialEditadoListener() {
            @Override
            public void onExito(String m) {
                mensaje.setValue(m); // Notificación de éxito
            }

            @Override
            public void onError(String m) {
                mensaje.setValue(m); // Notificación de error
            }
        });
    }

    // -------------------------------------------------------------
    // ELIMINAR MATERIAL
    // -------------------------------------------------------------

    // Elimina un material usando su ID en Firebase
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

    // -------------------------------------------------------------
    // OBTENER MATERIAL POR ID
    // -------------------------------------------------------------

    // Devuelve un LiveData<Material> con un solo material buscado por su ID
    public LiveData<Material> obtenerMaterialPorId(String id) {
        return repository.obtenerMaterialPorId(id);
    }
}
