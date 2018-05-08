package barunastra.its;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.MultipartConfigElement;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Hello world!
 *
 */
public class ServerTest {
    public static void main( String[] args ) throws Exception {
        // get ip
        InetAddress ip = InetAddress.getByName("DESKTOP-R60E10L");
        System.out.println("Host IP: " + ip.getHostAddress()+"\nPort: 8080");

        // bind server
        InetSocketAddress address = new InetSocketAddress(ip, 8080);
        Server server = new Server(address);

        //Server server = new Server(8080);

        // create handler
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(System.getProperty("java.io.tmpdir"));
        server.setHandler(context);

        // Add run servlet
        context.addServlet(RunServlet.class, "/run/*");

        // Add hearbeat servlet
        context.addServlet(HeartbeatServlet.class,"/heartbeat/*");

        // Add follow leader
        context.addServlet(FollowLeaderServlet.class,"/followLeader/*");

        // Add automated dock
        ServletHolder dockingHolder = new ServletHolder(new DockingServlet());
        dockingHolder.getRegistration().setMultipartConfig(//
                new MultipartConfigElement("E:/UploadFiles",
                        1024*1024*5, // 2MB
                        1024*1024*50,      // 10MB
                        1024*1024*50));

        context.addServlet(dockingHolder, "/docking/*");

        // Add default servlet
        context.addServlet(DefaultServlet.class, "/");



        // start server
        server.start();
        server.join();

    }
    private static void enumNetworkInterface() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            // drop inactive
            if (!networkInterface.isUp())
                continue;

            // smth we can explore
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                System.out.println(String.format("NetInterface: name [%s], ip [%s]",
                        networkInterface.getDisplayName(), addr.getHostAddress()));
            }
        }

    }
}
