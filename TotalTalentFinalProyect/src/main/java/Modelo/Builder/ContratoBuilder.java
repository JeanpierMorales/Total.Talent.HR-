package Modelo.Builder;

// Importaciones de forma unitaria para mejor eficiencia del programa
import Modelo.Empleado;
import Modelo.Contrato;
import Modelo.Factory.ContratoLocacion;
import Modelo.Factory.ContratoParcial;
import Modelo.Factory.ContratoPlanilla;
import java.util.Date;

// --- Patrón de Diseño Builder para Contratos ---
// Esta clase implementa el patrón Builder para construir objetos Contrato.
// Su propósito es facilitar la creación de objetos Contrato (y sus subclases)
// de una manera flexible y paso a paso.
//
// Esta implementación funciona en conjunto con la ContratoFactory.
// 1. ContratoFactory crea la instancia base (ej. new ContratoPlanilla()).
// 2. Este ContratoBuilder recibe esa instancia base en su constructor.
// 3. Se usan los métodos "con..." para añadir los atributos opcionales.
// 4. Se llama a "build()" para obtener el objeto finalizado.
public class ContratoBuilder {
    
    // Almacena la instancia del Contrato (creada por el Factory) que se está configurando.
    private final Contrato contrato; 

    // Constructor que recibe el objeto Contrato base.
    // Este objeto base es usualmente creado por la ContratoFactory.
    public ContratoBuilder(Contrato contratoBase) {
        this.contrato = contratoBase;
    }

    // --- Métodos de configuración (Interfaz Fluida) ---
    // Los siguientes métodos devuelven 'this' (la propia instancia del Builder)
    // para permitir el encadenamiento de llamadas (ej. builder.conId(1).conSalario(1000)).

    // Establece el ID del contrato.
    public ContratoBuilder conId(int id) {
        contrato.setIdContrato(id);
        return this;
    }

    // Establece el Empleado asociado al contrato.
    public ContratoBuilder conEmpleado(Empleado emp) {
        contrato.setEmpleado(emp);
        return this;
    }

    // Establece las fechas de inicio y fin.
    public ContratoBuilder conFechas(Date inicio, Date fin) {
        contrato.setFechaInicio(inicio);
        contrato.setFechaFin(fin);
        return this;
    }

    // Establece el salario base.
    public ContratoBuilder conSalario(float salario) {
        contrato.setSalarioBase(salario);
        return this;
    }

    // --- Métodos para atributos opcionales comunes ---

    // Establece la bonificación (opcional).
    public ContratoBuilder conBonificacion(float bonificacion) {
        contrato.setBonificacion(bonificacion);
        return this;
    }

    // Establece el descuento de AFP (opcional).
    public ContratoBuilder conDescuentoAFP(float descuento) {
        contrato.setDescuentoAFP(descuento);
        return this;
    }

    // --- Métodos para atributos opcionales específicos por tipo ---
    // Estos métodos verifican el tipo de contrato (usando 'instanceof')
    // antes de asignar el atributo, para evitar errores de casteo.

    // Asigna horas extras. Solo aplica si el contrato es de tipo ContratoPlanilla.
    public ContratoBuilder conHorasExtras(float horas) { 
        // Verificamos si el contrato es de tipo ContratoPlanilla.
        if (contrato instanceof ContratoPlanilla) {
            // Si es Planilla, se castea y se asigna el valor.
            ((ContratoPlanilla) contrato).setHorasExtras(horas);
        }
        return this;
    }

    // Asigna horas trabajadas. Solo aplica si el contrato es de tipo ContratoParcial.
    public ContratoBuilder conHorasTrabajadas(float horas) { 
        // Verificamos si el contrato es de tipo ContratoParcial.
        if (contrato instanceof ContratoParcial) {
            ((ContratoParcial) contrato).setHorasTrabajadas((int) horas);
        }
        return this;
    }

    // Asigna el pago por hora. Solo aplica si el contrato es de tipo ContratoParcial.
    public ContratoBuilder conPagoPorHora(float pago) { 
        // Verificamos si el contrato es de tipo ContratoParcial.
        if (contrato instanceof ContratoParcial) {
            ((ContratoParcial) contrato).setPagoPorHora(pago);
        }
        return this;
    }

    // Asigna el monto por proyecto. Solo aplica si el contrato es de tipo ContratoLocacion.
    public ContratoBuilder conMontoPorProyecto(float monto) { 
        // Verificamos si el contrato es de tipo ContratoLocacion.
        if (contrato instanceof ContratoLocacion) {
            ((ContratoLocacion) contrato).setMontoPorProyecto(monto);
        }
        return this;
    }

    // Asigna el número de proyectos. Solo aplica si el contrato es de tipo ContratoLocacion.
    public ContratoBuilder conNumeroProyectos(int num) { 
        // Verificamos si el contrato es de tipo ContratoLocacion.
        if (contrato instanceof ContratoLocacion) {
            ((ContratoLocacion) contrato).setNumeroProyectos(num);
        }
        return this;
    }

    // --- Método de construcción final ---
    
    // Devuelve el objeto Contrato ya configurado con todos los atributos.
    public Contrato build() { 
        return contrato; // Devuelve el contrato construido.
    }
}