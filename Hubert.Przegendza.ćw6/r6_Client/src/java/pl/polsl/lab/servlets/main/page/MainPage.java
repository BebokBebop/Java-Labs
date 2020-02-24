package pl.polsl.lab.servlets.main.page;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main page - from here one can: register login generate a berger table if
 * already logged in, one can view their calculation history if already logged
 * in, one can log out if already logged in, one can unregister view all
 * calculations view all already calculated results if logged as admin, one can
 * view all of the users and their passwords
 *
 * @author Hubert Przegendza
 * @version 1.1
 */
public class MainPage extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "         <title>Berger Table Generator</title>\n"
                    + "    </head>\n"
                    + "     <body>\n");
            if (userName != null) {
                out.println(""
                        + "       <h1>Hi " + userName + "</h1>\n"
                        + "        <br>\n"
                );
            } else {
                out.println(""
                        + "       <h1>You're not logged in.</h1>\n"
                        + "        <br>\n"
                );
            }
            out.println(""
                    + "        <form action = \"Calculate\" method=\"GET\">\n"
                    + "            <p>Team count:<input type=text size=20 name=teamCount></p>\n"
                    + "            <input type=\"submit\" value=\"Calculate berger table!\" />\n"
                    + "        </form>\n");

            if (userName == null) {
                out.println(""
                        + "        <form action=\"Register\">\n"
                        + "            <input type=\"submit\" value=\"Register\" />\n"
                        + "        </form>\n"
                        + "        <form action=\"Login\">\n"
                        + "            <input type=\"submit\" value=\"Login\" />\n"
                        + "        </form>\n");
            } else {
                if (!"admin".equals(userName)) {
                    out.println(""
                            + "        <form action=\"UnRegister\">\n"
                            + "            <input type=\"submit\" value=\"UnRegister\" />\n"
                            + "        </form>\n");
                }
                out.println(""
                        + "        <form action=\"LogOut\">\n"
                        + "            <input type=\"submit\" value=\"Log Out\" />\n"
                        + "        </form>\n"
                        + "      <form action=\"ViewHistory\">\n"
                        + "            <input type=\"submit\" value=\"View Your Calculation History\" />\n"
                        + "        </form>\n");
            }

            out.println(""
                    + "        <form action = \"ViewCalculations\" method=\"GET\">\n"
                    + "            <input type=\"submit\" value=\"View All Calculations\" />\n"
                    + "        </form>\n");
            out.println(""
                    + "        <form action = \"ViewResults\" method=\"GET\">\n"
                    + "            <input type=\"submit\" value=\"View All Results\" />\n"
                    + "        </form>\n");
            if (userName.equals("admin")) {
                out.println(""
                        + "        <form action = \"ViewUsers\" method=\"GET\">\n"
                        + "            <input type=\"submit\" value=\"View All Users\" />\n"
                        + "        </form>\n");
            }

            out.println(""
                    + "    </body>\n"
                    + "</html>");
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
        return "Main page";
    }

}
