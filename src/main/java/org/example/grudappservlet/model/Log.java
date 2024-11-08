package org.example.grudappservlet.model;

import java.util.Date;

public class Log {

  private int id;
  private Date logDate;
  private boolean completed;
  private Habit habit;
  private User user;

  public Log(int id, Date logDate, boolean completed, Habit habit, User user) {
    this.id = id;
    this.logDate = logDate;
    this.completed = completed;
    this.habit = habit;
    this.user = user;
  }

  public Log() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getLogDate() {
    return logDate;
  }

  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public void setHabit(Habit habit) {
    this.habit = habit;
  }

  public Habit getHabit() {
    return habit;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
