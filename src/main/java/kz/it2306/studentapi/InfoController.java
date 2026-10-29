package kz.it2306.studentapi;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
@RestController
public class InfoController {
    @Value("${app.version}") private String version;
    @GetMapping("/info") public Map<String,String> info(){
        return Map.of("name","Alisher","surname","Suyubek","group","IT2-2306","studentId","37773","version",version);
    }
    @GetMapping(value="/",produces="text/html") public String home(){
        return "<html><head><title>Suyubek Alisher Student API</title></head><body style='font:20px Georgia;max-width:850px;margin:70px auto'>"
            +"<h1>Suyubek Alisher Student API</h1><p>Group IT2-2306 | Student ID 37773</p><p>Spring Boot CRUD API and PostgreSQL</p>"
            +"<p>Version: "+version+"</p><p><a href='/info'>Project identity</a> | <a href='/students'>Students</a> | <a href='/actuator/health'>Health</a></p></body></html>";
    }
}
