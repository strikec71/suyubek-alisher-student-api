package kz.it2306.studentapi;
import java.net.URI;
import java.util.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository repository;
    public StudentController(StudentRepository repository){this.repository=repository;}
    private Student find(Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));}
    @PostMapping @Transactional
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request){
        Student s=repository.saveAndFlush(new Student(request));
        return ResponseEntity.created(URI.create("/students/"+s.getId())).body(s.response());
    }
    @GetMapping
    public List<StudentResponse> all(){return repository.findAll().stream().map(Student::response).toList();}
    @GetMapping("/{id}") public StudentResponse one(@PathVariable Long id){return find(id).response();}
    @PutMapping("/{id}") @Transactional
    public StudentResponse update(@PathVariable Long id,@Valid @RequestBody StudentRequest request){
        Student s=find(id);s.update(request);repository.flush();return s.response();
    }
    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id){repository.delete(find(id));return ResponseEntity.noContent().build();}
}
