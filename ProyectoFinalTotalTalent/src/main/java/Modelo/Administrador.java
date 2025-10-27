package Modelo;

// Clase Administrador extiende Empleado para dar roles especiales
public class Administrador extends Empleado {

    public Administrador() {
        super();
    }

    public Administrador(String nombre, String apellido, String correo, String cargo, double salario) {
        super(nombre, apellido, correo, cargo, salario);
    }
}
