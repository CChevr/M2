package fr.uge.jee.servlet.rectangle;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@WebServlet("/rectangle")
public class RectangleServlet extends HttpServlet {
    private String page;

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        var lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines();
        return lines.collect(Collectors.joining("\n"));
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
        writer.println(this.page);
        writer.flush();
    }

    private long computeResponse(HttpServletRequest request) throws IOException {
        var body = request.getReader().readLine();
        var length = body.split("&");

        if (length.length != 2) {
            throw new IllegalArgumentException("Two length are necessaries");
        }

        return 0;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html ; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        var test = request.getReader();
        var res = test.readLine();
        writer.println("<!DOCTYPE html><html><p>"+res+"</p></html>");
        writer.flush();
    }
}
