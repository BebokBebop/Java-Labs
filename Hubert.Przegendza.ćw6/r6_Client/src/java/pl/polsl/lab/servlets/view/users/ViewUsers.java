package pl.polsl.lab.servlets.view.users;

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
import pl.polsl.lab.web.services.reference.MyException_Exception;
import pl.polsl.lab.web.services.reference.UserList;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Lists database entries for Users (must be logged as admin)
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class ViewUsers extends HttpServlet {
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
        String userName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    userName = c.getValue();
                }
            }
        }
        List<UserList> userList = null;
        try {
            userList = getUserList(userName);
        } catch (MyException_Exception ex) { //not admin
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
        } catch (WSException_Exception ex) { //db error
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
                    + "    <th bgcolor=>ID</th>\n"
                    + "    <th bgcolor=>Login</th>\n"
                    + "    <th bgcolor=>Password</th>\n"
                    + "    <th bgcolor=># Of Calculations</th>\n"
                    + "</tr>");
            if (userList != null) {
                for (UserList u : userList) {
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
                    out.println(u.getNoOfCalcs());
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

    private List<UserList> getUserList(java.lang.String userName) throws WSException_Exception, MyException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        return port.getUserList(userName);
    }

}
