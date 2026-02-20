package ru.yandex.practicum.gym;

import java.util.Objects;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int coachCount;

    public CounterOfTrainings(Coach coach, int trainingCount) {
        this.coach = coach;
        this.coachCount = trainingCount;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getСoachCount() {
        return coachCount;
    }

    @Override
    public int compareTo(CounterOfTrainings other) {
        return Integer.compare(other.coachCount, this.coachCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterOfTrainings counterOfTrainings = (CounterOfTrainings) o;
        return coachCount == counterOfTrainings.coachCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coachCount);
    }

    @Override
    public String toString() {
        return  coach.getSurname() + " " +
                coach.getName() + " " +
                coach.getMiddleName() + " " + coachCount;
    }
}
