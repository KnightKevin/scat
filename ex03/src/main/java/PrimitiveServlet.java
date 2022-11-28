import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveServlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        System.out.println("from service");
        PrintWriter out = response.getWriter();
        String s = "Hello. Roses are red.\r\n"+"Violets are blue.";
        System.out.print(s);
        out.println("HTTP/1.1 200 ok\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: "+s.length()+"\r\n");
        out.println(s);
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }

    @Override
    public String getServletInfo() {
        return null;
    }
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

}

