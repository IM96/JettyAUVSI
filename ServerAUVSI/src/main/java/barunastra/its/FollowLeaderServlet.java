package barunastra.its;

import org.eclipse.jetty.util.Jetty;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class FollowLeaderServlet extends HttpServlet {
    protected static final String[] CODES = new String[]{"12", "23" ,"34", "41"};
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqUrl = req.getRequestURI();
        PrintWriter out = resp.getWriter();
        int formatStatus = checkFormat(reqUrl);
        if(formatStatus == 0){
            Random rand = new Random();
            JSONObject obj = new JSONObject();
            int idx = rand.nextInt(4);
            obj.put("code",CODES[idx] );
            String msg = obj.toJSONString();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setHeader("Content-Type", "application/json");
            resp.setHeader("Content-Length", String.valueOf(msg.length()));
            resp.setHeader("Server",Jetty.VERSION);
            out.write(msg);
            System.out.println("Konesksi berhasil misi follow leader");
        }
        else if(formatStatus == 1){
            resp.setStatus((HttpServletResponse.SC_NOT_FOUND));
        }
        else{
            resp.setStatus((HttpServletResponse.SC_BAD_REQUEST));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.println("<h1>" + FollowLeaderServlet.class + "</h1>");
    }
    protected int checkFormat(String url){
        String reqUrl[] = url.split("/");
        if(reqUrl[1].equals("followLeader")  && !reqUrl[2].isEmpty() && !reqUrl[3].isEmpty() ){
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
