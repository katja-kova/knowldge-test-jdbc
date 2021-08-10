package de.hda.fbi.db2.stud.entity;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "Player")
@NamedQuery(
    name = "Player.findByName",
    query = "SELECT p FROM Player p WHERE p.name = :name"
)
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;
  private String name;
  @OneToMany(mappedBy = "player")
  private List<Game> games;

  public Player() {
    this.name = "";
  }

  public Player(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Player)) {
      return false;
    }

    Player player = (Player) obj;
    return id == player.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
