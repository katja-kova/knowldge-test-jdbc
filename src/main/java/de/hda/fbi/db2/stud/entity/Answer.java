package de.hda.fbi.db2.stud.entity;

import javax.persistence.*;

@Entity
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;
  private String answer;
  @ManyToOne
  private Question question;
  @ManyToOne
  private Game game;

  public Answer() {
  }

  public Answer(Question question, Game game) {
    this.answer = null;
    this.question = question;
    this.game = game;
  }

  public Answer(String answer, Question question, Game game) {
    this.answer = answer;
    this.question = question;
    this.game = game;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public Question getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }

}
