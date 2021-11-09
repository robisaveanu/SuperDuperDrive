package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Credentials {
    private int credentialid;
    private String url;
    private String username;
    private String key;
    private String password;
}
