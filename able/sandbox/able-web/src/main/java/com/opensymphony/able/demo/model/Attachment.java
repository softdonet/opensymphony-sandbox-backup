package com.opensymphony.able.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * Very simple wrapper for file attachments uploaded for bugs.  Assumes that the attachment
 * contains some type of textual data.
 *
 * @author Tim Fennell
 */
@Entity
public class Attachment {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @Lob
    private long size;
    private String data;
    private String contentType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    @Transient
    public String getPreview() {
        int endIndex = Math.min(data.length(), 30);
        return data.substring(0, endIndex);
    }
}
