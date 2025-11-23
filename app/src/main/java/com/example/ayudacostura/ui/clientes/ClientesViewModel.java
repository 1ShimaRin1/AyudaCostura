package com.example.ayudacostura.ui.clientes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ayudacostura.Data.model.Cliente;
import com.example.ayudacostura.Data.repository.ClienteRepository;

import java.util.List;

public class ClientesViewModel extends ViewModel {

    // Repositorio encargado de manejar Firebase
    private final ClienteRepository repository = new ClienteRepository();

    // LiveData para mostrar mensajes (éxito o error)
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    // LiveData con la lista de clientes desde Firebase
    private final LiveData<List<Cliente>> clientes = repository.obtenerClientes();


    // Getter del mensaje
    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // Getter de la lista de clientes
    public LiveData<List<Cliente>> getClientes() {
        return clientes;
    }


    // -------------------------------------------------------------
    // ➤ AGREGAR CLIENTE
    //   Valida campos y envía la solicitud al repositorio.
    //   Se comunica mediante callbacks que actualizan el LiveData "mensaje".
    // -------------------------------------------------------------
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


    // -------------------------------------------------------------
    // ➤ ELIMINAR CLIENTE
    //   Llama al repositorio y actualiza el LiveData "mensaje".
    // -------------------------------------------------------------
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


    // -------------------------------------------------------------
    // ➤ EDITAR CLIENTE
    //   Valida campos y llama al repositorio para actualizar Firebase.
    // -------------------------------------------------------------
    public void editarCliente(String clienteId, String nuevoNombre, String nuevoTelefono) {
        if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty()) {
            mensaje.setValue("Completa todos los campos");
            return;
        }

        repository.actualizarCliente(clienteId, nuevoNombre, nuevoTelefono,
                new ClienteRepository.OnClienteActualizadoListener() {
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
