package pl.polsl.lab.servlets.view.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
import pl.polsl.lab.entities.user.Users;

/**
 * Lists database entries for Users (must be logged as admin)
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ViewUsers extends HttpServlet {

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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        assert emf != null;
        EntityManager em = null;
        
        String userName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    userName = c.getValue();
                }
            }
        }

        if (userName == null) {
            userName = "wrong";//to reduce amount of nested ifs
        }
        if (!userName.equals("admin")) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Must be an admin!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }

        List<Users> users = null;
        try {
            em = emf.createEntityManager();

            users = em.createQuery("SELECT u FROM Users u").getResultList();
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ViewCalculations</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Calculations done all users</h1>");

            out.println(""
                    + "<table id=\"aaa\" border=\"3\">\n"
                    + "<tr >\n"
                    + "    <th bgcolor=>ID</th>\n"
                    + "    <th bgcolor=>Login</th>\n"
                    + "    <th bgcolor=>Password</th>\n"
                    + "    <th bgcolor=># Of Calculations</th>\n"
                    + "</tr>");
            if (users != null) {
                for (Users u : users) {
                    out.println("<tr>");

                    out.println("<td>");
                    out.println(u.getID());
                    out.println("</td>");

                    out.println("<td>");
                    out.println(u.getLogin());
                    out.println("</td>");

                    out.println("<td>");
                    out.println(u.getPassword());
                    out.println("</td>");

                    out.println("<td>");
                    out.println(u.getHistory().size());
                    out.println("</td>");

                    out.println("</tr>");
                }
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Lists database entries for Users (must be logged as admin)";
    }// </editor-fold>

}
