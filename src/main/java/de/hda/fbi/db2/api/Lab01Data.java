package de.hda.fbi.db2.api;

import java.util.List;

/**
 * API Class for lab01 Created by l.koehler on 05.08.2019.
 */
public abstract class Lab01Data {

  public void init() {
  }

  /**
   * Return all questions.
   *
   * @return questions
   */
  public abstract List<?> getQuestions();

  /**
   * Return all categories.
   *
   * @return categories
   */
  public abstract List<?> getCategories();

  /**
   * Save the csv data in appropriate objects.
   *
   * @param additionalCsvLines is the csv data
   */
  public abstract void loadCsvFile(List<String[]> additionalCsvLines);
}
