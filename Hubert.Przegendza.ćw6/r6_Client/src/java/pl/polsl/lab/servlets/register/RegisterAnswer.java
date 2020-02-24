package pl.polsl.lab.servlets.register;

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
import pl.polsl.lab.web.services.reference.MyException_Exception;
import pl.polsl.lab.web.services.reference.WSException_Exception;

/**
 * Checks if user is registering correctly and adds them to the database via webservice or
 * prints errors if username or passwords are wrong
 *
 * @author Hubert Przegendza
 * @version 1.5
 */
public class RegisterAnswer extends HttpServlet {
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
        String errorMessage = null;
        String name = request.getParameter("nickname");
        String password = request.getParameter("password");

        if (name.length() < 1) {
            errorMessage = "Must type in a nickname! Try again.";
        } else if (password.length() < 1) {
            errorMessage = "Must type in a password! Try again.";
        } else if (!password.equals(request.getParameter("repeatPassword"))) {
            errorMessage = "The passwords are not the same! Try again.";
        } else if (name.equals("admin") && !password.equals("admin")) {
            errorMessage = "Wrong admin password. Ask your server provider.";
        } else if (name.equals("Guest")) {
            errorMessage = "Guest username is reserved.";
        } else {
            try {
                register(name, password);
            } catch (MyException_Exception ex) { //name already used
                errorMessage = "Name already in use! Try different one.";
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
        }
        
        if (errorMessage == null) {
            Cookie loginCookie = new Cookie("user", (String) URLEncoder.encode(name, "UTF-8"));
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
                        + "<form action = \"Register\" method=\"GET\">\n"
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
        return "Returns error if registering incorrectly, otherwise adds data to the database and redirects to the MainPage.";
    }

    private void register(String login, String password) throws MyException_Exception, WSException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        pl.polsl.lab.web.services.reference.BTGWebService port = service.getBTGWebServicePort();
        port.register(login, password);
    }
}
