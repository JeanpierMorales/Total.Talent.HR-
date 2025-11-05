package Modelo.Builder;

// Importaciones de forma unitaria para mejor eficiencia del programa
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Factory.ContratoLocacion;
import Modelo.Factory.ContratoParcial;
import Modelo.Factory.ContratoPlanilla;
import java.util.Date;

public class ContratoBuilder {
    // La clase builder sirve para potencial objetos de tipo Contrato de manera flexible
    // Con este patrón vamos a añadir las bonificaciones y descuentos a los contratos 

    private final Contrato contrato; // Instancia de Contrato que se va a construir 

    // Constructor que recibe un objeto Contrato existente
    public ContratoBuilder(Contrato contratoBase) {
        this.contrato = contratoBase;
    }

    // Datos obligatorios ya están en el contratoBase pasado al constructor
        
    public ContratoBuilder conId(int id) {
        contrato.setIdContrato(id);
        return this;
    }

    public ContratoBuilder conEmpleado(Empleado emp) {
        contrato.setEmpleado(emp);
        return this;
    }

    public ContratoBuilder conFechas(Date inicio, Date fin) {
        contrato.setFechaInicio(inicio);
        contrato.setFechaFin(fin);
        return this;
    }

    public ContratoBuilder conSalario(double salario) {
        contrato.setSalarioBase(salario);
        return this;
    }

    // Opcionales comunes a todos los contratos pero opcionales 
    public ContratoBuilder conBonificacion(double bonificacion) {
        contrato.setBonificacion(bonificacion);
        return this;
    }

    public ContratoBuilder conDescuentoAFP(double descuento) {
        contrato.setDescuentoAFP(descuento);
        return this;
    }

    // Opcionales específicos para cada una de las clases hijas de Contrato, tanto planilla, parcial o locación
    public ContratoBuilder conHorasExtras(double horas) { // Solo para ContratoPlanilla
        // Verificamos si el contrato es de tipo ContratoPlanilla antes de establecer las horas extras
        if (contrato instanceof ContratoPlanilla) {
            ((ContratoPlanilla) contrato).setHorasExtras(horas);
        }
        return this;
    }

    public ContratoBuilder conHorasTrabajadas(double horas) { // Solo para ContratoParcial
        // Verificamos si el contrato es de tipo ContratoParcial antes de establecer las horas trabajadas
        if (contrato instanceof ContratoParcial) {
            ((ContratoParcial) contrato).setHorasTrabajadas((int) horas);
        }
        return this;
    }

    public ContratoBuilder conPagoPorHora(double pago) { // Solo para ContratoParcial
        // Verificamos si el contrato es de tipo ContratoParcial antes de establecer el pago por hora
        if (contrato instanceof ContratoParcial) {
            ((ContratoParcial) contrato).setPagoPorHora(pago);
        }
        return this;
    }

    public ContratoBuilder conMontoPorProyecto(double monto) { // Solo para ContratoLocacion
        // Verificamos si el contrato es de tipo ContratoLocacion antes de establecer el monto por proyecto
        if (contrato instanceof ContratoLocacion) {
            ((ContratoLocacion) contrato).setMontoPorProyecto(monto);
        }
        return this;
    }

    public ContratoBuilder conNumeroProyectos(int num) { // Solo para ContratoLocacion
        // Verificamos si el contrato es de tipo ContratoLocacion antes de establecer el número de proyectos
        if (contrato instanceof ContratoLocacion) {
            ((ContratoLocacion) contrato).setNumeroProyectos(num);
        }
        return this;
    }

    public Contrato build() { // Método para devolver el contrato construido
        return contrato; // Devuelve el contrato construido
    }
}
