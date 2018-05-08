package barunastra.its;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.servlet.http.Part;

@WebServlet("/docking/*")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)   // 50MB
public class DockingServlet extends HttpServlet {
    private static final String SAVE_DIR = "E:/UploadFiles";



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*

        req.setAttribute("message", "Upload has been done successfully!");
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                req, resp);

        */

        int formatStatus = checkFormat(req.getRequestURI());
        if(formatStatus == 0){
            saveImage(req);
            System.out.println("Konesksi berhasil misi docking upload");
            resp.setStatus(HttpServletResponse.SC_OK);
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

        if(reqUrl[1].equals("docking") && reqUrl[2].equals("image") && !reqUrl[3].isEmpty() && !reqUrl[4].isEmpty() ){
            return 0;
        }
        else if(reqUrl[3].isEmpty() || reqUrl[4].isEmpty()){
            return 1;
        }
        else{
            return 2;
        }

    }
    private void saveImage(HttpServletRequest req) throws IOException, ServletException {
        // creates the save directory if it does not exists
        File fileSaveDir = new File(SAVE_DIR);
        if (!fileSaveDir.exists()) {
            System.out.println(fileSaveDir.getAbsolutePath());
            fileSaveDir.mkdir();
        }

        for (Part part : req.getParts()) {
            String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            System.out.println(part.getSize());
            System.out.println(part.toString());
            part.write("/"+ fileName);
        }
    }
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}
