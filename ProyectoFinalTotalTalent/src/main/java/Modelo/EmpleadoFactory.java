package Modelo;

public class EmpleadoFactory {

    public static Empleado crearEmpleado(String tipo, String nombre, String apellido, String correo, String password, String cargo, double salario) {
        String t = tipo == null ? "empleado" : tipo.toLowerCase();
        switch (t) {
            case "administrador":
                return new Administrador(nombre, apellido, correo, cargo, salario) {
                    {
                        setPassword(password);
                    }
                };
            case "reclutador":
                return new Empleado(nombre, apellido, correo, cargo, salario) {
                    {
                        setPassword(password);
                    }
                }; // reclutador se modela como empleado con cargo Reclutador
            default:
                return new Empleado(nombre, apellido, correo, cargo, salario) {
                    {
                        setPassword(password);
                    }
                };
        }
    }
}
