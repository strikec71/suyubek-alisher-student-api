package kz.it2306.studentapi;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApiTests {
    @Autowired TestRestTemplate http;
    @Autowired StudentRepository repository;
    @BeforeEach void clean(){repository.deleteAll();}
    Map<String,String> data(){return new HashMap<>(Map.of("name","Alisher","surname","Suyubek","group","IT2-2306","studentId","37773","email","alisher@example.test"));}
    ResponseEntity<Map> create(Map<String,String> body){return http.postForEntity("/students",body,Map.class);}
    @Test void ownerIdentity(){
        Map info=http.getForObject("/info",Map.class);
        assertThat(info.get("studentId")).isEqualTo("37773");
        assertThat(info.get("surname")).isEqualTo("Suyubek");
    }
    @Test void health(){assertThat(http.getForEntity("/actuator/health",String.class).getStatusCode()).isEqualTo(HttpStatus.OK);}
    @Test void createsStudent(){var r=create(data());assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);assertThat(r.getHeaders().getLocation()).isNotNull();assertThat(r.getBody().get("name")).isEqualTo("Alisher");}
    @Test void listsStudents(){create(data());assertThat(http.getForObject("/students",List.class)).hasSize(1);}
    @Test void getsOne(){var r=create(data());assertThat(http.getForObject(r.getHeaders().getLocation(),Map.class).get("studentId")).isEqualTo("37773");}
    @Test void updatesStudent(){var r=create(data());var changed=data();changed.put("group","IT2-2306-UPDATED");var u=http.exchange(r.getHeaders().getLocation(),HttpMethod.PUT,new HttpEntity<>(changed),Map.class);assertThat(u.getStatusCode()).isEqualTo(HttpStatus.OK);assertThat(u.getBody().get("group")).isEqualTo("IT2-2306-UPDATED");}
    @Test void deletesStudent(){var r=create(data());var d=http.exchange(r.getHeaders().getLocation(),HttpMethod.DELETE,HttpEntity.EMPTY,Void.class);assertThat(d.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);assertThat(repository.count()).isZero();}
    @Test void absentStudent(){assertThat(http.getForEntity("/students/999999",String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);}
    @Test void cannotUpdateAbsentStudent(){assertThat(http.exchange("/students/999999",HttpMethod.PUT,new HttpEntity<>(data()),String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);}
    @Test void cannotDeleteAbsentStudent(){assertThat(http.exchange("/students/999999",HttpMethod.DELETE,HttpEntity.EMPTY,String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);}
    @Test void rejectsBlankName(){var d=data();d.put("name","");assertThat(create(d).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);}
    @Test void rejectsInvalidEmail(){var d=data();d.put("email","wrong");assertThat(create(d).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);}
    @Test void rejectsInvalidIdentifier(){var d=data();d.put("studentId","not-numeric");assertThat(create(d).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);}
    @Test void preservesLeadingZeros(){var d=data();d.put("studentId","0037773");assertThat(create(d).getBody().get("studentId")).isEqualTo("0037773");}
    @Test void rejectsDuplicateIdentifier(){create(data());assertThat(create(data()).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);}
    @Test void rejectsMissingBody(){assertThat(http.postForEntity("/students",null,String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);}
}
