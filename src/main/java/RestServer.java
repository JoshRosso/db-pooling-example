import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestServer {

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final Logger logger = LogManager.getLogger(RestServer.class.getName());

    public static void main(String[] args) throws Exception {

        registerJDBCDriver();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server server = new Server(8001);
        server.setHandler(context);
        server.setThreadPool(new ExecutorThreadPool(100, 1000, 30000));

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                StudentService.class.getCanonicalName());

        try { server.start(); server.join(); }
        finally { server.destroy(); }
    }

    private static void registerJDBCDriver() {
        try {
            Class.forName(MYSQL_DRIVER);
        } catch(ClassNotFoundException ex) {
            logger.error("Could not load driver. Exception: " + ex.getMessage());
        }
    }

}