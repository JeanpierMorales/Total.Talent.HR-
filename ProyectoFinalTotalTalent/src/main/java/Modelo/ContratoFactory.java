package Modelo;

public class ContratoFactory {

    public Contrato crearContrato(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de contrato no valido");
        }
        switch (tipo.toLowerCase()) {
            case "planilla":
                return new ContratoPlanilla();
            case "locacion":
                return new ContratoLocacion();
            case "parcial":
                return new ContratoParcial();
            default:
                throw new IllegalArgumentException("Tipo de contrato no valido");
        }
    }
}
