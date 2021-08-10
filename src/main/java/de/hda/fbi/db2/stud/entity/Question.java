package de.hda.fbi.db2.stud.entity;

import com.google.common.base.Objects;
import java.util.Map;
import javax.persistence.*;

@Entity
public class Question {
  @Id
  private int id;
  private String question;
  @ElementCollection
  private Map<String, Boolean> options;
  @ManyToOne
  private Category category;

  /**
   * constructor of a question object.
   *
   * @param id is the id of the question.
   * @param question is the string asked as question.
   * @param options are the options available for the user to select the answer from.
   *                The correct option is mapped to true.
   * @param category is the category of the question.
   */
  public Question(int id, String question, Map<String, Boolean> options, Category category) {
    this.id = id;
    this.question = question;
    this.options = options;
    this.category = category;
  }

  public Question() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public Map<String, Boolean> getOptions() {
    return options;
  }

  public void setOptions(Map<String, Boolean> options) {
    this.options = options;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Question question = (Question) o;
    return getId() == question.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
