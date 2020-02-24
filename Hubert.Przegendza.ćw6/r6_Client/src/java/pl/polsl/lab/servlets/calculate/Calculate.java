package pl.polsl.lab.servlets.calculate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import pl.polsl.lab.web.services.reference.BTGWebService_Service;
import pl.polsl.lab.web.services.reference.BtResultRow;
import pl.polsl.lab.web.services.reference.MyException_Exception;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Communicates with the WebService to get and print the output
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class Calculate extends HttpServlet {

    /**
     * Web service responsible for managing the database and BTG.
     */
    @WebServiceRef(wsdlLocation = "http://localhost:8080/r6_WebService/BTGWebService?wsdl")
    private BTGWebService_Service service;

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
        response.setContentType("text/html; charset=ISO-8859-2");
        Integer argument;

        try {
            argument = Integer.parseInt(request.getParameter("teamCount"));
        } catch (NumberFormatException ex) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Must send a Number!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }

        String userName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    userName = c.getValue();
                }
            }
        }

        List<BtResultRow> outp;
        try {
            outp = calculateBergerTable(argument, userName);
        } catch (WSException_Exception ex) { //DB error
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Something is wrong with DataBase! Check WebService!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        } catch (MyException_Exception ex) { //wrong argument
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error!</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Wrong number sent! Must be [2-100]!</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Calculate</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Generated Berger Table: </h1>");

            boolean dispParity = true;
            for (BtResultRow r : outp) {
                out.println("<h1>|");
                for (int j : r.getResult()) {
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

    private java.util.List<pl.polsl.lab.web.services.reference.BtResultRow> calculateBergerTable(java.lang.Integer teamCount, java.lang.String userName) throws WSException_Exception, MyException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        return port.calculateBergerTable(teamCount, userName);
    }

}
