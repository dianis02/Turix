/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.turix.controlador;

import com.turix.modelo.Calificar;
import com.turix.modelo.Login;
import com.turix.modelo.Comentarios;
import com.turix.modelo.Marcadores;
import com.turix.modelo.Temas;
import com.turix.modelo.Temporal;
import com.turix.modelo.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import static sun.security.jgss.GSSUtil.login;



/**
 *
 * @author miguel
 */
public class Utility {

    static Usuario userObj;
    static Session sessionObj;
    static Comentarios comObj;
    static Temas temaObj;
    static Temporal temObj;
    static Marcadores marcaObj;
    static Comentarios c = new Comentarios();


    public List getUsuario(){
        List l = null;
        try {
            sessionObj = HibernateUtil.getSessionFactory().openSession();
            System.out.println("Session " + sessionObj);
            Query q = sessionObj.createSQLQuery("SELECT * "
                    + "FROM notitia.Comentarista"
                    + "notitia.Comentarista('"+userObj.getNombre_usuario()+"','"+userObj.getCorreo()+"')");
            l = q.list();
            System.out.println("\n.......Records loades Successfully from the Database.......\n");

        } catch (HibernateException sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction03 Is Being Rolled Back.......\n"
                        + "Values of usu");
                sessionObj.getTransaction().rollback();
            }
            l = null;
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }

        }
        return l;

    }

    /**Metodo que busca en la base de datos un usuario en especifico
     *
     * @param login -- Un objeto de tipo login
     * @return Un usuario si ambos atributos en login son correctos, null si no
     */
    public Usuario login(Login login){
        Usuario usuario = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT *  FROM notitia.Usuario('"+login.getUsuario()+
                "','"+login.getContraseña()+"');";
        List l;
        try {
            Transaction tx = sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
            usuario = (q.list().isEmpty())? null:(Usuario) q.list().get(0);
            tx.commit();
        } catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction02 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {


            if (sessionObj != null && sessionObj.isOpen()) {
                sessionObj.close();
            }
        }
        return usuario;
    }

    /**Un metodo para actualizar un usuario cuya contraseña ha sido cambiada
     *
     * @param user -- Usuario a buscar
     * @param usuario -- El mismo usuario con los cambios hechos (no por nombre)
     * @return True si se hizo el update
     */
    public boolean update(Usuario user){
        boolean success = false;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction tx = sessionObj.beginTransaction();
            sessionObj.update(user);
            tx.commit();
            success = true;
        } catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction01 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {


            if (sessionObj != null && sessionObj.isOpen()) {
                sessionObj.close();
            }
        }
        return success;
    }

    /**Un metodo para actualizar un usuario cuya contraseña no ha sido cambiada
     *
     * @param usuario -- Usuario modificado (no por nombre)
     */
    public void update1(Usuario usuario){
        try {
            sessionObj = HibernateUtil.getSessionFactory().openSession();
            sessionObj.beginTransaction();

            sessionObj.update(usuario);

            // Committing The Transactions To The Database
            sessionObj.getTransaction().commit();
        } catch (HibernateException sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction0 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
    }

//        List l = null;
    /**Metodo para borrar un usuario de la base de datos
     *
     * @param user -- Usuario a borrar
     */
    public void delete(Usuario user){
        boolean guardar = false;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
          try{
             sessionObj.beginTransaction();
               guardar = user!=null;
                sessionObj.delete(user);
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction1 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: El usuario ya ha sido eliminado", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se eliminó correctamente el usuario", ""));

        }

    }

    /**
     * Metodo para eliminar un tema de la BD
     * @param t
     */
    public void eliminarUsuario(Usuario user){
      boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();

        //query para restricciones
        String query = "SELECT * FROM notitia.Usuario "
                   + "WHERE notitia.Usuario.nombre = '"+ user.getNombre_usuario() +"';";
          try{
            sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
            l = q.list();
            if(!l.isEmpty()){
                guardar = true;
                Usuario usuario = (Usuario)sessionObj.get(Usuario.class, user.getNombre_usuario());
                sessionObj.delete(usuario);
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
            }
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction2 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
        }

          //mensajes de error o exito
        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: No existe el Tema a eliminar", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se eliminó correctamente el tema", ""));

        }
      }


    public void save() {
        try {
            sessionObj = HibernateUtil.getSessionFactory().openSession();
            System.out.println("Session " + sessionObj);
            sessionObj.beginTransaction();

            sessionObj.save(userObj);

            System.out.println("\n.......Records Saved Successfully To The Database.......\n");

            // Committing The Transactions To The Database
            sessionObj.getTransaction().commit();
        } catch (HibernateException sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction3 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
    }
    /**
     * Metodo para guardar a un Usuario en la base de datos al momento de registrar
     * @param user
     * @return true si fue exitosa la transaccion
     */
    public void save(Usuario user) {

          sessionObj = HibernateUtil.getSessionFactory().openSession();
        try {

            sessionObj.beginTransaction();
            sessionObj.save(user);
            sessionObj.getTransaction().commit();

        } catch (Exception sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction4 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }

        }
    }
    
    /**
     * Metodo para guardar a una Calificacion en la base de datos al momento de registrar
     * @param cal
     * @return true si fue exitosa la transaccion
     */
    public void save(Calificar cal) {

          sessionObj = HibernateUtil.getSessionFactory().openSession();
        try {

            sessionObj.beginTransaction();
            sessionObj.save(cal);
            sessionObj.getTransaction().commit();

        } catch (Exception sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......TransactionCal Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }

        }
    }
    
    /**
     * Metodo para guardar a una Calificacion en la base de datos al momento de registrar
     * @param cal
     * @return true si fue exitosa la transaccion
     */
    public void update(Calificar cal) {

          sessionObj = HibernateUtil.getSessionFactory().openSession();
        try {

            sessionObj.beginTransaction();
            sessionObj.update(cal);
            sessionObj.getTransaction().commit();

        } catch (Exception sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......TransactionCal Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }

        }
    }
    
    
    /**
     * Metodo para pre-registro de un Usuario en la base de datos
     * @param user
     * @return true si fue exitosa la transaccion
     */
    public boolean saveTemp(Temporal user) {
        //querys para hacer verificaciones
        boolean guardar = false;
          sessionObj = HibernateUtil.getSessionFactory().openSession();
           String queryNombre = "SELECT * FROM notitia.Usuario "
                   + "WHERE notitia.Usuario.nombre_usuario LIKE '"+ user.getNombre_usuario()+"';";
            String queryCorreo = "SELECT * FROM notitia.Usuario "
                   + "WHERE notitia.Usuario.correo LIKE '"+ user.getCorreo() +"';";
           //iniciamos transaccion
        try {

            sessionObj.beginTransaction();
            Query q1 = sessionObj.createSQLQuery(queryNombre).addEntity(Temporal.class);
            Query q2 = sessionObj.createSQLQuery(queryCorreo).addEntity(Temporal.class);
            if(q1.list().isEmpty()&& q2.list().isEmpty()){
                guardar = true;
                sessionObj.save(user);
                sessionObj.getTransaction().commit();
            }
        } catch (Exception sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction5 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
        //Mensajes de exito o error
          if (!user.getContraseña().equals(user.getConfirmaContrasena())) {
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo de registro: Las contraseñas deben coincidir", ""));
        } else if (!guardar){
             FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo de registro: elija un correo o usuario distinto", ""));


        }else {
            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Revise su correo e ingrese el código en Confirmar", ""));
        }
          return guardar;
    }



    /**
     * Metodo para guarda el tema en la base de datos
     * @param tema
     */
    public void guardarTema(Temas tema){
      boolean guardar = false;
        List l = null;
       sessionObj = HibernateUtil.getSessionFactory().openSession();
       //query para verificacion

        String query = "SELECT * FROM notitia.Temas "
                   + "WHERE notitia.Temas.nombre LIKE '"+ tema.getNombre() +"';";
          try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Temas.class);
            l = q.list();
            if(l.isEmpty()){
                guardar = true;
               sessionObj.save(tema);
                sessionObj.getTransaction().commit();
            }
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction6 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

          //mensajes de error o exito

        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: Ya existe el tema", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Felicidades, se agrego correctamente el tema", ""));

        }
    }
    /**
     * Metodo para buscar una lista de temas de la BD
     * @param temas -- Una cadena la cual se usara como regex
     * @return Lista que coinciden con la regex
     */
    public List buscarTemas(String temas){
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "select * from notitia.Temas where nombre ILIKE '%"+ temas +"%';";
        Query q = sessionObj.createSQLQuery(query).addEntity(Temas.class);
        try{
            l = q.list();
        }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction7 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            l = null;
        } finally {
            return l;
        }
    }


    /**
     * Metodo para eliminar un tema de la BD
     * @param t
     */
    public void eliminarTema(Temas t){
      boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        //query para restricciones
        String query = "SELECT * FROM notitia.Temas "
                   + "WHERE notitia.Temas.nombre LIKE '"+ t.getNombre() +"';";
          try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Temas.class);
            l = q.list();
            if(!l.isEmpty()){
                guardar = true;
               Temas tema = (Temas)sessionObj.get(Temas.class,t.getNombre());
                sessionObj.delete(tema);
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
            }
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction9 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
        }

          //mensajes de error o exito
        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: No existe el Tema a eliminar", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se eliminó correctamente el tema", ""));

        }
      }
    /**
     * Metodo auxiliar para saber si existe el Tema en la BD
     * @param t
     * @return Temas
     */
    public Temas existeTema(String t){
        List l = null;
        Temas tema = new Temas();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Temas "
                   + "WHERE notitia.Temas.nombre LIKE '"+t+"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Temas.class);
        l = q.list();
        if(!l.isEmpty()){
            tema =(Temas)l.get(0);
        }
        return tema;
     }

      public String getCorreo(Temporal t){
        List l = null;
        Temporal u = new Temporal();
        String correo = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Temporal "
                   + "WHERE notitia.Temporal.nombre_usuario LIKE '"+t.getNombre_usuario()+"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Temporal.class);
        l = q.list();
        if(!l.isEmpty()){
             u=(Temporal)l.get(0);
             correo=u.getCorreo();
        }
        return correo;
     }

    /**
     * Metodo auxiliar para saber si existe el marcador en la BD
     * @param m
     * @return null si no existe
     */
    public Marcadores existeMarcador(String m){
         List l = null;
        Marcadores mar = new Marcadores();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.marcadores "
                   + "WHERE notitia.marcadores.ubicacion LIKE '"+m+"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Marcadores.class);
        l = q.list();
        if(!l.isEmpty()){
            mar =(Marcadores)l.get(0);
        }
        return mar;

     }

     /**
      * Metodo para ver si existe un usuario
      * @param u -- un string
      * @return null si no existe
      */
     public Usuario existeUsuario(String u){
        List l = null;
        Usuario usuario = new Usuario();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Usuario "
                   + "WHERE notitia.Usuario.nombre_usuario LIKE '"+u+"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
        l = q.list();
        if(!l.isEmpty()){
            usuario =(Usuario)l.get(0);
        }
        return usuario;

     }

     /**
      * Metodo para guardar un marcador en la BD
      * @param marcador
      */
    public void guardarMarcador(Marcadores marcador){
        boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        //Querys para restricciones
        String query = "SELECT * FROM notitia.Marcadores "
                   + "WHERE notitia.Marcadores.nombre LIKE '"+ marcador.getUbicacion() +"';";
        String queryT = "SELECT * FROM notitia.Temas "
                  + "WHERE notitia.Temas.nombre LIKE '"+ marcador.getTemas().getNombre() +"';";
         //inicio transaccion
          try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Marcadores.class);
            Query qT = sessionObj.createSQLQuery(queryT).addEntity(Temas.class);
            l = q.list();

            if(l.isEmpty()&& !qT.list().isEmpty()&& !marcador.getInformador().equals(null)){
                guardar = true;
               sessionObj.save(marcador);
                sessionObj.getTransaction().commit();
            }
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction8 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

          //mensaje de error o exito
        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: Ya existe el marcador o el Tema asociado no existe", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Felicidades, se agrego correctamente el marcador", ""));

        }


    }

    /**
     * Muestra si hay usuarios en espera de asignación
     * @return si hay usuarios en espera de asignación
     */
    public boolean hayUsuariosEnEspera() {
        return this.contarEnEspera() > 0;
    }    

    /**
     * Devuelve la cantidad de usuarios en espera de asignación
     * @return la cantidad de usuarios en espera de asignación
     */
    public int contarEnEspera() {
        return this.darUsuarios().size();
    }

    /**
     * Devuelve la lista de usuarios en espera de asignación
     * @return la lista de usuarios en espera de asignación
     */
    public List darUsuarios() {
        List l;
        Usuario u = new Usuario();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Usuario WHERE en_espera AND NOT es_informador AND NOT nombre_usuario = 'Admin'";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
        l = q.list();
        return l;
    }

    /**
     * Devuelve la lista de usuarios registrados en la base
     * @return la lista de usuarios registrados en la base
     */
    public List darUsuariosRegistrados() {
        List l;
        Usuario u = new Usuario();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Usuario WHERE not nombre_usuario = 'Admin'";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
        l = q.list();
        return l;
    }

    /**
     * Devuelve la lista de informadores registrados en la base
     * @return la lista de informadores registrados en la base
     */
    public List darInformadores() {
        List l;
        Usuario u = new Usuario();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Usuario WHERE not nombre_usuario = 'Admin' AND es_informador";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
        l = q.list();
        return l;
    }

    /**
     * Devuelve la lista de comentaristas registrados en la base
     * @return la lista de comentaristas registrados en la base
     */
    public List darComentaristas() {
        List l;
        Usuario u = new Usuario();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Usuario WHERE NOT nombre_usuario = 'Admin' AND NOT es_informador";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
        l = q.list();
        return l;
    }

    /**
     * Devuelve la lista de comentaristas registrados en la base
     * @return la lista de comentaristas registrados en la base
     */
    public List darEncontrados(String nombre) {
        List l;
        Usuario u = new Usuario();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Usuario WHERE NOT nombre_usuario = 'Admin' AND nombre_usuario ILIKE '%"+nombre+"%'";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Usuario.class);
        l = q.list();
        return l;
    }

    /**
     * Metodo para enlistar todos los temas
     * @return list
     */
     public List darTemas(){
        List l = null;
        Temas tema = new Temas();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Temas ";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Temas.class);
        l = q.list();
        return l;
     }
     /**
      * 
      * @param m
      * @return Lista de los temas del usuario loggeado
      */
     public List darMisTemas(String m){
         List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Temas "
                   + "WHERE notitia.Temas.nombre_usuario LIKE '"+  m+"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Temas.class);
        l = q.list();
        return l;
     }
     

     /**
     * Metodo para enlistar todos los temas
     * @return list
     */
     public List darNomTemas(){
         List l = null;
        Temas tema = new Temas();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT nombre FROM notitia.Temas ";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query);
        l = q.list();

        return l;
     }

     /**
      * Metodo para enlistar todos los marcadores
      * @return list
      */
     public List darMarcadores(){
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Marcadores ";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Marcadores.class);
        l = q.list();
        return l;
     }


    /**
     * Metodo para enlistar todos los comentarios
     * @return list
     */
     public List darComentariosAdmin(){
        List l = null;
        Comentarios coment = new Comentarios();
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Comentarios ";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Comentarios.class);
        l = q.list();
        return l;
     }


     /**
      * Metodo para eliminar un marcador de BD
      * @param m
      */
     public void eliminarMarcador(Marcadores m){
        boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        //query para restricciones
        String query = "SELECT * FROM notitia.Marcadores "
                   + "WHERE notitia.Marcadores.ubicacion LIKE '"+ m.getUbicacion() +"';";
          try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Marcadores.class);
            l = q.list();
            if(!l.isEmpty()){
                guardar = true;
               Marcadores marcador = (Marcadores)sessionObj.get(Marcadores.class,m.getUbicacion());
                sessionObj.delete(marcador);
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
            }
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction10 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

          //mensajes de error o exito
        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: No existe el marcador a eliminar", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se eliminó correctamente el marcador", ""));

        }
     }

     /**
      * Metodo para dar un marcador por su ubicacion
      * @param m -- un string
      * @return Una lista de marcadores
      */
     public List dameMarcadoresT(String m){
         List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Marcadores "
                   + "WHERE notitia.Marcadores.ubicacion LIKE '"+  m +"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Marcadores.class);
        l = q.list();
        return l;
     }
    //Métodos para comentariosCOntroller
     /**
      * Metodo para dar un comentario por su ubicacion
      * @param m -- Un string
      * @return Lista de comentarios
      */
     public List darComentarios(String m){
         List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Comentarios "
                   + "WHERE notitia.Comentarios.ubicacion LIKE '"+  m+"'";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Comentarios.class);
        l = q.list();
        return l;
     }
     /**
      * Metodo para ver si existe un usuario
      * @param usuario -- un string
      * @return  true si existe
      */
     public boolean verificaUsuario(String usuario){
         boolean es = false;
         Usuario a = null;
         sessionObj = HibernateUtil.getSessionFactory().openSession();
        try{
            String hq1 = "FROM notitia.usuario WHERE NOMBRE = '" + usuario ;
            Query query = sessionObj.createQuery(hq1);

            if (!query.list().isEmpty()){
                a = (Usuario) query.list().get(0);
                return es =true;
            }
        }catch (Exception e){
            throw e;
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
         return es;
     }

    public List getMarca() throws SQLException{
        boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT ubicacion FROM notitia.Marcadores";
        System.out.println(query);
        try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query);
            l = q.list();
            System.out.println(l);
            if(!l.isEmpty()){
                guardar = true;
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
                return l;
            }
        }
            catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction09 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }
        return l;
    }

    public List getMiUsuario() throws SQLException{
        boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT nombre_usuario FROM notitia.Usuario";
        System.out.println(query);
        try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query);
            l = q.list();
            System.out.println(l);
            if(!l.isEmpty()){
                guardar = true;
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
            }
        }
            catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction08 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }
        return l;
    }
    /**
      * Metodo para guardar un comentario
      * @param comentario -- Comentario a guardar
      */
    public void guardarComentario(Comentarios comentario) {
        boolean guardar = false;
        List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Comentarios "
                   + "WHERE notitia.Comentarios.ubicacion LIKE '"+ comentario.darUbicacion() +"';";
        String queryT = "SELECT * FROM notitia.Marcadores "
                   + "WHERE notitia.Marcadores.ubicacion LIKE '"+ comentario.darUbicacion() +"';";
          try{
         sessionObj.beginTransaction();
            Query q = sessionObj.createSQLQuery(query).addEntity(Comentarios.class);
            Query qT = sessionObj.createSQLQuery(queryT).addEntity(Marcadores.class);
            l = q.list();
              System.out.println(qT.list());
            if(!qT.list().isEmpty()){
                guardar = true;
               sessionObj.save(comentario);
                sessionObj.getTransaction().commit();
            }
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction07 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: No existe el marcador", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Felicidades, se agrego correctamente el comentario", ""));

        }
    }
    /**
      * Metodo para borrar un comentario
      * @param comentario -- Objeto Comentario
      */
    public void borrarComentario(Comentarios comentario){
        boolean guardar = false;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
          try{
         sessionObj.beginTransaction();

               guardar = comentario!=null;
                sessionObj.delete(comentario);
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction06 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: No existe el marcador a editar", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se editó correctamente el comentario", ""));

        }
    }

    /**
      * Metodo auxiliar para actualizar
      * @param id_comentario-- Id del comentario que queremos obtener
      * @return  El comentario
      */
     public Comentarios obtenerC (int id_comentario){
        boolean guardar = false;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        Comentarios come = null;
          try{
         sessionObj.beginTransaction();

               come = (Comentarios)sessionObj.get(Comentarios.class,id_comentario);
               guardar = come!=null;
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction05 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
              return come;


    }
     }

    /**
      * Metodo para actualizar un comentario
      * @param comentario -- Comentario a actualizar
      */
    public void actualizarComentario(Comentarios comentario){
        boolean guardar = false;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
          try{
         sessionObj.beginTransaction();

               guardar = comentario!=null;
                sessionObj.update(comentario);
                if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction04 Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
        } finally {
              if (sessionObj != null) {
              sessionObj.close();
          }
          }

        if (!guardar) {
           FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: No existe el marcador a editar", ""));
        } else {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se editó correctamente el comentario", ""));

        }
    }

  /**
   * Elimina a un informador de la base de datos
   * @param user -- el usuario que desea eliminarse de la base
   */
  public void eliminarInformador(Usuario user) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public Usuario confirmar(String cod){
        List l = null;
        Usuario u = new Usuario();
        Temporal t= new Temporal();
        String codigo = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Temporal "
                   + "WHERE notitia.Temporal.codigo LIKE '"+cod+"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Temporal.class);
        l = q.list();
        if(l.isEmpty()){
         FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fallo: el código no coincide" , ""));

        }else{
             t=(Temporal)l.get(0);
             u.setContraseña(t.getContraseña());
             u.setNombre_usuario(t.getNombre_usuario());
             u.setCorreo(t.getCorreo());
             u.setConfirmaContrasena(t.getConfirmaContrasena());
             u.setEs_informador(t.isEs_informador());

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Felicidades, se finalizó tu registro con éxito", ""));

        }
        return u;
    }

         /**
      * Metodo para filtrar los marcadores
      * @param t -- un string
      * @return Una lista de marcadores
      */
     public List filtrar(String t){
         List l = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        String query = "SELECT * FROM notitia.Marcadores "
                   + "WHERE notitia.Marcadores.nombre LIKE '"+  t +"';";
        sessionObj.beginTransaction();
        sessionObj.getTransaction().commit();
        Query q = sessionObj.createSQLQuery(query).addEntity(Marcadores.class);
        l = q.list();
        return l;
     } 
     
     public Calificar calificacion(Usuario u, Comentarios c){
         List l = null;
         sessionObj = HibernateUtil.getSessionFactory().openSession();
         try{
             sessionObj.beginTransaction();
             String query = "SELECT * FROM notitia.Calificar "
                     + " WHERE nombre_usuario = '"+u.getNombre_usuario()+"' AND "
                     + " id_comentario = "+c.getId_comentario()+";";
             Query q = sessionObj.createSQLQuery(query).addEntity(Calificar.class);
             l = q.list();
             sessionObj.getTransaction().commit();
         }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction@@ Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }   
         }finally{
             if (sessionObj != null) {
              sessionObj.close();
          }
         }
         if(l.isEmpty())
                 return null;
         
         return (Calificar)l.get(0);
     }
     
//     private boolean aCalificado(Usuario u, Comentarios c){
//         List l = null;
//         if(sessionObj == null)
//             return false;
//         try{
//             sessionObj.beginTransaction();
//             String query = "SELECT * FROM notitia.Calificar "
//                     + " WHERE nombre_usuario = '"+u.getNombre_usuario()+"' AND "
//                     + " id_comentario = "+c.getId_comentario()+";";
//             Query q = sessionObj.createSQLQuery(query).addEntity(Calificar.class);
//             l = q.list();
//             sessionObj.getTransaction().commit();
//         }catch (HibernateException e) {
//            if (null != sessionObj.getTransaction()) {
//                System.out.println("\n.......Transaction@@ Is Being Rolled Back.......");
//                sessionObj.getTransaction().rollback();
//            }   
//         }
//         if(l == null || l.isEmpty())
//                 return false;
//         
//         return true;
//     }
     
     public void delete(Calificar cal){
        sessionObj = HibernateUtil.getSessionFactory().openSession();
        //query para restricciones
          try{
            sessionObj.beginTransaction();
            sessionObj.delete(cal);
            if (sessionObj.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                sessionObj.getTransaction().commit();
          }catch (HibernateException e) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......TransactionCal Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
          } finally {
              if (sessionObj != null) {
              sessionObj.close();
        }
     }
     
    }
}
