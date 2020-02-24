package pl.polsl.lab.servlets.unregister;

import java.io.IOException;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.lab.entities.calculation.Calculation;
import pl.polsl.lab.entities.user.Users;

/**
 * Removes an user from the database
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class UnRegister extends HttpServlet {

    /**
     * Used to create EntityManager.
     */
    @PersistenceUnit
    private EntityManagerFactory emf;

    /**
     * Used to create transactions.
     */
    @Resource
    private UserTransaction utx;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;

        String name = null;
        response.setContentType("text/html");
        Cookie loginCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    name = cookie.getValue();
                    loginCookie = cookie;
                    break;
                }
            }
        }
        if (loginCookie == null) {
            response.sendRedirect("MainPage");
            return;
        }
        if ("admin".equals(name) || "Guest".equals(name)) {
            response.sendRedirect("MainPage");
            return;
        }

        loginCookie.setMaxAge(0);
        response.addCookie(loginCookie);
        try {
            utx.begin();
            em = emf.createEntityManager();
            Users user = (Users) em.createQuery("select u from Users u where u.login like :name").setParameter("name", name).getSingleResult();
            for (Calculation c : user.getCalculations()) {
                c.setUser(null);
                em.merge(c);
            }
            em.remove(user);
            utx.commit();
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        response.sendRedirect("MainPage");

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Removes an user from the database or prints an error in case the user wasnt in it.";
    }

}
