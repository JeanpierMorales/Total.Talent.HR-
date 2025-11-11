package Controlador;

import Modelo.Facade.Facade;
import Modelo.Rol;
import Modelo.Usuario;
import Modelo.Empleado;
import Modelo.Contrato;
import java.util.List;
import java.util.Scanner;

// Controlador principal del sistema Total Talent HR
// Maneja la lógica de negocio y coordina las operaciones entre modelo y vista
public class TotalTalentControlador {

    private Facade facade;
    private Scanner scanner;
    private int intentosLogin;

    // Constructor que inicializa el facade y el scanner
    public TotalTalentControlador() {
        this.facade = new Facade();
        this.scanner = new Scanner(System.in);
        this.intentosLogin = 0;
    }

    // Método para manejar el login del usuario con límite de intentos
    public boolean login(String nombreUsuario, String contrasena) {
        if (intentosLogin >= 3) {
            System.out.println("Demasiados intentos fallidos. Sistema bloqueado.");
            return false;
        }

        boolean exito = facade.login(nombreUsuario, contrasena);
        if (exito) {
            intentosLogin = 0; // Resetear intentos en login exitoso
        } else {
            intentosLogin++;
            System.out.println("Intento fallido " + intentosLogin + " de 3.");
        }
        return exito;
    }

    // Método para obtener el usuario actualmente logueado
    public Usuario getUsuarioActual() {
        return facade.getUsuarioActual();
    }

    // Método para verificar si hay usuario logueado
    public boolean hayUsuarioLogueado() {
        return facade.hayUsuarioLogueado();
    }

    // Método para cerrar sesión
    public void logout() {
        facade.logout();
    }

    // Método para mostrar el dashboard según el rol del usuario
    public void mostrarDashboardSegunRol() {
        Usuario usuarioActual = facade.getUsuarioActual();
        if (usuarioActual == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }

        Rol rol = usuarioActual.getRol();

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
                System.out.println("Rol no reconocido.");
        }
    }

    // Método para mostrar menú de administrador
    private void mostrarMenuAdministrador() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== DASHBOARD ADMINISTRADOR ===");
            System.out.println("Funciones disponibles:");
            System.out.println("1. Gestionar usuarios");
            System.out.println("2. Gestionar empleados");
            System.out.println("3. Gestionar contratos");
            System.out.println("4. Ver logs del sistema");
            System.out.println("5. Generar reportes");
            System.out.println("6. Ver estadísticas");
            System.out.println("7. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

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
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para mostrar menú de reclutador
    private void mostrarMenuReclutador() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== DASHBOARD RECLUTADOR ===");
            System.out.println("Funciones disponibles:");
            System.out.println("1. Gestionar empleados");
            System.out.println("2. Gestionar contratos");
            System.out.println("3. Buscar candidatos");
            System.out.println("4. Ver reportes");
            System.out.println("5. Ver estadísticas");
            System.out.println("6. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

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
                    case 3:
                        buscarCandidatos();
                        break;
                    case 4:
                        generarReportes();
                        break;
                    case 5:
                        verEstadisticas();
                        break;
                    case 6:
                        logout();
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para mostrar menú de gerente
    private void mostrarMenuGerente() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== DASHBOARD GERENTE ===");
            System.out.println("Funciones disponibles:");
            System.out.println("1. Ver reportes");
            System.out.println("2. Gestionar contratos");
            System.out.println("3. Ver métricas");
            System.out.println("4. Ver estadísticas");
            System.out.println("5. Gestionar empleados");
            System.out.println("6. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        generarReportes();
                        break;
                    case 2:
                        gestionarContratos();
                        break;
                    case 3:
                        verMetricas();
                        break;
                    case 4:
                        verEstadisticas();
                        break;
                    case 5:
                        gestionarEmpleados();
                        break;
                    case 6:
                        logout();
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para mostrar menú de empleado
    private void mostrarMenuEmpleado() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== DASHBOARD EMPLEADO ===");
            System.out.println("Funciones disponibles:");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Actualizar información personal");
            System.out.println("3. Ver mis contratos");
            System.out.println("4. Ver estadísticas");
            System.out.println("5. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

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
                    case 3:
                        verMisContratos();
                        break;
                    case 4:
                        verEstadisticas();
                        break;
                    case 5:
                        logout();
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ===== MÉTODOS PARA GESTIÓN DE EMPLEADOS =====
    // Método para obtener todos los empleados
    public List<Empleado> obtenerTodosEmpleados() {
        return facade.obtenerTodosEmpleados();
    }

    // Método para buscar empleado por ID
    public Empleado buscarEmpleadoPorId(int idEmpleado) {
        return facade.buscarEmpleadoPorId(idEmpleado);
    }

    // Método para buscar empleado por DNI
    public Empleado buscarEmpleadoPorDni(String dni) {
        return facade.buscarEmpleadoPorDni(dni);
    }

    // Método para buscar empleados por nombre
    public List<Empleado> buscarEmpleadosPorNombre(String nombre) {
        return facade.buscarEmpleadosPorNombre(nombre);
    }

    // Método para guardar empleado
    public void guardarEmpleado(Empleado empleado) {
        facade.guardarEmpleado(empleado);
    }

    // Método para actualizar empleado
    public void actualizarEmpleado(Empleado empleado) {
        facade.actualizarEmpleado(empleado);
    }

    // Método para eliminar empleado
    public void eliminarEmpleado(int idEmpleado) {
        facade.eliminarEmpleado(idEmpleado);
    }

    // ===== MÉTODOS PARA GESTIÓN DE CONTRATOS =====
    // Método para obtener todos los contratos
    public List<Contrato> obtenerTodosContratos() {
        return facade.obtenerTodosContratos();
    }

    // Método para buscar contrato por ID
    public Contrato buscarContratoPorId(int idContrato) {
        return facade.buscarContratoPorId(idContrato);
    }

    // Método para buscar contratos por empleado
    public List<Contrato> buscarContratosPorEmpleado(int idEmpleado) {
        return facade.buscarContratosPorEmpleado(idEmpleado);
    }

    // Método para guardar contrato
    public void guardarContrato(Contrato contrato) {
        facade.guardarContrato(contrato);
    }

    // Método para actualizar contrato
    public void actualizarContrato(Contrato contrato) {
        facade.actualizarContrato(contrato);
    }

    // Método para eliminar contrato
    public void eliminarContrato(int idContrato) {
        facade.eliminarContrato(idContrato);
    }

    // ===== MÉTODOS PARA GESTIÓN DE USUARIOS =====
    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        return facade.obtenerTodosUsuarios();
    }

    // Método para buscar usuario por ID
    public Usuario buscarUsuarioPorId(int idUsuario) {
        return facade.buscarUsuarioPorId(idUsuario);
    }

    // Método para buscar usuario por nombre
    public Usuario buscarUsuarioPorNombre(String nombreUsuario) {
        return facade.buscarUsuarioPorNombre(nombreUsuario);
    }

    // Método para guardar usuario
    public void guardarUsuario(Usuario usuario) {
        facade.guardarUsuario(usuario);
    }

    // Método para agregar usuario
    public void agregarUsuario(String nombreUsuario, String contrasena, String rol) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setContrasena(contrasena);
        // Crear empleado básico para el rol
        Empleado empleado = new Empleado();
        empleado.setNombre(nombreUsuario);
        empleado.setApellidos("");
        empleado.setRol(Rol.valueOf(rol.toUpperCase()));
        nuevoUsuario.setEmpleado(empleado);
        facade.guardarUsuario(nuevoUsuario);
    }

    // Método para actualizar usuario
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

    // Método para actualizar usuario
    public void actualizarUsuario(Usuario usuario) {
        facade.actualizarUsuario(usuario);
    }

    // Método para eliminar usuario
    public void eliminarUsuario(int idUsuario) {
        facade.eliminarUsuario(idUsuario);
    }

    // ===== MÉTODOS PARA REPORTES =====
    // Método para generar reporte de empleados
    public String generarReporteEmpleados() {
        return facade.generarReporteEmpleados();
    }

    // Método para generar reporte de contratos
    public String generarReporteContratos() {
        return facade.generarReporteContratos();
    }

    // ===== MÉTODOS PARA EMPLEADOS =====
    // Método para que empleado vea sus datos
    public Empleado empleadoVerMisDatos() {
        return facade.empleadoVerMisDatos();
    }

    // Método para que empleado actualice sus datos
    public void empleadoActualizarDatos(int idEmpleado, String nuevaDireccion, String nuevoCorreo, String nuevoNumero) {
        facade.empleadoActualizarDatos(idEmpleado, nuevaDireccion, nuevoCorreo, nuevoNumero);
    }

    // ===== MÉTODOS PARA LOGS =====
    // Método para obtener logs por usuario
    public List<String> obtenerLogsPorUsuario(String nombreUsuario) {
        return facade.obtenerLogsPorUsuario(nombreUsuario);
    }

    // ===== MÉTODOS PARA MENÚS FUNCIONALES =====
    // Método para gestionar usuarios (solo administrador)
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

    // Método para gestionar empleados
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
                // Implementar agregar empleado
                System.out.println("Funcionalidad de agregar empleado no implementada aún.");
                break;
            case 6:
                // Implementar actualizar empleado
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

    // Método para gestionar contratos
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
                // Implementar agregar contrato
                System.out.println("Funcionalidad de agregar contrato no implementada aún.");
                break;
            case 5:
                // Implementar actualizar contrato
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

    // Método para ver logs del sistema
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

    // Método para generar reportes
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

    // Método para ver estadísticas
    private void verEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS DEL SISTEMA ---");
        List<Empleado> empleados = obtenerTodosEmpleados();
        List<Contrato> contratos = obtenerTodosContratos();
        List<Usuario> usuarios = obtenerTodosUsuarios();

        System.out.println("Total de empleados: " + empleados.size());
        System.out.println("Total de contratos: " + contratos.size());
        System.out.println("Total de usuarios: " + usuarios.size());

        // Estadísticas por rol
        long administradores = usuarios.stream().filter(u -> u.getRol() == Rol.ADMINISTRADOR).count();
        long reclutadores = usuarios.stream().filter(u -> u.getRol() == Rol.RECLUTADOR).count();
        long gerentes = usuarios.stream().filter(u -> u.getRol() == Rol.GERENTE).count();
        long empleadosRol = usuarios.stream().filter(u -> u.getRol() == Rol.EMPLEADO).count();

        System.out.println("Administradores: " + administradores);
        System.out.println("Reclutadores: " + reclutadores);
        System.out.println("Gerentes: " + gerentes);
        System.out.println("Empleados: " + empleadosRol);
    }

    // Método para buscar candidatos
    private void buscarCandidatos() {
        System.out.println("\n--- BÚSQUEDA DE CANDIDATOS ---");
        System.out.println("Funcionalidad de búsqueda de candidatos no implementada aún.");
        // Aquí se implementaría la lógica para buscar candidatos externos
    }

    // Método para ver métricas
    private void verMetricas() {
        System.out.println("\n--- MÉTRICAS DEL SISTEMA ---");
        System.out.println("Funcionalidad de métricas no implementada aún.");
        // Aquí se implementaría la lógica para mostrar métricas avanzadas
    }

    // Método para ver mis datos (empleado)
    private void verMisDatos() {
        System.out.println("\n--- MIS DATOS ---");
        Empleado empleado = empleadoVerMisDatos();
        if (empleado != null) {
            System.out.println(empleado.toString());
        } else {
            System.out.println("No se pudieron obtener sus datos.");
        }
    }

    // Método para actualizar información personal
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

    // Método para ver mis contratos
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

    // Método para cerrar el controlador y liberar recursos
    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
        facade.cerrarConexion();
    }

    // Getter para el facade (para acceso directo si es necesario)
    public Facade getFacade() {
        return facade;
    }
}
