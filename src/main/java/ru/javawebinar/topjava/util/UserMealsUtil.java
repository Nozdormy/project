package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> resultList = new ArrayList<>();
        int totalCalories = 0;
        boolean excess;

        for (UserMeal meal : meals) {
            LocalTime temp = meal.getDateTime().toLocalTime();
            if ((temp.isAfter(startTime) || temp.equals(startTime)) && (temp.isBefore(endTime) || temp.equals(endTime))) {
                totalCalories += meal.getCalories();
                excess = totalCalories > caloriesPerDay;

                resultList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return resultList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> filteredMeals = meals.stream()
                .filter(meal -> meal.getDateTime().toLocalTime().isAfter(startTime) || meal.getDateTime().toLocalTime().equals(startTime))
                .filter(meal -> meal.getDateTime().toLocalTime().isBefore(endTime) || meal.getDateTime().toLocalTime().equals(endTime))
                .collect(Collectors.toList());

        int totalCalories = filteredMeals.stream()
                .mapToInt(UserMeal::getCalories)
                .reduce(0, Integer::sum);

        boolean hasExcess = totalCalories > caloriesPerDay;

        return filteredMeals.stream()
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), hasExcess))
                .collect(Collectors.toList());
    }
}