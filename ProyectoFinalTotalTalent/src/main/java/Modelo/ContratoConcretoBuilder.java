package Modelo;

/**
 * Implementación concreta del patrón Builder para la clase Contrato.
 */
public class ContratoConcretoBuilder implements ContratoBuilder {

    private Contrato contrato;

    // Constructor que recibe el contrato base generado por la Factory
    public ContratoConcretoBuilder(Contrato contratoBase) {
        this.contrato = contratoBase;
    }

    @Override
    public void reset() {
        this.contrato = new Contrato() {
        };
    }

    @Override
    public void setTipo(String tipo) {
        this.contrato.setTipo(tipo);
    }

    @Override
    public void setDuracion(String duracion) {
        this.contrato.setDuracion(duracion);
    }

    @Override
    public void setMonto(double monto) {
        this.contrato.setMonto(monto);
    }

    @Override
    public void setBeneficios(String beneficios) {
        this.contrato.setBeneficios(beneficios);
    }

    @Override
    public void setEmpleadoId(Integer empleadoId) {
        this.contrato.setEmpleadoId(empleadoId);
    }

    // Campos opcionales genéricos
    @Override
    public void setCampoOpcional1(String valor) {
        this.contrato.setCampoOpcional1(valor);
    }

    @Override
    public void setCampoOpcional2(String valor) {
        this.contrato.setCampoOpcional2(valor);
    }

    @Override
    public void setCampoOpcional3(String valor) {
        this.contrato.setCampoOpcional3(valor);
    }

    // Campos opcionales específicos
    public void setHorarioFlexible(boolean flexible) {
        this.contrato.setHorarioFlexible(flexible);
    }

    public void setBonificacionExtra(double bonificacion) {
        this.contrato.setBonificacionExtra(bonificacion);
    }

    public void setComentarios(String comentarios) {
        this.contrato.setComentarios(comentarios);
    }

    @Override
    public Contrato getResultado() {
        return this.contrato;
    }
}
