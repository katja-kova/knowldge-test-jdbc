package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.controller.CsvDataReader;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Lab01DataImpl extends Lab01Data {

  List<Category> categories = new ArrayList<>();
  List<Question> questions = new ArrayList<>();

  @Override
  public List<Question> getQuestions() {
    return this.questions;
  }

  @Override
  public List<Category> getCategories() {
    return this.categories;
  }

  @Override
  public void loadCsvFile(List<String[]> additionalCsvLines) {
    try {
      for (int i = 1; i < additionalCsvLines.size(); i++) {
        //line should have these Values (copied from first row in CSV-file)
        // ID;_frage;_antwort_1;_antwort_2;_antwort_3;_antwort_4;_loesung;_kategorie

        String[] line = additionalCsvLines.get(i);
        //generate the options map
        Map<String, Boolean> options = new HashMap<>();
        int answer = Integer.parseInt(line[6]);
        options.put(line[2], (answer == 1));
        options.put(line[3], (answer == 2));
        options.put(line[4], (answer == 3));
        options.put(line[5], (answer == 4));

        // create Category for read line
        Category category = new Category(line[7]);
        // try to find it in the existing categories.
        // The binary search Method returns 0..N for a found value.
        // If the binary Search Method did not find a match, it returns (-(insertion point) - 1)

        boolean found = false;
        for (Category c: this.categories) {
          if (c.getName().equals(category.getName())) {
            category = c;
            found = true;
          }
        }
        if (!found) {
          this.categories.add(category);
        }
        /*int pos = Arrays.binarySearch(this.categories.toArray(), category);
        System.out.println("OK");
        if (pos >= 0) {
          //found, so just override the newish object
          category = this.categories.get(pos);
        } else {
          //add the new object at the right position to prevent sorting
          this.categories.add(-(pos + 1), category);
        }
        System.out.println("OK");

        // */


        Question q = new Question(Integer.parseInt(line[0]), line[1], options, category);
        this.questions.add(q);
        category.addQuestion(q);

        String printString = q.getQuestion() + "; " + q.getCategory().getName() + "; ";
        printString = printString.concat(q.getOptions().keySet() + "; ");
        int k = 0;
        for (String s: q.getOptions().keySet()) {
          if (q.getOptions().get(s)) {
            break;
          }
          k++;
        }
        printString += k;
        System.out.println(printString);
      }
      System.out.println("Number of Categories: " + this.categories.size());
      System.out.println("Number of Questions: " + this.questions.size());
    } catch (RuntimeException e/* | IOException | URISyntaxException e*/) {
      System.out.println(e);
    }
  }
}