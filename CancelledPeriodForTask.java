/** Assignment for session 2 
    
    
    Problem 1

    for problem 1 I used two classes 
        1)Plan.java - basic class
        2)CancelledPeriodForTask.java - contains the function getCancelledPeriodsForTask
 */
package com.vamsi;

import java.time.LocalDate;
import java.util.*;

public class CancelledPeriodForTask {

  public static void main(String[] args) throws Exception {

    List<Plan> oldPlanList = new ArrayList<>();
    oldPlanList.add(new Plan(101, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(102, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(103, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(104, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(105, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(106, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(107, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));

    List<Plan> newPlanList = new ArrayList<>();
    newPlanList.add(new Plan(101, LocalDate.of(2019, 5, 1), LocalDate.of(2019, 5, 5)));
    newPlanList.add(new Plan(102, LocalDate.of(2019, 5, 5), LocalDate.of(2019, 5, 15)));
    newPlanList.add(new Plan(103, LocalDate.of(2019, 5, 5), LocalDate.of(2019, 5, 25)));
    newPlanList.add(new Plan(104, LocalDate.of(2019, 5, 15), LocalDate.of(2019, 5, 18)));
    newPlanList.add(new Plan(105, LocalDate.of(2019, 5, 15), LocalDate.of(2019, 5, 25)));
    newPlanList.add(new Plan(106, LocalDate.of(2019, 5, 25), LocalDate.of(2019, 5, 30)));
    newPlanList.add(new Plan(107, LocalDate.of(2019, 5, 12), LocalDate.of(2019, 5, 13)));
    newPlanList.add(new Plan(107, LocalDate.of(2019, 5, 16), LocalDate.of(2019, 5, 18)));

    List<Plan> cancelledPlanPeriods = getCancelledPeriodsForTask(oldPlanList, newPlanList);

    System.out.println("CANCELLED PERIODS\n");
    System.out.println(cancelledPlanPeriods);
  }

 /* we are using stream ,filter and map from java 8 by using lambda functions */

  public static List<Plan> getCancelledPeriodsForTask(List<Plan> oldPlanList, List<Plan> newPlanList) {
    List<Plan> cancelledPlanPeriods = new ArrayList<>();
    List<Plan> cancelledPlanPeriodsForTask = new ArrayList<>();

    oldPlanList.stream().map(Plan::getTaskId).distinct().forEach(taskId -> {
      List<Plan> oldPlans = oldPlanList.stream().filter(oldPlan -> oldPlan.getTaskId().equals(taskId)).collect(Collectors.toList());
      List<Plan> newPlans = newPlanList.stream().filter(newPlan -> newPlan.getTaskId().equals(taskId)).collect(Collectors.toList());

      oldPlans.stream().forEach(oldPlan -> {
        newPlans.stream().forEach(newPlan -> {
          if (oldPlan.getStartDate().isAfter(newPlan.getEndDate()) || oldPlan.getEndDate().isBefore(newPlan.getStartDate())) {
            cancelledPlanPeriodsForTask.add(new Plan(taskId, oldPlan.getStartDate(), oldPlan.getEndDate()));
          }
          else {
            if (newPlan.getStartDate().isAfter(oldPlan.getStartDate()) && newPlan.getStartDate().isBefore(oldPlan.getEndDate())) {
              cancelledPlanPeriodsForTask.add(new Plan(taskId, oldPlan.getStartDate(), newPlan.getStartDate().minusDays(1L)));
            }
            if (newPlan.getEndDate().isAfter(oldPlan.getStartDate()) && newPlan.getEndDate().isBefore(oldPlan.getEndDate())) {
              cancelledPlanPeriodsForTask.add(new Plan(taskId, newPlan.getEndDate().plusDays(1L), oldPlan.getEndDate()));
            }
          }
        });
        List<LocalDate> dates = cancelledPlanPeriodsForTask.stream().map(Plan::getStartAndEndDate).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        Collections.sort(dates);
        for (int index = 0; index < dates.size(); index = index + 2) {
          cancelledPlanPeriods.add(new Plan(taskId, dates.get(index), dates.get(index + 1)));
        }
        cancelledPlanPeriodsForTask.clear();
      });
    });
    return cancelledPlanPeriods;
  }
}