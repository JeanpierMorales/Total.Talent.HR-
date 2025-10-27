package Modelo;

/**
 * Interfaz Builder para construir contratos paso a paso.
 */
public interface ContratoBuilder {

    void reset();

    void setTipo(String tipo);

    void setDuracion(String duracion);

    void setMonto(double monto);

    void setBeneficios(String beneficios);

    void setEmpleadoId(Integer empleadoId);

    void setCampoOpcional1(String valor);

    void setCampoOpcional2(String valor);

    void setCampoOpcional3(String valor);

    Contrato getResultado();
}
