package pl.polsl.lab.servlets.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
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

/**
 * Checks if user is logging in correctly and prints errors in case one isn't
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class LoginAnswer extends HttpServlet {

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

        String errorMessage = null;
        String name = request.getParameter("nickname");
        String password = request.getParameter("password");

        if (name.length() < 1) {
            errorMessage = "Must type in a nickname! Try again.";
        } else if (password.length() < 1) {
            errorMessage = "Must type in a password! Try again.";
        } else if (name.equals("Guest")) {
            errorMessage = "Guest username is reserved.";
        } else {
            em = emf.createEntityManager();

            List users = em.createQuery("SELECT u FROM Users u WHERE u.login LIKE :login AND u.password LIKE :passwd")
                    .setParameter("login", name).setParameter("passwd", password).getResultList();

            if (users.size() != 1) {
                if (name.equals("admin")) {
                    errorMessage = "admin account must be registered first. Ask server provider for the password.";
                } else {
                    errorMessage = "Wrong name or password! Try again.";
                }
            }

        }
        if (errorMessage == null) {
            response.setContentType("text/html;charset=UTF-8");

            Cookie loginCookie = new Cookie("user", URLEncoder.encode(name, "UTF-8"));
            loginCookie.setMaxAge(60);//1min for easy debugging
            response.addCookie(loginCookie);
            response.sendRedirect("MainPage");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println(""
                        + "<h1>" + errorMessage + "</h1>"
                        + "<form action = \"Login\" method=\"GET\">\n"
                        + "    <input type=\"submit\" value=\"Try Again\" />\n"
                        + "</form>");
                out.println("</body>");
                out.println("</html>");
            }

        }
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
        return "Responds to the user about unsuccessful attempt to log in or logs them in and redirects to the MainPage.";
    }

}
