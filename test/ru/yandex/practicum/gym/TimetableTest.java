package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, mondaySessions.size());
        //Проверить, что за вторник не вернулось занятий
        assertEquals(Collections.emptyMap(), tuesdaySessions);
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());

        List<TimeOfDay> keys = new ArrayList<>(thursdaySessions.keySet());
        for (int i = 0; i < keys.size(); i++) {
            assertEquals("13:0", keys.get(0).toString());
            assertEquals("20:0", keys.get(1).toString());
        }

        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(Collections.emptyMap(), tuesdaySessions);
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, mondaySessions.size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> otherMondaySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        //assertEquals(null, otherMondaySessions);
        assertEquals(Collections.emptyList(), otherMondaySessions);
    }

    @Test
    void testGetTraingSessionInRightTimeOfHours() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession mondaySession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(-1, 0));
        TrainingSession thuesdaySession = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(0, 0));
        TrainingSession wednesdaySession = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(23, 0));
        TrainingSession thursdaySession = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(24, 0));

        timetable.addNewTrainingSession(mondaySession);
        timetable.addNewTrainingSession(thuesdaySession);
        timetable.addNewTrainingSession(wednesdaySession);
        timetable.addNewTrainingSession(thursdaySession);

        Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> testTimetable = new HashMap<>((Map) timetable.getTimetable());
        TimeOfDay thuesdayTimeTraning = thuesdaySession.getTimeOfDay();
        TimeOfDay wednesdayTimeTraning = wednesdaySession.getTimeOfDay();

        assertEquals(2, testTimetable.size());
        assertEquals((new TimeOfDay(0, 0)), thuesdayTimeTraning);
        assertEquals((new TimeOfDay(23, 0)), wednesdayTimeTraning);
    }

    @Test
    void testGetTraingSessionInRightTimeOfMinutes() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession mondaySession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(11, -1));
        TrainingSession thuesdaySession = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(11, 0));
        TrainingSession wednesdaySession = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(11, 59));
        TrainingSession thursdaySession = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(11, 60));

        timetable.addNewTrainingSession(mondaySession);
        timetable.addNewTrainingSession(thuesdaySession);
        timetable.addNewTrainingSession(wednesdaySession);
        timetable.addNewTrainingSession(thursdaySession);

        Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> testTimetable = new HashMap<>((Map) timetable.getTimetable());
        TimeOfDay thuesdayTimeTraning = thuesdaySession.getTimeOfDay();
        TimeOfDay wednesdayTimeTraning = wednesdaySession.getTimeOfDay();

        assertEquals(2, testTimetable.size());
        assertEquals((new TimeOfDay(11, 0)), thuesdayTimeTraning);
        assertEquals((new TimeOfDay(11, 59)), wednesdayTimeTraning);

        assertEquals(null, testTimetable.get(thursdaySession));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeReturnList() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Сергей", "Михайлович");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Йога", Age.ADULT, 90);

        TrainingSession mondayChildSession = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(12, 00));
        TrainingSession mondayAdultSession = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(12, 00));

        timetable.addNewTrainingSession(mondayChildSession);
        timetable.addNewTrainingSession(mondayAdultSession);

        List<TrainingSession> mondayList = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        assertNotNull(mondayList);
        assertEquals(2, mondayList.size());
        assertFalse(mondayList.isEmpty());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Сергей", "Михайлович");
        Coach coach3 = new Coach("Петров", "Евгений", "Эдуардович");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Йога", Age.ADULT, 90);
        Group group3 = new Group("Кросфит", Age.CHILD, 45);
        Group group4 = new Group("Аэробика", Age.ADULT, 60);
        Group group5 = new Group("Бокс", Age.CHILD, 45);
        Group group6 = new Group("Фитнес", Age.ADULT, 60);

        TrainingSession trainingSession1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(12, 00));
        TrainingSession trainingSession2 = new TrainingSession(group2, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(11, 00));
        TrainingSession trainingSession3 = new TrainingSession(group3, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(18, 00));
        TrainingSession trainingSession4 = new TrainingSession(group4, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(12, 00));
        TrainingSession trainingSession5 = new TrainingSession(group5, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(12, 00));
        TrainingSession trainingSession6 = new TrainingSession(group6, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(12, 00));

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);
        timetable.addNewTrainingSession(trainingSession6);

        List<CounterOfTrainings> coachCount = timetable.getCountByCoaches();

        assertEquals(3, coachCount.get(0).getСoachCount());
        assertEquals(2, coachCount.get(1).getСoachCount());
        assertEquals(1, coachCount.get(2).getСoachCount());
        assertEquals(3, coachCount.size());
        assertNotNull(coachCount);
    }

    @Test
    void testGetCountByCoachesRightSort() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Н.", "С.");
        Coach coach2 = new Coach("Иванов", "С.", "М.");
        Coach coach3 = new Coach("Петров", "Е.", "Э.");

        timetable.addNewTrainingSession(new TrainingSession(new Group("Акробатика для детей", Age.CHILD,
                60), coach3, DayOfWeek.MONDAY, new TimeOfDay(12, 00)));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Йога", Age.ADULT,
                90), coach2, DayOfWeek.MONDAY, new TimeOfDay(12, 00)));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Кросфит", Age.CHILD,
                45), coach2, DayOfWeek.TUESDAY, new TimeOfDay(12, 00)));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Аэробика", Age.ADULT,
                60), coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 00)));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Бокс", Age.CHILD,
                45), coach1, DayOfWeek.FRIDAY, new TimeOfDay(12, 00)));
        timetable.addNewTrainingSession(new TrainingSession(new Group("Фитнес", Age.ADULT,
                60), coach1, DayOfWeek.SATURDAY, new TimeOfDay(12, 00)));

        assertEquals("Васильев Н. С.", timetable.getCountByCoaches().get(0).getCoach().toString());
        assertEquals(3, timetable.getCountByCoaches().get(0).getСoachCount());

        assertEquals("Иванов С. М.", timetable.getCountByCoaches().get(1).getCoach().toString());
        assertEquals(2, timetable.getCountByCoaches().get(1).getСoachCount());

        assertEquals("Петров Е. Э.", timetable.getCountByCoaches().get(2).getCoach().toString());
        assertEquals(1, timetable.getCountByCoaches().get(2).getСoachCount());
    }

    @Test
    void testGetCountByCoachesReturnEmptyList() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> coachCount = timetable.getCountByCoaches();

        assertNotNull(coachCount);
        assertTrue(coachCount.isEmpty());
    }
}