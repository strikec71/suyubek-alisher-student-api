package kz.it2306.studentapi;
import jakarta.persistence.*;
@Entity
@Table(name="students", uniqueConstraints=@UniqueConstraint(name="uk_student_identifier", columnNames="student_identifier"))
public class Student {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private String surname;
    @Column(nullable=false) private String groupName;
    @Column(name="student_identifier",nullable=false) private String studentId;
    @Column(nullable=false) private String email;
    protected Student() {}
    public Student(StudentRequest r) { update(r); }
    public void update(StudentRequest r) {
        name=r.name().strip(); surname=r.surname().strip(); groupName=r.group().strip();
        studentId=r.studentId().strip(); email=r.email().strip();
    }
    public Long getId(){return id;}
    public StudentResponse response(){ return new StudentResponse(id,name,surname,groupName,studentId,email); }
}
