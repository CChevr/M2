package fr.uge.jee.hellosession;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@WebServlet("/hellosession")
public class HelloSessionBetter extends HttpServlet {
    private final TokenManager tokenManager = new TokenManager();
    private final String NAME = "hellosession";

    private Optional<Cookie> getCookie(HttpServletRequest request) {
        var cookies = request.getCookies();

        if(cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(x -> x.getName().equals(NAME))
                .findAny();
    }

    private void computeScreen(PrintWriter writer, long time) {
        writer.format("<p>Bonjour pour la %d-ème fois", time);
    }

    private Cookie generateCookie(String token) {
        Objects.requireNonNull(token);

        return new Cookie(NAME, token);
    }

    private void manageNewClient(PrintWriter writer, HttpServletResponse response) {
        var token = tokenManager.addClient();
        response.addCookie(generateCookie(token));
        computeScreen(writer, 1L);
    }

    private void manageOldClient(PrintWriter writer, HttpServletResponse response, String token) {
        if (!tokenManager.exists(token)) {
            manageNewClient(writer, response);
            return;
        }

        var time = tokenManager.visitClient(token);
        computeScreen(writer, time);
    }

    private void computeConnection(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
        var cookie = getCookie(request);

        cookie.ifPresentOrElse(c -> manageOldClient(writer, response, c.getValue()),
                () -> manageNewClient(writer, response));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html ; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();

        computeConnection(writer, request, response);

        writer.flush();
    }
}
