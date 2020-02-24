package pl.polsl.lab.servlets.calculate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.model.BergerTableGenerator;

/**
 * Communicates with the model to get and print the output
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class Calculate extends HttpServlet {
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
        //guarantees one model per session
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

            List<List<Integer>> outp = null;
            try {
                outp = btg.generateBergerTableNew(Integer.parseInt(request.getParameter("teamCount")));
            } catch (MyException ex) {

                out.println("<body>");
                out.println("<h1>Wrong number sent! Must be [2-100]!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            } catch (NumberFormatException ex) {
                out.println("<body>");
                out.println("<h1>Must send a Number!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }

            out.println("<body>");
            out.println("<h1>Generated Berger Table: </h1>");
            boolean dispParity = true;
            for (List<Integer> i : outp) {
                out.println("<h1>|");
                for (int j : i) {
                    out.println(j);
                    if (dispParity) {
                        out.println("-");
                    } else {
                        out.println(" | ");
                    }
                    dispParity = !dispParity;
                }
                out.println("</h1>");
            }
            out.println("</h1>");
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
        return "Generates and displays a berger table.";
    }

}
