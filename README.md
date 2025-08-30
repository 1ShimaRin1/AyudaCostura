## Nombre de la App

AyudaCostura.

## Propósito y problema que resuelve (¿para quién? ¿qué aporta?)

Propósito: Ayudar a organizar pedidos, clientes y materiales de un pequeño negocio de costura.

Problema: Actualmente, los pedidos y fechas de entrega se llevan en papel, lo que puede generar pérdidas de información o desorden.

¿Para quién?: Para la dueña del negocio (mi mamá) o cualquier persona que tenga un pequeño taller de costura.

¿Qué aporta?: Un registro digital de pedidos, clientes e inventario, con acceso rápido y seguro mediante inicio de sesión con Gmail.

## Pantallas iniciales (Activities)

LoginActivity – Inicio de sesión con Google (Firebase Authentication).

MenuActivity – Menú principal con botones para Pedidos, Clientes y Materiales.

PedidosActivity – Lista de pedidos (pendientes y entregados).

AgregarPedidoActivity – Formulario para registrar un pedido.

ClientesActivity – Lista de clientes frecuentes.

MaterialesActivity – Inventario de telas, hilos y accesorios.

## Navegación entre pantallas (¿qué Intents usarás? ¿qué datos pasarás en extras?)

De LoginActivity → MenuActivity:

Intent explícito pasando nombre y correo del usuario (putExtra("nombreUsuario", nombre)).

De MenuActivity → PedidosActivity, ClientesActivity o MaterialesActivity:

Intent explícito sin necesidad de extras.

De PedidosActivity → AgregarPedidoActivity:

Intent explícito para abrir formulario.

De ClientesActivity → WhatsApp o llamada:

Intent implícito para abrir WhatsApp o marcador telefónico con el número del cliente.

## Componentes de Android que se prevé usar

Activities e Intents (para navegación).

RecyclerView (para mostrar listas de pedidos y clientes).

Firebase Authentication (para login con Gmail).

SQLite o Firebase Firestore (para guardar pedidos y clientes).

SharedPreferences (para guardar datos temporales del usuario).

Camera Intent (para tomar fotos de pedidos y guardarlas).

Toasts y AlertDialogs (para notificaciones sencillas).

### Activities, Intents (obligatorio describir)

LoginActivity → MenuActivity: Intent explícito con datos del usuario.

MenuActivity → PedidosActivity: Intent explícito simple.

PedidosActivity → AgregarPedidoActivity: Intent explícito para registrar nuevo pedido.

ClientesActivity → WhatsApp / Llamada: Intent implícito para abrir app externa con datos del cliente.

### ¿Service? ¿BroadcastReceiver? ¿Content Provider?

Service: No aplica por ahora.

BroadcastReceiver: Puede aplicarse en el futuro para detectar si hay conexión a Internet antes de sincronizar datos.

Content Provider: No aplica por ahora.

## Datos (¿qué datos manejarás? fuentes internas/externas)

Pedidos: nombre cliente, fecha entrega, descripción prenda, precio, estado (pendiente/entregado), foto de referencia.

Clientes: nombre, teléfono, correo.

Materiales: tipo de tela, cantidad disponible, accesorios.

Fuente de datos: almacenamiento local (SQLite) inicialmente, con posibilidad de sincronizar en la nube (Firestore).

## Riesgos o desafíos iniciales (2–3 puntos)

Integrar correctamente Firebase Authentication para inicio de sesión con Google.

Manejar el guardado de datos sin conexión usando SQLite.

Organizar la navegación entre Activities de manera clara.
