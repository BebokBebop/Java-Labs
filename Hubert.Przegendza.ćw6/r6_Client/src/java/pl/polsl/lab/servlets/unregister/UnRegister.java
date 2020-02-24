package pl.polsl.lab.servlets.unregister;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import pl.polsl.lab.web.services.reference.BTGWebService_Service;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Removes an user from the database via webservice
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class UnRegister extends HttpServlet {
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
        String name = null;
        response.setContentType("text/html");
        Cookie loginCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    name = cookie.getValue();
                    loginCookie = cookie;
                    break;
                }
            }
        }
        if (loginCookie == null) {
            response.sendRedirect("MainPage");
            return;
        }
        if ("admin".equals(name) || "Guest".equals(name)) {
            response.sendRedirect("MainPage");
            return;
        }

        loginCookie.setMaxAge(0);
        response.addCookie(loginCookie);
        try {
            unRegister(name);
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
        response.sendRedirect("MainPage");

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
        return "Removes an user from the database or prints an error in case the user wasnt in it.";
    }

    private void unRegister(String login) throws WSException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        port.unRegister(login);
    }

}
