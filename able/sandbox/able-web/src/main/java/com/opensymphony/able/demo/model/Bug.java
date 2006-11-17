package com.opensymphony.able.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.SearchableMetaData;
import com.opensymphony.able.annotations.DisplayList;
import com.opensymphony.able.annotations.DisplayEdit;
import com.opensymphony.able.annotations.Input;
import com.opensymphony.able.annotations.InputType;

/**
 * Represents a bug in the bug database.
 *
 * @author Tim Fennell
 */
@Entity
@DisplayList(excludes = {"longDescription", "attachments"})
@DisplayEdit(excludes = {"id", "attachments"})
@Searchable
public class Bug {
    @Id
    @GeneratedValue
    @Input(label = "ID")
    @SearchableId
    private Integer id;
    private Date openDate;
    @SearchableProperty
    @SearchableMetaData(name="shortDescription")
    @Input(label = "Description")
    private String shortDescription;
    @Input(type = InputType.TextArea)
    private String longDescription;
    @ManyToOne
    private Component component;
    private Priority priority;
    @Input(type = InputType.Radio)
    private Status status;
    @ManyToOne
    private Person owner;
    private Date dueDate;
    private Float percentComplete;
    @OneToMany
    private List<Attachment> attachments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Float getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Float percentComplete) {
        this.percentComplete = percentComplete;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<Attachment>();
        }

        this.attachments.add(attachment);
    }
}
