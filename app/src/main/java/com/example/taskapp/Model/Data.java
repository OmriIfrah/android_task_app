package com.example.taskapp.Model;

public class Data {
    private String title;
    private String note;
    private String date;
    private String id;
    private String urgency;

    public Data()
    {

    }

    public Data(String title, String note, String date, String id, String urgency) {
        this.title = title;
        this.note = note;
        this.date = date;
        this.id = id;
        this.urgency = urgency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }
}
