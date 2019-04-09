/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turix.controlador;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.turix.modelo.Usuario;


/**
 *
 * @author dianis
 */
@ManagedBean
@RequestScoped
public class RegistroController {

    private Usuario user = new Usuario();
    private Utility u = new Utility();
    private String confirmarContraseña;

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }

    public void setConfirmarContraseña(String confirmarContraseña) {
        this.confirmarContraseña = confirmarContraseña;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public RegistroController() {
        FacesContext.getCurrentInstance()
                .getViewRoot()
                .setLocale(new Locale("es-Mx"));
    }

    public String agregarUsuario() {
        if (!user.getContraseña().equals(confirmarContraseña)) {
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo de registro: Las contraseñas deben coincidir", ""));
        } else {
            if(u.save(user)){
                user = null;
                FacesContext.getCurrentInstance()
                        .addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_INFO,
                                        "Felicidades, el registro se ha realizado correctamente", ""));
                return "inicio?faces-redirect=true";
            }
            user = null;
        }
        return null;
    }
}
