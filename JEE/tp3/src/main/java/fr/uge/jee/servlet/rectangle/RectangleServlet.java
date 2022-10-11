package fr.uge.jee.servlet.rectangle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet("/rectangle")
public class RectangleServlet extends HttpServlet {
    private String page;

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        var lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines();
        return lines.collect(Collectors.joining("\n"));
    }

    private void emptyPage(PrintWriter writer) {
        writer.format(this.page, "", "", "");
    }

    private void finalPage(PrintWriter writer, int height, int width) {
        Objects.requireNonNull(writer);

        writer.format("<p>Area of the rectangle is: %d</p>", (width * height));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (this.page == null) {
            InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/templates/rectangle-form.html");
            this.page = readFromInputStream(inputStream);
        }

        response.setContentType("text/html ; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        emptyPage(writer);
        writer.flush();
    }

    private void manageError(PrintWriter writer, String sHeight, String sWidth, String msg) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(sHeight);
        Objects.requireNonNull(sWidth);
        Objects.requireNonNull(msg);

        writer.format(this.page, sHeight, sWidth, msg);
    }

    private void computeResponse(PrintWriter writer, HttpServletRequest request) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(request);
        int height;
        int width;

        var sHeight = request.getParameter("height");
        var sWidth = request.getParameter("width");

        // Empty field
        if (sHeight.equals("") || sWidth.equals("")) {
            manageError(writer, sHeight, sWidth, "");
            return;
        }

        // Not numbers
        try {
            height = Integer.parseInt(sHeight);
            width = Integer.parseInt(sWidth);
        } catch(NumberFormatException e) {
            var msg = "Wrong format";
            manageError(writer, sHeight, sWidth, msg);
            return;
        }

        // Invalid numbers
        if ((height < 0) || (width < 0)) {
            var msg = "Fields must be positive";
            manageError(writer, sHeight, sWidth, msg);
            return;
        }

        // Correct entries
        finalPage(writer, height, width);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html ; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();

        computeResponse(writer, request);

        writer.flush();
    }
}
