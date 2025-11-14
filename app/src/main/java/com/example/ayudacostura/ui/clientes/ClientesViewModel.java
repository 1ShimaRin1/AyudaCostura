package com.example.ayudacostura.ui.clientes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.Data.repository.ClienteRepository;

import java.util.List;

public class ClientesViewModel extends ViewModel {

    private final ClienteRepository repository = new ClienteRepository();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final LiveData<List<Cliente>> clientes = repository.obtenerClientes();

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<List<Cliente>> getClientes() {
        return clientes;
    }

    // ✅ Método para agregar cliente (ya existente)
    public void agregarCliente(String nombre, String telefono) {
        if (nombre.isEmpty() || telefono.isEmpty()) {
            mensaje.setValue("Completa todos los campos");
            return;
        }

        repository.agregarCliente(nombre, telefono, new ClienteRepository.OnClienteAgregadoListener() {
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

    // ✅ NUEVO: método para eliminar cliente (usado en el botón Eliminar)
    public void eliminarCliente(String clienteId) {
        repository.eliminarCliente(clienteId, new ClienteRepository.OnClienteEliminadoListener() {
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

    // ✅ NUEVO: método para editar cliente (usado en EditarClienteActivity)
    public void editarCliente(String clienteId, String nuevoNombre, String nuevoTelefono) {
        if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty()) {
            mensaje.setValue("Completa todos los campos");
            return;
        }

        repository.actualizarCliente(clienteId, nuevoNombre, nuevoTelefono, new ClienteRepository.OnClienteActualizadoListener() {
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
}