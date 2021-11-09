package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notes {
    private int noteid;
    private String notetitle;
    private String notedescription;
}
