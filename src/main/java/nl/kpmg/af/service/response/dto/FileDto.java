/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.response.dto;

import java.util.Date;

/**
 * @author Hoekstra.Maarten
 */
public class FileDto {
    private String filename;
    private Long size;
    private String md5;
    private Date create;

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(final Long size) {
        this.size = size;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(final String md5) {
        this.md5 = md5;
    }

    public Date getCreate() {
        return this.create;
    }

    public void setCreate(final Date create) {
        this.create = create;
    }
}
