package ru.mb.analytics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Proxy {
    private String ip;
    private String port;
    private String login;
    private String password;
}
