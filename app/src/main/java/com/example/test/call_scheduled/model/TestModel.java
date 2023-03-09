package com.example.test.call_scheduled.model;

public class TestModel {

    private String queue;
    private String name;
    private String date;
    private String time;

    public TestModel(String queue, String name, String date, String time) {
        this.queue = queue;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
