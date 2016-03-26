import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/students")
public class StudentService {

    private static final Logger logger = LogManager.getLogger(StudentService.class.getName());
    private static final Gson mapper = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStudents() throws Exception {
        logger.info("LOGGING DATA: " + System.currentTimeMillis());
        List<Map<String,Object>> students = DataRetriever.retrieveData("SELECT * FROM university.students;");
        String response = mapper.toJson(students);
        return Response.status(200).entity(response).build();
    }

}