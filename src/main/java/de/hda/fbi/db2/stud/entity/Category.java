package de.hda.fbi.db2.stud.entity;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@NamedQuery(
    name = "Category.findAll",
    query = "SELECT c FROM Category c"
)
public class Category implements Comparable<Category> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;
  @Column(unique = true)
  private String name;
  @OneToMany(mappedBy = "category")
  private List<Question> questions;

  public Category() {
    name = "";
    questions = new ArrayList<>();
  }

  /**
   * constructor for a category object.
   *
   * @param name is the name of the category.
   */
  public Category(String name) {
    this.name = name;
    questions = new ArrayList<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return Objects.equal(id, category.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  /**
   * constructor for a category object.
   *
   * @param name is the name of the category.
   * @param questions is a list of Questions that will be present in this category.
   */
  public Category(String name, List<Question> questions) {
    this.name = name;
    this.questions = questions;
  }

  @Override
  public int compareTo(Category c) {
    return this.id.compareTo(c.id);
  }

  public void addQuestion(Question q) {
    this.questions.add(q);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }
}
