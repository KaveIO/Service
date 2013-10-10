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
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }
}
