package de.hda.fbi.db2.controller;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.text.html.HTMLDocument;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * MenuController Created by l.koehler on 05.08.2019.
 */
public class MenuController {

  private Controller controller;

  public MenuController(Controller controller) {
    this.controller = controller;
  }

  /**
   * shows the menu.
   */
  public void showMenu() {
    do {
      System.out.println("Choose your Destiny?");
      System.out.println("--------------------------------------");
      System.out.println("1: Re-read csv");
      System.out.println("2: Play test");
      System.out.println("3: Create mass data");
      System.out.println("4: Analyze data");
      System.out.println("0: Quit");
    } while (readInput());
  }

  private boolean readInput() {
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String input = reader.readLine();
      if (input == null) {
        return true;
      }
      switch (input) {
        case "0":
          return false;
        case "1":
          readCsv();
          break;
        case "2":
          playTest();
          break;
        case "3":
          createMassData();
          break;
        case "4":
          analyzeData();
          break;
        default:
          System.out.println("Input Error");
          break;
      }

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void analyzeData() {
    try {
      EntityManager em = controller.getLab02EntityManager().getEntityManager();

      Query q = em.createQuery("SELECT DISTINCT p.name from Game g join g.player p "
          + "WHERE g.startDate > :startDate AND g.startDate < :endDate");
      Date now = new Date();
      q.setParameter("startDate", new Date(now.getTime() - 302400000)); // last 7 days
      q.setParameter("endDate", now);
      List resultList = q.getResultList();
      System.out.println(resultList);

      q = em.createQuery("SELECT g.id, g.startDate, "
          + "sum(CASE WHEN value(o) = true THEN 1 ELSE 0 END), count(a) "
          + "FROM Game g, Player p, Answer a, Question q join q.options o "
          + "WHERE p = g.player AND a.game = g AND a.question = q AND "
          + "key(o) = a.answer AND p.name = :name GROUP BY g");
      q.setParameter("name", "Player_1234");
      resultList = q.getResultList();
      for (Object o : resultList) {
        Object[] element = (Object[]) o;
        System.out.println("ID: " + element[0] + ", StartDate: " + element[1]
            + ", Correct: " + element[2] + ", ttl: " + element[3]);
      }

      q = em.createQuery("SELECT p.name, count(g) FROM Player p join p.games g "
          + "GROUP BY p.name ORDER BY count(g.id) DESC");
      resultList = q.getResultList();
      for (Object o : resultList) {
        Object[] element = (Object[]) o;
        System.out.println("Name: " + element[0] + ", #Games: " + element[1]);
      }

      q = em.createQuery("SELECT c.name, count(g) "
          + "FROM Answer a join a.game g join a.question q join q.category c "
          + "GROUP BY c.name ORDER BY count(g.id) DESC");
      resultList = q.getResultList();
      for (Object o : resultList) {
        Object[] element = (Object[]) o;
        System.out.println("Category: " + element[0] + ", #Games: " + element[1]);
      }

      em.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private void createMassData() {
    controller.createMassData();
  }

  private void playTest() {
    Lab03Game gameController = controller.getLab03Game();
    Object player = gameController.interactiveGetOrCreatePlayer();
    List<?> questions = gameController.interactiveGetQuestions();
    Object game = gameController.createGame(player, questions);
    gameController.interactivePlayGame(game);
    gameController.persistGame(game);
  }

  private void readCsv() {
    controller.readCsv();
  }
}
