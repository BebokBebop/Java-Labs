package pl.polsl.lab.servlets.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import pl.polsl.lab.web.services.reference.BTGWebService_Service;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Checks if user is logging in correctly and prints errors in case one isn't
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class LoginAnswer extends HttpServlet {
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
        String errorMessage = null;
        String name = request.getParameter("nickname");
        String password = request.getParameter("password");

        if (name.length() < 1) {
            errorMessage = "Must type in a nickname! Try again.";
        } else if (password.length() < 1) {
            errorMessage = "Must type in a password! Try again.";
        } else if (name.equals("Guest")) {
            errorMessage = "Guest username is reserved.";
        } else {
            try {
                errorMessage = checkLogin(name, password);
            } catch (WSException_Exception ex) { //database error
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
        }
        if (errorMessage == null) {
            response.setContentType("text/html;charset=UTF-8");

            Cookie loginCookie = new Cookie("user", URLEncoder.encode(name, "UTF-8"));
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
                        + "<form action = \"Login\" method=\"GET\">\n"
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
        return "Responds to the user about unsuccessful attempt to log in or logs them in and redirects to the MainPage.";
    }

    private String checkLogin(String login, String password) throws WSException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        return port.checkLogin(login, password);
    }

}
