package pl.polsl.lab.servlets.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.saved.users.UsersDB;

/**
 * Checks if user is logging in correctly and prints errors in case one isn't
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class LoginAnswer extends HttpServlet {

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
        } else {
            UsersDB usersDB = (UsersDB) request.getSession().getAttribute("usersDB");
            if (usersDB == null) {
                usersDB = new UsersDB();
                request.getSession().setAttribute("usersDB", usersDB);
            }

            if (!usersDB.checkUser(name, password)) {
                errorMessage = "Wrong name or password! Try again.";
            }

        }
        if (errorMessage == null) {
            response.setContentType("text/html;charset=UTF-8");
//            try (PrintWriter out = response.getWriter()) {
//                out.println("<!DOCTYPE html>");
//                out.println("<html>");
//                out.println("<head>");
//                out.println("<title>Log in Successful</title>");
//                out.println("</head>");
//                out.println("<body>");
//                out.println("<h1>Hello " + name + "!</h1>");
//                out.println("<h1>You are now logged in.</h1>"
//                        + "<form action = \"MainPage\" method=\"GET\">\n"
//                        + "    <input type=\"submit\" value=\"Main Page\" />\n"
//                        + "</form>");
//                out.println("</body>");
//                out.println("</html>");
//            } //cookies do not work this way

            Cookie loginCookie = new Cookie("user", URLEncoder.encode(name, "UTF-8"));
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
        return "Responds to the user about unsuccessful attempt to log in or redirects to the MainPage.";
    }

}
