package fr.uge.jee.hellosession;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hellosessionbetter")
public class HelloSessionBetter extends HttpServlet {
    private String COUNTER_ID = "counter";

    private void buildScreen(PrintWriter writer, Long counter) {
        writer.format("<p>Bonjour pour la %d-ème fois", counter);
    }

    private void computeConnexion(PrintWriter writer, HttpServletRequest request) {
        var session = request.getSession(true);

        if (session.isNew()) {
            session.setAttribute(COUNTER_ID, 1L);
        }

        var counter = (Long) session.getAttribute(COUNTER_ID);
        buildScreen(writer, counter);
        session.setAttribute(COUNTER_ID, (counter + 1L));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html ; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();

        computeConnexion(writer, request);

        writer.flush();
    }
}
