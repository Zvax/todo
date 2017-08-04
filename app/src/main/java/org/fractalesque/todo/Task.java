package org.fractalesque.todo;

public class Task {
    private String title;
    private String description;
    Task(String t, String d) {
        this.title = t;
        this.description = d;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
