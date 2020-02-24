package pl.polsl.lab.servlets.view.history;

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
import pl.polsl.lab.entities.calculation.Calculation;
import pl.polsl.lab.entities.user.Users;

/**
 * Views history of calculations for a single - logged-in user.
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class ViewHistory extends HttpServlet {

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
        assert emf != null;
        EntityManager em = null;
        response.setContentType("text/html;charset=UTF-8");

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
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Must be logged in!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }

        em = emf.createEntityManager();
        Users user = (Users) em.createQuery("SELECT u FROM Users u where u.login LIKE :login").setParameter("login", userName).getSingleResult();
        List<Calculation> calcs = user.getCalculations();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Your Calculation History</title>");
            out.println("</head>");

            out.println("<body>");
            out.println("<h1>TeamCounts " + userName + " has used so far: </h1>");

            out.println(""
                    + "<table id=\"aaa\" border=\"3\">\n"
                    + "<tr >\n"
                    + "    <th bgcolor=>Argument</th>\n"
                    //+ "    <th bgcolor=># Of times used</th>\n"
                    + "</tr>");
            if (calcs != null) {
                for (Calculation c : calcs) {
                    out.println("<tr>");

                    out.println("<td>");
                    out.println(c.getArgument());
                    out.println("</td>");

                    //doesn't look good
//                    out.println("<td>");
//                    out.println(user.getAmountOfCalculationsFor(c.getArgument()));
//                    out.println("</td>");
                    out.println("</tr>");
                }
            } else {
                out.println("<h1>");
                out.println("None");
                out.println("</h1>");
            }
            out.println("</body>");
            out.println("</html>");
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
        return "Shows history of calculations for a logged-in user";
    }

}
