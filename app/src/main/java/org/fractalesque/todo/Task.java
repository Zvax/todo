package org.fractalesque.todo;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String description;
    private int id;

    Task(String t, String d) {
        title = t;
        description = d;
    }
    Task(String t, String d, int i) {
        title = t;
        description = d;
        id = i;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
