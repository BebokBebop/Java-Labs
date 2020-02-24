package pl.polsl.lab.servlets.register;

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
import pl.polsl.lab.entities.user.Users;

/**
 * Checks if user is registering correctly and adds them to the database or
 * prints errors if username or passwords are wrong
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class RegisterAnswer extends HttpServlet {

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

        response.setContentType("text/html;charset=UTF-8");
        String errorMessage = null;
        String name = request.getParameter("nickname");
        String password = request.getParameter("password");

        if (name.length() < 1) {
            errorMessage = "Must type in a nickname! Try again.";
        } else if (password.length() < 1) {
            errorMessage = "Must type in a password! Try again.";
        } else if (!password.equals(request.getParameter("repeatPassword"))) {
            errorMessage = "The passwords are not the same! Try again.";
        } else if (name.equals("admin") && !password.equals("admin")) {
            errorMessage = "Wrong admin password. Ask your server provider.";
        } else if (name.equals("Guest")) {
            errorMessage = "Guest username is reserved.";
        } else {
            try {
                Users user = new Users(name, password);

                utx.begin();
                em = emf.createEntityManager();

                List users = em.createQuery("SELECT u FROM Users u where u.login LIKE :login").setParameter("login", name).getResultList();
                if (!users.isEmpty()) {
                    errorMessage = "Name already in use! Try different one.";
                } else {
                    em.persist(user);
                }
                utx.commit();
            } catch (Exception ex) {
                throw new ServletException(ex);
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }
        if (errorMessage == null) {
            Cookie loginCookie = new Cookie("user", (String) URLEncoder.encode(name, "UTF-8"));
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
                        + "<form action = \"Register\" method=\"GET\">\n"
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
        return "Returns error if registering incorrectly, otherwise adds data to the database and redirects to the MainPage.";
    }
}
