package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Files {
    private int fileid;
    private String filename;
    private String contenttype;
    private String filesize;
    private byte[] filedata;
}
