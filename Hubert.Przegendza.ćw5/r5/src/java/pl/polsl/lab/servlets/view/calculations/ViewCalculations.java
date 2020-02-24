package pl.polsl.lab.servlets.view.calculations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.lab.entities.calculation.Calculation;

/**
 * Lists all of the calculations in the database
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class ViewCalculations extends HttpServlet {

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
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;
        List<Calculation> calcs = null;
        try {
            em = emf.createEntityManager();

            calcs = em.createQuery("SELECT c FROM Calculation c").getResultList();
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
            out.println("<h1>Calculations done by all users</h1>");

            out.println(""
                    + "<table id=\"aaa\" border=\"3\">\n"
                    + "<tr >\n"
                    + "    <th bgcolor=>ID</th>\n"
                    + "    <th bgcolor=>Argument</th>\n"
                    + "    <th bgcolor=>Username</th>\n"
                    + "</tr>");
            if (calcs != null) {
                for (Calculation c : calcs) {
                    out.println("<tr>");

                    out.println("<td>");
                    out.println(c.getId());
                    out.println("</td>");

                    out.println("<td>");
                    out.println(c.getArgument());
                    out.println("</td>");

                    out.println("<td>");
                    if(c.getUser() != null)
                    out.println(c.getUser().getLogin());
                    else{
                        out.println("-deletedUser-");
                    }
                    //or
                    //out.println(c.getUser().getID());
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
        return "Prints a list of all the calculations with names of users that did them";
    }// </editor-fold>

}
