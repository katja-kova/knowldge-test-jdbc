package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Lab02EntityManagerImpl extends Lab02EntityManager {
  private EntityManagerFactory emf;

  public Lab02EntityManagerImpl()  {
    this.emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
  }

  /**
   * There you have to persist the data in the database.
   */
  @Override
  public void persistData() {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    for (int i = 0; i < this.lab01Data.getCategories().size(); i++) {
      em.persist(this.lab01Data.getCategories().get(i));
    }
    for (int i = 0; i < this.lab01Data.getQuestions().size(); i++) {
      em.persist(this.lab01Data.getQuestions().get(i));
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Return a valid EntityManager.
   *
   * @return EntityManager
   */
  @Override
  public EntityManager getEntityManager() {
    EntityManager em = null;
    try {
      em = emf.createEntityManager();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return em;
  }
}