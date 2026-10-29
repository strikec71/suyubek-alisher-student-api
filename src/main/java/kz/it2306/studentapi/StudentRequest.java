package kz.it2306.studentapi;
import jakarta.validation.constraints.*;
public record StudentRequest(
    @NotBlank @Size(max=100) String name,
    @NotBlank @Size(max=100) String surname,
    @NotBlank @Size(max=50) String group,
    @NotBlank @Pattern(regexp="[0-9]{1,20}") String studentId,
    @NotBlank @Email @Size(max=200) String email) {}
