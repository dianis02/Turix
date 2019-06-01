/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turix.controlador;
import javax.faces.application.FacesMessage;
import com.turix.controlador.HibernateUtil;
import com.turix.modelo.Temas;
import java.util.Locale;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.*;

/**
 *
 * @author dianis
 */
@ManagedBean
@RequestScoped
public class TemaController {
    
    private Temas tema = new Temas();
    private final Utility u = new Utility();
    private boolean f = false;
    public String nom;

    public boolean isF() {
        return f;
    }

    public void setF(boolean f) {
        this.f = f;
    }
    
    
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
     public TemaController() {
        FacesContext.getCurrentInstance()
                .getViewRoot()
                .setLocale(new Locale("es-Mx"));
    }

    public Temas getTema() {
        return tema;
    }

    public void setTema(Temas tema) {
        this.tema = tema;
    }
        
     /**
      * Metodo que guarda un Tema
      * manda a llamar a guardarTema de Utility
      * para guardarlo en la BD
      */
     public void guardarTema(){
             u.guardarTema(tema);
            tema = null;         
         }

     /**
      * Metodo que elimina un Tema
      * manda a llamar a eliminarTema de Utility
      * para eliminarlo en la BD
      */
     public void eliminarTema(){
             u.eliminarTema(tema);

     }  

     /**
      * Metodo que elimina un Tema
      * manda a llamar a eliminarTema de Utility
      * para eliminarlo en la BD
      */
     public void eliminarTema(Temas t){
             u.eliminarTema(t);

     }   

     /**
      * Metodo que da la lista de todos los temas
      * manda a llamar darTema de Utility
      * para buscarlos en la BD
      * @return List
      */
     public List listaTemas(){
        return u.darTemas(); 
     }
     

     /**
      * Metodo que busca temas en la BD
      * @return List - lista que coincida el regex
      */
     public List buscarTemas(){
         return u.buscarTemas(nom);
     }
     
     public String redir(){
         f = true;
         return "buscarTemas?faces-redirect=true";
     }
     /**
      * Metodo que da la lista de todos los temas
      * manda a llamar darTema de Utility
      * para buscarlos en la BD
      * @return List
      */
     public List getListaNomTemas(){
        return u.darNomTemas();
     }
     
     
}
