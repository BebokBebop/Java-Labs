package pl.polsl.lab.servlets.view.results;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import pl.polsl.lab.web.services.reference.BTGWebService_Service;
import pl.polsl.lab.web.services.reference.ResultList;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Lists entries from database table - Result.
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class ViewResults extends HttpServlet {
    /**
     * Web service responsible for managing the database and BTG.
     */
    @WebServiceRef(wsdlLocation = "http://localhost:8080/r6_WebService/BTGWebService?wsdl")
    private BTGWebService_Service service;

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
        List<ResultList> resultList = null;
        try {
            resultList = getResultList();
        } catch (WSException_Exception ex) { //dbError
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
                    + "    <th bgcolor=>Argument</th>\n"
                    + "    <th bgcolor=># Of Calculations</th>\n"
                    + "    <th bgcolor=>Result</th>\n"
                    + "</tr>");
            if (resultList != null) {
                for (ResultList r : resultList) {
                    out.println("<tr>");

                    out.println("<td>");
                    out.println(r.getArgument());
                    out.println("</td>");

                    out.println("<td>");
                    out.println(r.getNoOfCalcs());
                    out.println("</td>");

                    out.println("<td>");
                    out.println(r.getResult());
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
        return "Lists entries from database table - Result";
    }// </editor-fold>

    private List<ResultList> getResultList() throws WSException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        return port.getResultList();
    }

}
