package pl.polsl.lab.servlets.history;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.model.BergerTableGenerator;

/**
 * Gets history from the model and prints it
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class History extends HttpServlet {

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
        BergerTableGenerator btg = (BergerTableGenerator) request.getSession().getAttribute("model");
        if (btg == null) {
            btg = new BergerTableGenerator();
            request.getSession().setAttribute("model", btg);
        }

        response.setContentType("text/html; charset=ISO-8859-2");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Calculate</title>");
            out.println("</head>");

            List<Integer> outp = btg.getHistory();

            out.println("<body>");
            out.println("<h1>TeamCounts used so far: </h1>");
            if (outp.size() > 0) {
                for (Integer i : outp) {
                    out.println("<h1>");
                    out.println(i);
                    out.println("</h1>");
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
        return "Shows history of calculations";
    }

}
