package ru.mb.analytics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Vacancy {
    private Long id;
    private String title;
    private Position position;
    private ProfessionalArea professionalArea;
    private String url;
    private LocalDateTime date;
    private Rate rate;
    private Account account;
    private Long hhId;
    private Boolean isActive;
    private Long totalAmount;
    private Long amountOfNoSent;
    private List<Candidate> candidateList;
}