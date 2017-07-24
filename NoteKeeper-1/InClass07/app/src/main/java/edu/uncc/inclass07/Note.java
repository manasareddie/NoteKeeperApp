package edu.uncc.inclass07;

import java.util.Date;

public class Note {
    private long id;
    private String subject;
    private String priority;
    private Date createdOn;
    private boolean completed;

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", priority='" + priority + '\'' +
                ", createdOn=" + createdOn +
                ", completed=" + completed +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
