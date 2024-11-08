package org.example.grudappservlet.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Habit {
  private int id;
  private String name;
  private String description;
  private String frequency;
  private List<Log> logs;
  private User user;
  private Date creationDate;

  public Habit(int id, String name, String description, String frequency, User user,
      Date creationDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.frequency = frequency;
    this.logs = new ArrayList<>();
    this.user = user;
    this.creationDate = new Date();
  }

  public Habit(int habitId) {
    this.id = habitId;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public String getFrequency() {
    return frequency;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }
  public List<Log> getLogs() {
    return logs;
  }
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public boolean isCompleted() {
    Date today = new Date();
    int daysSinceLastLog = 0;
    for (Log log : logs) {
      if (frequency.equals("daily")) {
        daysSinceLastLog = (int) ((today.getTime() - log.getLogDate().getTime()) / (1000 * 60 * 60
            * 24));
        if (daysSinceLastLog <= 1) {
          return true;
        }
      } else if (frequency.equals("weekly")) {
        daysSinceLastLog = (int) ((today.getTime() - log.getLogDate().getTime()) / (1000 * 60 * 60
            * 24 * 7));
        if (daysSinceLastLog <= 1) {
          return true;
        }
      }
    }
    return false;
  }
}
