package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab04MassData;
import de.hda.fbi.db2.controller.Controller;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.EntityManager;

public class Lab04MassDataImpl extends Lab04MassData implements Runnable {
  private static Lab04MassDataImpl inst;

  public static synchronized Lab04MassDataImpl getInstance() {
    if (Lab04MassDataImpl.inst == null) {
      Lab04MassDataImpl.inst = (Lab04MassDataImpl) Controller.getInstance().getLab04MassData();
    }
    return Lab04MassDataImpl.inst;
  }

  @Override
  public void run() {
    Player p = (Player) this.lab03Game.getOrCreatePlayer(
        "Player_" + Thread.currentThread().getId());
    Lab04MassDataImpl thisObj = (Lab04MassDataImpl) Controller.getInstance().getLab04MassData();
    EntityManager em = this.lab02EntityManager.getEntityManager();
    em.getTransaction().begin();
    em.persist(p);
    for (int j = 0; j < 100; j++) {
      List<Category> cats = new LinkedList<>();
      //get 2 random Categories
      int numberOfCategories = thisObj.lab01Data.getCategories().size();
      int index1 = ThreadLocalRandom.current().nextInt(numberOfCategories);
      cats.add((Category) thisObj.lab01Data.getCategories().get(index1));
      int index2 = ThreadLocalRandom.current().nextInt(numberOfCategories);
      cats.add((Category) thisObj.lab01Data.getCategories().get(index2));
      //generate the game
      Game g = (Game) thisObj.lab03Game.createGame(p, thisObj.lab03Game.getQuestions(cats,2));
      g.setEm(em);
      //play the game
      thisObj.lab03Game.playGame(g);
      //Edit times for future reasons
      Date now = new Date();
      long offset = ThreadLocalRandom.current().nextLong(1209600000);
      long start = now.getTime() - offset;
      g.setStart(new Date(start));
      g.setEnd(new Date(start + 60000));
      //save the game in
      thisObj.lab03Game.persistGame(g);
      //good place for flush if it would work.
      //A game has been played and it would be possible to send it to the Database here.
      //em.flush();
    }
    em.getTransaction().commit();
    em.clear();
    em.close();
  }

  @Override
  public void createMassData() {

    System.out.println("START TIMESTAMP: " + new Timestamp(System.currentTimeMillis()));
    if (this.lab01Data.getQuestions().size() != 200) {
      System.out.println("missing Questions!");
    }
    List<Thread> threadList = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      //create players and let them play simultaneously
      Thread t = new Thread(Lab04MassDataImpl.getInstance());
      threadList.add(t);
      t.start();
    }
    for (Thread t: threadList) {
      try {
        t.join();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    }
    System.out.println("END TIMESTAMP: " + new Timestamp(System.currentTimeMillis()));
    long countPlayers = (Long) this.lab02EntityManager.getEntityManager().createQuery(
        "SELECT count(p.id) FROM Player p").getSingleResult();
    long countGames = (Long) this.lab02EntityManager.getEntityManager().createQuery(
        "SELECT count(g.id) FROM Game g").getSingleResult();
    System.out.println(countPlayers + " Players played a total of " + countGames + " Games.");
  }
}
