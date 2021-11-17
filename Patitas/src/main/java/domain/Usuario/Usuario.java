package domain.Usuario;

import domain.Asociacion.Asociacion;
import domain.PersistenteID;
import utils.passwordUtilities.HashPassword;

import javax.persistence.*;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Column
    @Id
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuil_persona", referencedColumnName = "cuil")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_asociacion", referencedColumnName = "id")
    private Asociacion asociacion;

    @Enumerated(EnumType.STRING)
    private NivelAutorizacion autorizacion;

    public void crearVoluntarioEnAsociacionPropia(Usuario usuario) {
        if (this.autorizacion != NivelAutorizacion.VOLUNTARIO || this.asociacion == null)
            return;

        usuario.setAutorizacion(NivelAutorizacion.VOLUNTARIO);
        usuario.setAsociacion(this.asociacion);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public NivelAutorizacion getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(NivelAutorizacion autorizacion) {
        this.autorizacion = autorizacion;
    }

    public boolean autenticate(String candidate) {
        return HashPassword.correctPassword(candidate, this.passwordHash);
    }
}
