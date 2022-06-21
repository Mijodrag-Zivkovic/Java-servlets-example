package rs.raf.demo;

import java.io.*;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "odabir", value = "/odabir")
public class Odabir extends HttpServlet {


    public Odabir() {

    }

    public void init() {
        if (Model.model.isEmpty()) {
            try {
                String path = getServletContext().getRealPath("/WEB-INF/dani");
                System.out.println(path);
                //File dir = new File("C:\\Users\\mijod\\Desktop\\fax\\web\\demo4\\src\\main\\java\\rs\\raf\\demo\\dani");
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
        if (Skladiste.skladiste.get(request.getSession().getId().toString()) != null) {
            out.println("<h2>Korisnik: " + request.getSession().getId() + "</h2>");
            out.println("<h2>Ponedeljak: " + Skladiste.skladiste.get(request.getSession().getId().toString()).getLista().get(0) + "</h2>");
            out.println("<h2>Utorak: " + Skladiste.skladiste.get(request.getSession().getId().toString()).getLista().get(1) + "</h2>");
            out.println("<h2>Sreda: " + Skladiste.skladiste.get(request.getSession().getId().toString()).getLista().get(2) + "</h2>");
            out.println("<h2>Cetvrtak: " + Skladiste.skladiste.get(request.getSession().getId().toString()).getLista().get(3) + "</h2>");
            out.println("<h2>Petak: " + Skladiste.skladiste.get(request.getSession().getId().toString()).getLista().get(4) + "</h2>");
        } else {
            String htmlResponse = "<form method=\"POST\" action=\"/odabir\">\n";
            for (int i = 0; i < Model.model.size(); i++) {
                Model m = Model.model.get(i);
                htmlResponse +=
                        "    <label for=\"" + m.getDay() + "\">" + m.getDay().substring(0, 1).toUpperCase() + m.getDay().substring(1) + ":</label>\n" +
                                "    <select name=\"" + m.getDay() + "\" id=\"" + m.getDay() + "\">\n";
                for (int j = 0; j < m.getMeals().size(); j++) {
                    htmlResponse += "        <option value=\"" + m.getMeals().get(j) + "\">" + m.getMeals().get(j) + "</option>\n";
                }
                htmlResponse += "    </select>\n" +
                        "    <br><br>\n";
            }
            htmlResponse += "    <input type=\"submit\" name=\"submit\" value=\"Potvrda\"/>\n" +
                    "</form>";
            out.println(htmlResponse);
//            out.println("<form method=\"POST\" action=\"/odabir\">\n" +
//                    "    <label for=\"ponedeljak\">Ponedeljak:</label>\n" +
//                    "    <select name=\"ponedeljak\" id=\"ponedeljak\">\n" +
//                    "        <option value=\"grasak 1\">Grasak 1</option>\n" +
//                    "        <option value=\"grasak 2\">Grasak 2</option>\n" +
//                    "        <option value=\"grasak 3\">Grasak 3</option>\n" +
//                    "    </select>\n" +
//                    "    <br><br>\n" +
//                    "    <input type=\"submit\" name=\"submit\" value=\"Potvrda\"/>\n" +
//                    "</form>");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Skladiste.skladiste.get(request.getSession().getId().toString()) == null)
        //if(request.getSession().getAttribute("ponedeljak") == null)
        {
            if (request.getParameter("ponedeljak") == null) {
                response.getOutputStream().println("Morate izabrati obrok");
                response.setStatus(403);
                return;
            }
            request.getSession().setAttribute("ponedeljak", request.getParameter("ponedeljak"));
            request.getSession().setAttribute("utorak", request.getParameter("utorak"));
            request.getSession().setAttribute("sreda", request.getParameter("sreda"));
            request.getSession().setAttribute("cetvrtak", request.getParameter("cetvrtak"));
            request.getSession().setAttribute("petak", request.getParameter("petak"));
            Skladiste s = new Skladiste();
            for (int i = 0; i < Model.model.size(); i++) {
                s.getLista().add(request.getParameter(Model.model.get(i).getDay()));
                int broj = Model.model.get(i).getMeals().indexOf(request.getParameter(Model.model.get(i).getDay()));
                Skladiste.brojac[i * 3 + broj]++;
            }
            Skladiste.skladiste.put(request.getSession().getId().toString(), s);
        }

        response.sendRedirect("/odabir");
    }

    public void destroy() {
        System.out.println("destroy method");
    }
}
