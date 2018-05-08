package barunastra.its;

import netscape.javascript.JSObject;
import org.eclipse.jetty.util.Jetty;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class RunServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<String> fields = resp.getHeaderNames();
        String headerMsg="";

        for(String headerName : fields){
            headerMsg+= "<h1>" + headerName +": " + resp.getHeader(headerName) + "</h1><br>";
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.println(headerMsg+"<h1>" + RunServlet.class + "</h1>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqUrl = req.getRequestURI();
        PrintWriter out = resp.getWriter();
        int formatStatus = checkFormat(reqUrl);

        if(formatStatus == 0){
            JSONObject obj = new JSONObject();
            obj.put("success","true" );
            String msg = obj.toJSONString();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setHeader("Content-Type", "application/json");
            resp.setHeader("Content-Length", String.valueOf(msg.length()));
            resp.setHeader("Server",Jetty.VERSION);
            out.write(msg);
            String urls[] = reqUrl.split("/");
            System.out.println("Konesksi berhasil misi " +urls[2] +" run");
        }
        else if(formatStatus == 1){
            resp.setStatus((HttpServletResponse.SC_NOT_FOUND));
        }
        else{
            resp.setStatus((HttpServletResponse.SC_BAD_REQUEST));
        }
    }
    protected int checkFormat(String url){
        String reqUrl[] = url.split("/");

        if(reqUrl[1].equals("run") && (reqUrl[2].equals("start") || reqUrl[2].equals("end") ) && !reqUrl[3].isEmpty() && !reqUrl[4].isEmpty() ){
            return 0;
        }
        else if(reqUrl[3].isEmpty() || reqUrl[4].isEmpty()){
            return 1;
        }
        else{
            return 2;
        }

    }
}
