package com.joshrosso;

import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/students")
public class StudentService {

    private static final Logger logger = LogManager.getLogger(StudentService.class.getName());
    private static final Gson mapper = new Gson();
    private static final String RUN_ID = "static";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStudents() throws Exception {
        String corrId = UUID.randomUUID().toString();
        logger.trace(String.format("%d %s %s %s", System.currentTimeMillis(), "start_retrieveData", corrId,  RUN_ID));
        List<Map<String,Object>> students = DataRetriever.retrieveData("SELECT * FROM university.students;");
        logger.trace(String.format("%d %s %s %s", System.currentTimeMillis(), "end_retrieveData", corrId,  RUN_ID));
        String response = mapper.toJson(students);
        return Response.status(200).entity(response).build();
    }

}