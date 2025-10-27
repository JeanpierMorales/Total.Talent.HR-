package Modelo;
// Entidad Cliente basica, util si se integra con contratos orientados a cliente

public class Cliente {

    private Integer id;
    private String nombreEmpresa;
    private String contactoCorreo;
    private String telefono;

    public Cliente() {
    }

    public Cliente(String nombreEmpresa, String contactoCorreo, String telefono) {
        this.nombreEmpresa = nombreEmpresa;
        this.contactoCorreo = contactoCorreo;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getContactoCorreo() {
        return contactoCorreo;
    }

    public void setContactoCorreo(String contactoCorreo) {
        this.contactoCorreo = contactoCorreo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", empresa='" + nombreEmpresa + '\'' + ", correo='" + contactoCorreo + '\'' + '}';
    }
}
