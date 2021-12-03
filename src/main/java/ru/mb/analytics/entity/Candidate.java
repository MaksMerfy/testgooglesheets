package ru.mb.analytics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Candidate {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String mail;
    private String phone;
    private Gender gender;
    private LocalDate dateBirth;
    private String urlPhoto;
    private String city;
    private String education;
    private String workExperience;
    private Boolean mailingIsTrue;
    private Vacancy vacancy;
    private Utm utm;
    private LocalDateTime createdDate;
}
