package de.hda.fbi.db2.stud.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;
  @ManyToOne
  private Player player;
  @Temporal(TemporalType.DATE)
  private Date startDate;
  @Temporal(TemporalType.DATE)
  private Date endDate;
  @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
  private List<Answer> answers;
  @Transient
  private EntityManager em;

  public Game() {
  }

  public Game(Player player, List<Answer> answers) {
    this.player = player;
    this.answers = answers;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public Player getPlayer() {
    return player;
  }

  public void setStart(Date start) {
    this.startDate = (Date) start.clone();
  }

  public Date getEnd(Date end) {
    return (Date) end.clone();
  }

  public void setEnd(Date end) {
    this.endDate = (Date) end.clone();
  }

  public EntityManager getEm() {
    return em;
  }

  public void setEm(EntityManager em) {
    this.em = em;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Game)) {
      return false;
    }

    Game game = (Game) obj;
    return id == game.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
