package pl.polsl.lab.servlets.view.history;

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
import pl.polsl.lab.web.services.reference.History;
import pl.polsl.lab.web.services.reference.MyException_Exception;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Views history of calculations for a single - logged-in user.
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class ViewHistory extends HttpServlet {

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
        List<History> HistoryList = null;
        try {
            HistoryList = getHistoryList(userName);
        } catch (MyException_Exception ex) { //userName == null            
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
        } catch (WSException_Exception ex) {
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
            if (HistoryList != null) {
                for (History c : HistoryList) {
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

    private List<History> getHistoryList(java.lang.String userName) throws MyException_Exception, WSException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        return port.getHistoryList(userName);
    }

}
