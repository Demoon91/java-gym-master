package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        try {
            validationCheck(timeOfDay);
            Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
            if (daySchedule == null) {
                daySchedule = new TreeMap<>();
                timetable.put(dayOfWeek, daySchedule);
            }
            List<TrainingSession> listSession = daySchedule.get(timeOfDay);
            if (listSession == null) {
                listSession = new ArrayList<>();
                daySchedule.put(timeOfDay, listSession);
            }
            listSession.add(trainingSession);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: при добавлении " + trainingSession.getDayOfWeek());
            System.out.println(e.getMessage());
        }
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        Map<TimeOfDay, List<TrainingSession>> dayShedule = timetable.get(dayOfWeek);
        if (dayShedule == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(timetable.get(dayOfWeek));
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        if (sessions == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(sessions);
    }

    public Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> getTimetable() {
        return new HashMap<>(timetable);
    }

    public ArrayList<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCountMap = new HashMap<>();
        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession trainig : sessions) {
                    Coach coach = trainig.getCoach();
                    if (coachCountMap.containsKey(coach)) {
                        int count = coachCountMap.get(coach);
                        coachCountMap.put(coach, count + 1);
                    } else {
                        coachCountMap.put(coach, 1);
                    }
                }
            }
        }
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCountMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        Collections.sort(result);
        return new ArrayList<>(result);
    }

    public void validationCheck(TimeOfDay timeOfDay) {
        if (timeOfDay.getHours() < 0 || timeOfDay.getHours() > 23
                || timeOfDay.getMinutes() < 0 || timeOfDay.getMinutes() > 59) {
            throw new IllegalArgumentException("Время введено не верно - часы должны быть (от 0 до 23)" +
                    " ; минуты (от 0 до 59)" + " введено значение - " +
                    timeOfDay.getHours() + ":" + timeOfDay.getMinutes());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timetable other = (Timetable) o;
        return Objects.equals(this.timetable, other.timetable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timetable);
    }
}