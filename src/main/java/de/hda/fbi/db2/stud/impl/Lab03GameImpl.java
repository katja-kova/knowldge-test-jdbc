package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.*;

public class Lab03GameImpl extends Lab03Game {
  /**
   * Creates a new Player or retrieves it from the database.
   * <p>
   * This function shall try to retrieve the player with the given name playerName from the
   * database. If no such player exists, it shall be created as a Java Object. It is not necessary
   * to persist the Player yet.
   * </p>
   *
   * <p>This function is primarily used for testing. There exists a version with user interaction
   * which shall be used from the menu
   * </p>
   *
   * @param playerName The name for the new Player.
   * @return Player object which was created or retrieved.
   * @see Lab03Game#interactiveGetOrCreatePlayer()
   */
  public Object getOrCreatePlayer(String playerName) {
    EntityManager em = this.lab02EntityManager.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    Player p = new Player(playerName);
    try {
      tx.begin();
      Query query = em.createNamedQuery("Player.findByName")
          .setParameter("name", playerName);
      p = (Player) query.getSingleResult();
    } catch (NoResultException e) {
      //no player found; use the newly created new one
    }
    em.close();
    return p;
  }

  /**
   * Creates a new Player or retrieves it from the database (interactive version).
   *
   * <p>
   * This function shall ask the user for a player name, and then shall try to retrieve such a
   * player from the database. If no such player exists, it shall be created as a Java Object. It is
   * not necessary to persist the player yet.
   * </p>
   *
   * <p>This function is primarily used for user interaction. There exists a version used for
   * testing, @see getOrCreatePlayer</p>
   *
   * @return Player object which was created or retrieved.
   * @see Lab03Game#getOrCreatePlayer(String)
   */
  @Override
  public Object interactiveGetOrCreatePlayer() {
    Scanner scan = new Scanner(System.in, "UTF-8");
    String name;
    System.out.println("Please enter your name: ");
    name = scan.next();

    return getOrCreatePlayer(name);
  }

  /**
   * This function shall generate a random list of questions which are from the given categories.
   *
   * <p>Per category there shall be a certain amount of questions chosen. If a category hosts less
   * questions than that amount, then all of the questions shall be chosen. Questions shall be
   * randomly selected.
   * </p>
   *
   * <p>There is also an interactive version of this function which asks the user for categories
   * instead of randomly selecting them.</p>
   *
   * @param categories                   A list of categories to select questions from
   * @param amountOfQuestionsForCategory The amount of questions per category. If a category has
   *                                     less than this amount, then all questions of that category
   *                                     shall be selected.
   * @return A List of randomly chosen Questions from the given Categories.
   * @see Lab03Game#interactiveGetQuestions()
   */
  @Override
  public List<?> getQuestions(List<?> categories, int amountOfQuestionsForCategory) {
    List<Question> questionList = new ArrayList<>();
    for (Object o: categories) {
      Category c = (Category) o;

      if (c.getQuestions().size() < amountOfQuestionsForCategory) {
        questionList.addAll(c.getQuestions());
      } else {
        HashSet<Integer> set = new HashSet<Integer>();
        while (set.size() < amountOfQuestionsForCategory) {
          set.add(ThreadLocalRandom.current().nextInt(c.getQuestions().size()));
        }
        for (Integer i :set) {
          questionList.add(c.getQuestions().get(i));
        }

        /*
        Random r = new Random();
        for (int i = 0; i < amountOfQuestionsForCategory; i++) {
          int index = r.nextInt(c.getQuestions().size())-1;
          while (questionList.contains(c.getQuestions().get(index))) {
            index = (index + 1) % c.getQuestions().size();
          }
          questionList.add(c.getQuestions().get(index));
        }// */
      }
    }

    return questionList;
  }

  /**
   * This function shall generate a random list of questions after asking the user for categories.
   *
   * <p>In this function, ask the user for categories and the number of questions per category.
   * Then, randomly select questions from those categories. Choose as many questions per category as
   * were entered, as long as the category has that many questions. If there are fewer questions,
   * then select all of them.</p>
   *
   * @return A List of randomly chosen Questions from categories which the user entered.
   * @see Lab03Game#getQuestions(List, int)
   */
  @Override
  public List<?> interactiveGetQuestions() {
    Scanner scan = new Scanner(System.in, "UTF-8");

    EntityManager em = this.lab02EntityManager.getEntityManager();
    List<Category> categories = em.createNamedQuery("Category.findAll").getResultList();

    List<Category> chosenCategories = new ArrayList<>();
    System.out.println("Select category names from the list: ");

    for (int i = 0; i < categories.size(); i++) {
      System.out.println(i + ": " + categories.get(i).getName());
    }

    do {
      String input = scan.nextLine().trim();

      if (input.isEmpty()) {
        if (chosenCategories.size() < 2) {
          System.out.println("Please select at least 2 categories.");
          continue;
        }
        break;
      }
      for (Category c : categories) {
        if (c.getName().equals(input)) {
          chosenCategories.add(c);
        }
      }
    } while (true);

    int num = 0;
    System.out.println("Number of questions: ");

    do {
      String input = scan.nextLine().trim();

      try {
        num = Integer.parseInt(input);
        if (num < 0) {
          System.out.println("The number cannot be less than 1.");
          continue;
        }
        break;
      } catch (NumberFormatException e) {
        System.out.println("Please enter a number");
      }
    } while (true);

    return this.getQuestions(chosenCategories, num);
  }

  /**
    * This function creates a Game Object with the given player and questions, but without playing
    * the game yet.
    *
    * <p>It is important that you neither play the game yet nor persist the game! This is just meant
    * to create the game object.</p>
    *
    * @param player    The Player which shall play the game.
    * @param questions The Questions which shall be asked during the game.
    * @return A Game Object which contains an unplayed game
    * for the given player with the given questions.
    */
  @Override
  public Object createGame(Object player, List<?> questions) {
    List<Answer> allAnswers = new ArrayList<>();
    Game g = new Game((Player) player, allAnswers);
    for (Object o: questions) {
      Answer a = new Answer((Question) o, g);
      allAnswers.add(a);
    }
    return g;
  }

  /**
   * This function simulates a game play without user interaction by randomly choosing answers.
   *
   * <p>There is also an interactive version of this function which shall be called from the menu.
   * </p>
   *
   * @param game The Game Object which shall be played.
   * @see Lab03Game#interactivePlayGame(Object)
   */
  @Override
  public void playGame(Object game) {
    Random r = new Random();
    Game g = (Game) game;
    g.setStart(new Date());
    for (Answer a: g.getAnswers()) {
      Object[] opt = a.getQuestion().getOptions().keySet().toArray();
      String givenAnswer = (String) opt[r.nextInt(4)];
      a.setAnswer(givenAnswer);
    }
    g.setEnd(new Date());
  }

  /**
   * This function plays the given game with the user, that is by using user interaction.
   *
   * <p>This is the function that should be called from the menu. Here you can implement the
   * necessary user interaction for the playing of the game.</p>
   *
   * @param game The Game Object which shall be played.
   * @see Lab03Game#playGame(Object)
   */
  @Override
  public void interactivePlayGame(Object game) {
    Scanner scan = new Scanner(System.in, "UTF-8");
    Game g = (Game) game;
    g.setStart(new Date());
    for (Answer a : g.getAnswers()) {
      Object[] opt = a.getQuestion().getOptions().keySet().toArray();
      System.out.println(a.getQuestion().getQuestion());
      System.out.println("[0] " + opt[0]);
      System.out.println("[1] " + opt[1]);
      System.out.println("[2] " + opt[2]);
      System.out.println("[3] " + opt[3]);
      System.out.println("====================");
      System.out.println("Your answer: ");
      int num = Integer.parseInt(scan.next());
      if (num < opt.length) {
        String givenAnswer = (String) opt[num];
        a.setAnswer(givenAnswer);
      }
      g.setEnd(new Date());
    }
  }

  /**
   * Persists a played game, including the player which played it.
   *
   * @param game The Game Object to be persisted.
   */
  @Override
  public void persistGame(Object game) {
    EntityManager em = ((Game) game).getEm();
    if (em == null) {
      em = this.lab02EntityManager.getEntityManager();
      ((Game)game).setEm(em);
    }
    try {
      if (em.find(Player.class, ((Game) game).getPlayer().getId()) == null) {
        em.persist(((Game) game).getPlayer());
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    em.persist(game);
  }
}
