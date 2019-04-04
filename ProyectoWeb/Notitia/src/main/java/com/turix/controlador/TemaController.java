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
import javax.faces.context.FacesContext;
import org.hibernate.*;

/**
 *
 * @author dianis
 */
public class TemaController {
    
    private Temas tema;
    private Utility u = new Utility();
    
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
     
   
     
     public void guardarTema(String id, String des){
          if (!tema.getNombre().equals(u.existeTema(tema))) {
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: Ya existe el tema", ""));
        } else {
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Felicidades, se agrego correctamente el tema", ""));
             u.guardarTema(tema);
            tema = null;
        }
         
        
         
         }
     
     public LinkedList<Temas> listTemas(){
         LinkedList<Temas> lista = new LinkedList<Temas>();
         return lista;
         
     }
     
     public String buscarTema(Temas tema){
         String id = null;
         return id;
         
     }
     
     public void eliminarTema(Temas tema){
         
     }
     
     
     
     
    
    
}
