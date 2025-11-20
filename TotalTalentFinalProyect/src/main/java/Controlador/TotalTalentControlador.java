package Controlador;

import java.util.List;
import java.util.Scanner;

import Modelo.Contrato;
import Modelo.Empleado;
import Modelo.Facade.Facade; // Importamos el Facade, que es la puerta de entrada al Modelo.
import Modelo.Rol;
import Modelo.Usuario;

// --- Controlador Principal del Sistema ---
// Esta clase, TotalTalentControlador, es el cerebro de la aplicación.
// Conecta la Vista (la interfaz gráfica de usuario) con el Modelo (la lógica de negocio).
// Recibe las peticiones de la Vista (ej. un clic en "login") y le pide
// al Facade del Modelo que ejecute la acción correspondiente.
public class TotalTalentControlador {

    // Instancia del Facade. Usamos este objeto para comunicarnos con el Modelo.
    private Facade facade;
    // Scanner para la entrada por consola (usado para las pruebas iniciales).
    private Scanner scanner;
    // Contador para los intentos fallidos de inicio de sesión.
    private int intentosLogin;

    // Constructor que inicializa el Facade y el Scanner.
    public TotalTalentControlador() {
        this.facade = new Facade(); // Crea la instancia del Facade.
        this.scanner = new Scanner(System.in);
        this.intentosLogin = 0;
    }

    // Método para manejar el login del usuario. Es llamado por la LoginVista.
    public boolean login(String nombreUsuario, String contrasena) {
        if (intentosLogin >= 3) {
            // Si se superan los 3 intentos, la función retorna falso.
            return false;
        }

        // Delega la validación de credenciales al Facade.
        boolean exito = facade.login(nombreUsuario, contrasena);
        if (exito) {
            intentosLogin = 0; // Reinicia el contador si el login es exitoso.
        } else {
            intentosLogin++; // Aumenta el contador si el login falla.
        }
        return exito;
    }

    // Devuelve el objeto Usuario que está actualmente logueado.
    // Lo usan las vistas (DashboardVista y las Strategies) para saber quién es.
    public Usuario getUsuarioActual() {
        return facade.getUsuarioActual();
    }

    // Verifica si hay un usuario en la sesión.
    public boolean hayUsuarioLogueado() {
        return facade.hayUsuarioLogueado();
    }

    // Llama al Facade para cerrar la sesión del usuario actual.
    // Se usa desde el botón "Cerrar Sesión" del DashboardVista.
    public void logout() {
        facade.logout();
    }

    // --- Métodos de Consola (Versión Antigua/Pruebas) ---
    
    // Lógica para los menús de la versión de consola.
    // Determina qué menú de consola mostrar según el rol del usuario.
    public void mostrarDashboardSegunRol() {
        Usuario usuarioActual = facade.getUsuarioActual();
        if (usuarioActual == null) {
            // No hay usuario logueado.
            return;
        }

        Rol rol = usuarioActual.getRol(); // Obtiene el rol (ADMINISTRADOR, GERENTE, etc.).

        // Selecciona el menú de consola apropiado.
        switch (rol) {
            case ADMINISTRADOR:
                mostrarMenuAdministrador();
                break;
            case RECLUTADOR:
                mostrarMenuReclutador();
                break;
            case GERENTE:
                mostrarMenuGerente();
                break;
            case EMPLEADO:
                mostrarMenuEmpleado();
                break;
            default:
            // Rol no reconocido.
        }
    }

    // Método para mostrar menú de administrador (consola).
    private void mostrarMenuAdministrador() {
        boolean continuar = true;
        while (continuar) {
            // Menú de administración (modo consola)
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        gestionarUsuarios();
                        break;
                    case 2:
                        gestionarEmpleados();
                        break;
                    case 3:
                        gestionarContratos();
                        break;
                    case 4:
                        verLogsSistema();
                        break;
                    case 5:
                        generarReportes();
                        break;
                    case 6:
                        verEstadisticas();
                        break;
                    case 7:
                        logout();
                        continuar = false;
                        break;
                    default:
                    // Opción no válida (modo consola)
                }
            } catch (Exception e) {
                // Error en la operación (modo consola)
            }
        }
    }

    // Método para mostrar menú de reclutador (consola).
    private void mostrarMenuReclutador() {
        boolean continuar = true;
        while (continuar) {
            // Menú de reclutador (modo consola)
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        gestionarEmpleados();
                        break;
                    case 2:
                        gestionarContratos();
                        break;
                    // ... (más casos)
                    case 6:
                        logout();
                        continuar = false;
                        break;
                    default:
                    // Opción no válida (modo consola)
                }
            } catch (Exception e) {
                // Error en operación (modo consola)
            }
        }
    }

    // Método para mostrar menú de gerente (consola).
    private void mostrarMenuGerente() {
        boolean continuar = true;
        while (continuar) {
            // Menú de gerente (modo consola)
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        generarReportes();
                        break;
                    // ... (más casos)
                    case 6:
                        logout();
                        continuar = false;
                        break;
                    default:
                    // Opción no válida (modo consola)
                }
            } catch (Exception e) {
                // Error en operación (modo consola)
            }
        }
    }

    // Método para mostrar menú de empleado (consola).
    private void mostrarMenuEmpleado() {
        boolean continuar = true;
        while (continuar) {
            // Menú de empleado (modo consola)
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        verMisDatos();
                        break;
                    case 2:
                        actualizarInformacionPersonal();
                        break;
                    // ... (más casos)
                    case 5:
                        logout();
                        continuar = false;
                        break;
                    default:
                    // Opción no válida (modo consola)
                }
            } catch (Exception e) {
                // Error en operación (modo consola)
            }
        }
    }

    // ===== MÉTODOS PARA GESTIÓN DE EMPLEADOS =====
    // Estos métodos son la "API" que el Controlador ofrece a las Vistas (Strategies).
    
    // Obtener todos los empleados.
    public List<Empleado> obtenerTodosEmpleados() {
        return facade.obtenerTodosEmpleados();
    }

    // Buscar un empleado por su ID.
    public Empleado buscarEmpleadoPorId(int idEmpleado) {
        return facade.buscarEmpleadoPorId(idEmpleado);
    }

    // Buscar un empleado por su DNI.
    public Empleado buscarEmpleadoPorDni(String dni) {
        return facade.buscarEmpleadoPorDni(dni);
    }

    // Buscar empleados por nombre.
    public List<Empleado> buscarEmpleadosPorNombre(String nombre) {
        return facade.buscarEmpleadosPorNombre(nombre);
    }

    // Guardar un nuevo empleado.
    public void guardarEmpleado(Empleado empleado) {
        facade.guardarEmpleado(empleado);
    }

    // Actualizar un empleado existente.
    public void actualizarEmpleado(Empleado empleado) {
        facade.actualizarEmpleado(empleado);
    }

    // Eliminar un empleado por su ID.
    public void eliminarEmpleado(int idEmpleado) {
        facade.eliminarEmpleado(idEmpleado);
    }

    // ===== MÉTODOS PARA GESTIÓN DE CONTRATOS =====

    // Obtener todos los contratos.
    public List<Contrato> obtenerTodosContratos() {
        return facade.obtenerTodosContratos();
    }

    // Buscar un contrato por su ID.
    public Contrato buscarContratoPorId(int idContrato) {
        return facade.buscarContratoPorId(idContrato);
    }

    // Buscar todos los contratos de un empleado específico.
    public List<Contrato> buscarContratosPorEmpleado(int idEmpleado) {
        return facade.buscarContratosPorEmpleado(idEmpleado);
    }

    // Guardar un nuevo contrato.
    public void guardarContrato(Contrato contrato) {
        facade.guardarContrato(contrato);
    }

    // Actualizar un contrato existente.
    public void actualizarContrato(Contrato contrato) {
        facade.actualizarContrato(contrato);
    }

    // Eliminar un contrato por su ID.
    public void eliminarContrato(int idContrato) {
        facade.eliminarContrato(idContrato);
    }

    // ===== MÉTODOS PARA GESTIÓN DE USUARIOS =====

    // Obtener todos los usuarios.
    public List<Usuario> obtenerTodosUsuarios() {
        return facade.obtenerTodosUsuarios();
    }

    // Buscar un usuario por su ID.
    public Usuario buscarUsuarioPorId(int idUsuario) {
        return facade.buscarUsuarioPorId(idUsuario);
    }

    // Buscar un usuario por su nombre de usuario.
    public Usuario buscarUsuarioPorNombre(String nombreUsuario) {
        return facade.buscarUsuarioPorNombre(nombreUsuario);
    }

    // Guardar un nuevo usuario.
    public void guardarUsuario(Usuario usuario) {
        facade.guardarUsuario(usuario);
    }

    // Lógica de consola para agregar un usuario.
    public void agregarUsuario(String nombreUsuario, String contrasena, String rol) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setContrasena(contrasena);
        // Se crea un empleado básico para asociarlo, ya que el Usuario lo requiere.
        Empleado empleado = new Empleado();
        empleado.setNombre(nombreUsuario);
        empleado.setApellidos("");
        empleado.setRol(Rol.valueOf(rol.toUpperCase()));
        nuevoUsuario.setEmpleado(empleado);
        facade.guardarUsuario(nuevoUsuario);
    }

    // Lógica de consola para actualizar un usuario.
    public void actualizarUsuario(int idUsuario, String nombreUsuario, String contrasena, String rol) {
        Usuario usuario = facade.buscarUsuarioPorId(idUsuario);
        if (usuario != null) {
            usuario.setNombreUsuario(nombreUsuario);
            if (contrasena != null && !contrasena.isEmpty()) {
                usuario.setContrasena(contrasena);
            }
            usuario.getEmpleado().setRol(Rol.valueOf(rol.toUpperCase()));
            facade.actualizarUsuario(usuario);
        }
    }

    // Actualizar un usuario (versión usada por la GUI).
    public void actualizarUsuario(Usuario usuario) {
        facade.actualizarUsuario(usuario);
    }

    // Eliminar un usuario por su ID.
    public void eliminarUsuario(int idUsuario) {
        facade.eliminarUsuario(idUsuario);
    }

    // ===== MÉTODOS PARA REPORTES =====

    // Generar un reporte (texto) de empleados.
    public String generarReporteEmpleados() {
        return facade.generarReporteEmpleados();
    }

    // Generar un reporte (texto) de contratos.
    public String generarReporteContratos() {
        return facade.generarReporteContratos();
    }

    // ===== MÉTODOS PARA EMPLEADOS (ROL) =====

    // Permite al empleado logueado ver sus propios datos.
    public Empleado empleadoVerMisDatos() {
        return facade.empleadoVerMisDatos();
    }

    // Permite al empleado logueado actualizar sus datos personales.
    public void empleadoActualizarDatos(int idEmpleado, String nuevaDireccion, String nuevoCorreo, String nuevoNumero) {
        facade.empleadoActualizarDatos(idEmpleado, nuevaDireccion, nuevoCorreo, nuevoNumero);
    }

    // ===== MÉTODOS PARA LOGS =====

    // Obtener los logs de auditoría para un usuario específico.
    public List<String> obtenerLogsPorUsuario(String nombreUsuario) {
        return facade.obtenerLogsPorUsuario(nombreUsuario);
    }

    // ===== MÉTODOS PARA MENÚS FUNCIONALES (CONSOLA) =====
    // Lógica interna de la versión de consola.

    private void gestionarUsuarios() {
        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
        System.out.println("1. Ver todos los usuarios");
        System.out.println("2. Agregar usuario");
        System.out.println("3. Actualizar usuario");
        System.out.println("4. Eliminar usuario");
        System.out.println("5. Volver");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                List<Usuario> usuarios = obtenerTodosUsuarios();
                System.out.println("Usuarios registrados:");
                for (Usuario u : usuarios) {
                    System.out.println(u.toString());
                }
                break;
            case 2:
                System.out.print("Nombre de usuario: ");
                String nombreUsuario = scanner.nextLine();
                System.out.print("Contraseña: ");
                String contrasena = scanner.nextLine();
                System.out.print("Rol (ADMINISTRADOR, RECLUTADOR, GERENTE, EMPLEADO): ");
                String rol = scanner.nextLine();
                agregarUsuario(nombreUsuario, contrasena, rol);
                System.out.println("Usuario agregado exitosamente.");
                break;
            case 3:
                System.out.print("ID del usuario a actualizar: ");
                int idUsuario = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Nuevo nombre de usuario: ");
                String nuevoNombre = scanner.nextLine();
                System.out.print("Nueva contraseña (dejar vacío para no cambiar): ");
                String nuevaContrasena = scanner.nextLine();
                System.out.print("Nuevo rol: ");
                String nuevoRol = scanner.nextLine();
                actualizarUsuario(idUsuario, nuevoNombre, nuevaContrasena, nuevoRol);
                System.out.println("Usuario actualizado exitosamente.");
                break;
            case 4:
                System.out.print("ID del usuario a eliminar: ");
                int idEliminar = scanner.nextInt();
                eliminarUsuario(idEliminar);
                System.out.println("Usuario eliminado exitosamente.");
                break;
            case 5:
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // Lógica de consola para gestionar empleados.
    private void gestionarEmpleados() {
        System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
        System.out.println("1. Ver todos los empleados");
        System.out.println("2. Buscar empleado por ID");
        System.out.println("3. Buscar empleado por DNI");
        System.out.println("4. Buscar empleados por nombre");
        System.out.println("5. Agregar empleado");
        System.out.println("6. Actualizar empleado");
        System.out.println("7. Eliminar empleado");
        System.out.println("8. Volver");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                List<Empleado> empleados = obtenerTodosEmpleados();
                System.out.println("Empleados registrados:");
                for (Empleado e : empleados) {
                    System.out.println(e.toString());
                }
                break;
            case 2:
                System.out.print("ID del empleado: ");
                int idBuscar = scanner.nextInt();
                Empleado empleado = buscarEmpleadoPorId(idBuscar);
                if (empleado != null) {
                    System.out.println(empleado.toString());
                } else {
                    System.out.println("Empleado no encontrado.");
                }
                break;
            case 3:
                System.out.print("DNI del empleado: ");
                String dni = scanner.nextLine();
                empleado = buscarEmpleadoPorDni(dni);
                if (empleado != null) {
                    System.out.println(empleado.toString());
                } else {
                    System.out.println("Empleado no encontrado.");
                }
                break;
            case 4:
                System.out.print("Nombre del empleado: ");
                String nombre = scanner.nextLine();
                List<Empleado> empleadosEncontrados = buscarEmpleadosPorNombre(nombre);
                System.out.println("Empleados encontrados:");
                for (Empleado e : empleadosEncontrados) {
                    System.out.println(e.toString());
                }
                break;
            case 5:
                // La lógica GUI para esto es más compleja y está en las Strategies.
                System.out.println("Funcionalidad de agregar empleado no implementada aún.");
                break;
            case 6:
                // La lógica GUI para esto es más compleja.
                System.out.println("Funcionalidad de actualizar empleado no implementada aún.");
                break;
            case 7:
                System.out.print("ID del empleado a eliminar: ");
                int idEliminar = scanner.nextInt();
                eliminarEmpleado(idEliminar);
                System.out.println("Empleado eliminado exitosamente.");
                break;
            case 8:
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // Lógica de consola para gestionar contratos.
    private void gestionarContratos() {
        System.out.println("\n--- GESTIÓN DE CONTRATOS ---");
        System.out.println("1. Ver todos los contratos");
        System.out.println("2. Buscar contrato por ID");
        System.out.println("3. Buscar contratos por empleado");
        System.out.println("4. Agregar contrato");
        System.out.println("5. Actualizar contrato");
        System.out.println("6. Eliminar contrato");
        System.out.println("7. Volver");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                List<Contrato> contratos = obtenerTodosContratos();
                System.out.println("Contratos registrados:");
                for (Contrato c : contratos) {
                    System.out.println(c.toString());
                }
                break;
            case 2:
                System.out.print("ID del contrato: ");
                int idBuscar = scanner.nextInt();
                Contrato contrato = buscarContratoPorId(idBuscar);
                if (contrato != null) {
                    System.out.println(contrato.toString());
                } else {
                    System.out.println("Contrato no encontrado.");
                }
                break;
            case 3:
                System.out.print("ID del empleado: ");
                int idEmpleado = scanner.nextInt();
                List<Contrato> contratosEmpleado = buscarContratosPorEmpleado(idEmpleado);
                System.out.println("Contratos del empleado:");
                for (Contrato c : contratosEmpleado) {
                    System.out.println(c.toString());
                }
                break;
            case 4:
                // La lógica GUI está en las Strategies.
                System.out.println("Funcionalidad de agregar contrato no implementada aún.");
                break;
            case 5:
                // La lógica GUI está en las Strategies.
                System.out.println("Funcionalidad de actualizar contrato no implementada aún.");
                break;
            case 6:
                System.out.print("ID del contrato a eliminar: ");
                int idEliminar = scanner.nextInt();
                eliminarContrato(idEliminar);
                System.out.println("Contrato eliminado exitosamente.");
                break;
            case 7:
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // Lógica de consola para ver logs.
    private void verLogsSistema() {
        System.out.println("\n--- LOGS DEL SISTEMA ---");
        System.out.print("Nombre de usuario para ver logs: ");
        String nombreUsuario = scanner.nextLine();
        List<String> logs = obtenerLogsPorUsuario(nombreUsuario);
        System.out.println("Logs del usuario " + nombreUsuario + ":");
        for (String log : logs) {
            System.out.println(log);
        }
    }

    // Lógica de consola para generar reportes.
    private void generarReportes() {
        System.out.println("\n--- GENERACIÓN DE REPORTES ---");
        System.out.println("1. Reporte de empleados");
        System.out.println("2. Reporte de contratos");
        System.out.println("3. Volver");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                String reporteEmpleados = generarReporteEmpleados();
                System.out.println(reporteEmpleados);
                break;
            case 2:
                String reporteContratos = generarReporteContratos();
                System.out.println(reporteContratos);
                break;
            case 3:
                return;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // Lógica de consola para ver estadísticas.
    private void verEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS DEL SISTEMA ---");
        List<Empleado> empleados = obtenerTodosEmpleados();
        List<Contrato> contratos = obtenerTodosContratos();
        List<Usuario> usuarios = obtenerTodosUsuarios();

        System.out.println("Total de empleados: " + empleados.size());
        System.out.println("Total de contratos: " + contratos.size());
        System.out.println("Total de usuarios: " + usuarios.size());

        // Conteo de roles usando streams.
        long administradores = usuarios.stream().filter(u -> u.getRol() == Rol.ADMINISTRADOR).count();
        long reclutadores = usuarios.stream().filter(u -> u.getRol() == Rol.RECLUTADOR).count();
        long gerentes = usuarios.stream().filter(u -> u.getRol() == Rol.GERENTE).count();
        long empleadosRol = usuarios.stream().filter(u -> u.getRol() == Rol.EMPLEADO).count();

        System.out.println("Administradores: " + administradores);
        System.out.println("Reclutadores: " + reclutadores);
        System.out.println("Gerentes: " + gerentes);
        System.out.println("Empleados: " + empleadosRol);
    }

    // Lógica de consola (placeholder).
    private void buscarCandidatos() {
        System.out.println("\n--- BÚSQUEDA DE CANDIDATOS ---");
        System.out.println("Funcionalidad de búsqueda de candidatos no implementada aún.");
    }

    // Lógica de consola (placeholder).
    private void verMetricas() {
        System.out.println("\n--- MÉTRICAS DEL SISTEMA ---");
        System.out.println("Funcionalidad de métricas no implementada aún.");
    }

    // Lógica de consola para que un empleado vea sus datos.
    private void verMisDatos() {
        System.out.println("\n--- MIS DATOS ---");
        Empleado empleado = empleadoVerMisDatos();
        if (empleado != null) {
            System.out.println(empleado.toString());
        } else {
            System.out.println("No se pudieron obtener sus datos.");
        }
    }

    // Lógica de consola para que un empleado actualice sus datos.
    private void actualizarInformacionPersonal() {
        System.out.println("\n--- ACTUALIZAR INFORMACIÓN PERSONAL ---");
        System.out.print("Nueva dirección: ");
        String nuevaDireccion = scanner.nextLine();
        System.out.print("Nuevo correo electrónico: ");
        String nuevoCorreo = scanner.nextLine();
        System.out.print("Nuevo número de teléfono: ");
        String nuevoNumero = scanner.nextLine();

        Usuario usuarioActual = getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getEmpleado() != null) {
            empleadoActualizarDatos(usuarioActual.getEmpleado().getIdEmpleado(),
                    nuevaDireccion, nuevoCorreo, nuevoNumero);
            System.out.println("Información actualizada exitosamente.");
        } else {
            System.out.println("Error al actualizar la información.");
        }
    }

    // Lógica de consola para que un empleado vea sus contratos.
    private void verMisContratos() {
        System.out.println("\n--- MIS CONTRATOS ---");
        Usuario usuarioActual = getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getEmpleado() != null) {
            List<Contrato> contratos = buscarContratosPorEmpleado(usuarioActual.getEmpleado().getIdEmpleado());
            System.out.println("Sus contratos:");
            for (Contrato c : contratos) {
                System.out.println(c.toString());
            }
        } else {
            System.out.println("No se pudieron obtener sus contratos.");
        }
    }

    // Método para cerrar el controlador y liberar recursos.
    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
        facade.cerrarConexion(); // Importante: cierra la conexión a la BD.
    }

    // Getter para el facade (usado por las Vistas).
    public Facade getFacade() {
        return facade;
    }
}