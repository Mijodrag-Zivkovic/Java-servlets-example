package rs.raf.demo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "pregled", value = "/pregled")
public class Pregled extends HttpServlet {


    public Pregled() {

    }

    String password;

    public void init() {
        if (Model.model.isEmpty()) {
            try {
                String path = getServletContext().getRealPath("/WEB-INF/dani");
                File dir = new File(path);
                File[] myObj = dir.listFiles();
                for (int i = 0; i < myObj.length; i++) {
                    Scanner myReader = new Scanner(myObj[i]);
                    //Model model = new Model(myObj[i].getName().replace(".txt",""));
                    Model model = new Model(myObj[i].getName().substring(1, myObj[i].getName().indexOf(".")));
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        model.getMeals().add(data);
                        //System.out.println(data);
                    }
                    Model.model.add(model);
                    myReader.close();
                }

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        System.out.println("pregled init");
        try {
            String path = getServletContext().getRealPath("/WEB-INF/lozinka.txt");
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.password = data;
                System.out.println(data);
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("service method");
        super.service(req, resp);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        System.out.println("unutra");
        System.out.println(request.getParameter("lozinka"));
        String parametar = request.getParameter("lozinka");
        System.out.println(parametar);
        System.out.println("nesto");
        if (parametar == null) {
            System.out.println("null");
            out.println("Unesite lozinku u URL-u!");
        } else if (request.getParameter("lozinka").equals(password)) {
            String htmlResponse = "<h1>Pregled</h1>\n";
            for (int i = 0; i < Model.model.size(); i++) {
                htmlResponse += "<h2>" + Model.model.get(i).getDay() + "</h2>\n";
                for (int j = 0; j < Model.model.get(i).getMeals().size(); j++) {
                    htmlResponse += Model.model.get(i).getMeals().get(j) + " " + Skladiste.brojac[i * 3 + j]; //
                    htmlResponse += "<br>";
                }
                //System.out.println("kraj 1");
                htmlResponse += "<br>";
            }
            //System.out.println("kraj");
            htmlResponse += "<form method=\"POST\" action=\"/pregled\">\n" +
                    "    <input type=\"submit\" name=\"submit\" value=\"Reset\"/>\n" +
                    "</form>";
            out.println(htmlResponse);
        } else {
            out.println("Unesite lozinku u URL-u!");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Skladiste.skladiste.clear();
        for (int i = 0; i < 15; i++) {
            Skladiste.brojac[i] = 0;
        }
        response.sendRedirect("/pregled?lozinka=123");
    }

    public void destroy() {
        System.out.println("destroy method");
    }

}