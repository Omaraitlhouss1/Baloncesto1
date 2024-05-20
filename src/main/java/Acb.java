
import java.io.*;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.http.*;

public class Acb extends HttpServlet {

    private ModeloDatos bd;

    public void init(ServletConfig cfg) throws ServletException {
        bd = new ModeloDatos();
        bd.abrirConexion();
    }

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession s = req.getSession(true);

        if (req.getParameter("resetVotes") != null) {
            bd.resetearVotos();
            // Crear una cookie para indicar que se han eliminado los votos.
            Cookie mensajeCookie = new Cookie("mensajeVotos",
                    URLEncoder.encode("Todos los votos han sido eliminados.", "UTF-8"));
            mensajeCookie.setMaxAge(60); // Expira en 60 segundos
            res.addCookie(mensajeCookie);

            res.sendRedirect("index.html"); // Redirige de vuelta a la página principal.
            return; // Finaliza la ejecución para no procesar más código.
        }

        String nombreP = (String) req.getParameter("txtNombre");
        String nombre = (String) req.getParameter("R1");
        if (nombre.equals("Otros")) {
            nombre = (String) req.getParameter("txtOtros");
        }
        if (bd.existeJugador(nombre)) {
            bd.actualizarJugador(nombre);
        } else {
            bd.insertarJugador(nombre);
        }
        s.setAttribute("nombreCliente", nombreP);
        // Llamada a la página jsp que nos da las gracias
        res.sendRedirect(res.encodeRedirectURL("TablaVotos.jsp"));
    }

    public void destroy() {
        bd.cerrarConexion();
        super.destroy();
    }
}