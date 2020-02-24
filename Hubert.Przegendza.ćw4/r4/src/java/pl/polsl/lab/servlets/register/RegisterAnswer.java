package pl.polsl.lab.servlets.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.saved.users.UsersDB;

/**
 * Checks if user is registering correctly and prints errors in case one isn't
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class RegisterAnswer extends HttpServlet {

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
        } else {
            UsersDB usersDB = (UsersDB) request.getSession().getAttribute("usersDB");
            if (usersDB == null) {
                usersDB = new UsersDB();
                request.getSession().setAttribute("usersDB", usersDB);
            }
            try {
                usersDB.addUser(name, password);
            } catch (MyException ex) {
                errorMessage = "Name already in use! Try different one.";
            }
        }
        if (errorMessage == null) {
//            try (PrintWriter out = response.getWriter()) {
//                out.println("<!DOCTYPE html>");
//                out.println("<html>");
//                out.println("<head>");
//                out.println("<title>Register Successful</title>");
//                out.println("</head>");
//                out.println("<body>");
//                out.println("<h1>Hello " + name + "!</h1>");
//                out.println("<h1>You can now log in using your nickname and password.</h1>"
//                        + "<form action = \"MainPage\" method=\"GET\">\n"
//                        + "    <input type=\"submit\" value=\"Main Page\" />\n"
//                        + "</form>");
//                out.println("</body>");
//                out.println("</html>");
//            } //cookies do not work this way

            Cookie loginCookie = new Cookie("user", (String) URLEncoder.encode(name, "UTF-8"));
            loginCookie.setMaxAge(20);//20sec for easy debugging
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
        return "Returns error if registering incorrectly, otherwise redirects to the MainPage.";
    }
}
