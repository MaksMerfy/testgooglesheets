package ru.mb.analytics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Account {
    private Long id;
    private String brand;
    private Thematic thematic;
    private String site;
    private String legalEntity;
    private String phone;
    private String mail;
    private String adress;
    private String personManager;
    private String numberPhonePersonManager;
    private LocalDate dateRegistration;
    private String login;
    private String password;
    private String cookie;
    private String fingerprint;
    private Proxy proxy;//this is ip-adress
    private Boolean isActive;
    private List<Vacancy> vacancyList;
}