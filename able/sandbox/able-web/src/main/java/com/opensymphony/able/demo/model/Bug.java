package com.opensymphony.able.demo.model;

import com.opensymphony.able.view.DisplayEdit;
import com.opensymphony.able.view.DisplayList;
import com.opensymphony.able.view.Input;
import com.opensymphony.able.view.InputType;

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
@DisplayList(excludes = {"longDescription", "attachments"})
@DisplayEdit(excludes = {"id", "attachments"})
public class Bug {
    @Id
    @GeneratedValue
    @Input(label = "ID")
    private Integer id;
    private Date openDate;
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
