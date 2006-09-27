package com.opensymphony.able.model;

import com.opensymphony.able.view.DisplayEdit;
import com.opensymphony.able.view.DisplayList;
import com.opensymphony.able.view.Input;
import com.opensymphony.able.view.InputType;
import com.opensymphony.able.view.Label;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a bug in the bug database.
 * 
 * @author Tim Fennell
 */
@Entity
@DisplayList(excludes = { "longDescription", "attachments" })
@DisplayEdit(excludes = { "id", "attachments" })
public class Bug {
    private Integer id;
    private Date openDate;
    private String shortDescription;
    private String longDescription;
    private Component component;
    private Priority priority;
    private Status status;
    private Person owner;
    private Date dueDate;
    private Float percentComplete;
    private List<Attachment> attachments;

    @Id
    @GeneratedValue
    @Label("ID")
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

    @Label("Description")
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Input(type=InputType.TextArea)
    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @ManyToOne
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

    @Input(type=InputType.Radio)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
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

    @OneToMany
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
