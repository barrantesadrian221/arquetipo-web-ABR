package it.pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/consultar")
public class ConsultaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/instituto";
    private static final String USER = "root";
    private static final String PASS = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String sql = "SELECT id, nombre, edad, clase, CASE WHEN edad >= 18 THEN 'Sí' ELSE 'No' END AS mayor FROM alumnos";

        // 1. Encabezado HTML y contenedor con el CSS vinculado
        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Gestión de Alumnos</title>");
        out.println("    <link rel='stylesheet' href='styles.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>Gestión de Alumnos</h1>");
        out.println("        <p>Haz clic en el botón para consultar la base de datos:</p>");
        out.println("        <form action='consultar' method='GET'>");
        out.println("            <button type='submit'>Cargar Datos de Alumnos</button>");
        out.println("        </form>");
        out.println("        <div id='resultado'>");

        // 2. Consulta JDBC y renderizado de la tabla dentro del contenedor
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                out.println("<table>");
                out.println("<thead>");
                out.println("  <tr><th>ID</th><th>Nombre</th><th>Edad</th><th>Clase</th><th>¿Mayor de Edad?</th></tr>");
                out.println("</thead>");
                out.println("<tbody>");

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("id") + "</td>");
                    out.println("<td>" + rs.getString("nombre") + "</td>");
                    out.println("<td>" + rs.getInt("edad") + "</td>");
                    out.println("<td>" + rs.getString("clase") + "</td>");
                    out.println("<td><strong>" + rs.getString("mayor") + "</strong></td>");
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
            }

        } catch (Exception e) {
            out.println("<p style='color: red; margin-top: 15px;'>Error al consultar la base de datos: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        // 3. Cierre de etiquetas HTML
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
}