package pl.madejski;

import pl.madejski.entity.Grades;
import pl.madejski.entity.Student;
import pl.madejski.entity.Subject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    private static EntityManagerFactory factory;
    private static Random random = new Random();

    private static void setUpStudents(EntityManager entityManager, List<Subject> subjectList) {
        List<String> names = Arrays.asList("Sebastian", "Ryszard", "Jacek", "Michał");
        List<String> surnames = Arrays.asList("Madejski", "Andrzejewski", "Graniecki", "Pałeczka");
        for(int i=0;i<4;i++) {
            Student student = new Student(null, names.get(i), surnames.get(i));
            subjectList.forEach(s -> {
                Grades newGrades = new Grades(s, random.nextInt(2) + 3);
                entityManager.persist(newGrades);
                student.getGrades().add(newGrades);
            });
            entityManager.persist(student);
        }
    }

    private static void setUp() {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Subject> subjectList = new ArrayList<>();

        Subject subject = new Subject(null, "Zaawansowane Programowanie Obiektowe");
        entityManager.persist(subject);
        subjectList.add(subject);

        subject = new Subject(null, "Podstawy Inżynierii Oprogramowania");
        entityManager.persist(subject);
        subjectList.add(subject);

        subject = new Subject(null, "Modelowanie i Analiza Obiektowa");
        entityManager.persist(subject);
        subjectList.add(subject);

        setUpStudents(entityManager, subjectList);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static void deleteSubject(String subjectName) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Query studentQuery = entityManager.createQuery("SELECT s FROM Student s");
            List<Student> students = studentQuery.getResultList();
            for(Student student: students) {
                List<Grades> cutGrades = student.getGrades().stream()
                        .filter(g -> {
                            if(g.getSubject().getName().equals(subjectName)) {
                                entityManager.remove(g);
                                return false;
                            }
                            else return true;
                        })
                        .collect(Collectors.toList());
                student.setGrades(cutGrades);
                entityManager.persist(student);
            }

            Query subjectQuery = entityManager.createQuery("SELECT s FROM Subject s WHERE s.name = :subjectName");
            subjectQuery.setParameter("subjectName", subjectName);
            Subject subject = (Subject) subjectQuery.getSingleResult();
            entityManager.remove(subject);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if(entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private static void printAllStudents() {
        EntityManager entityManager = factory.createEntityManager();
        Query studentQuery = entityManager.createQuery("SELECT s FROM Student s");
        studentQuery.getResultList().stream().filter(s -> s != null).forEachOrdered(System.out::println);
        entityManager.close();
    }

    private static void printSubjectGradeAverage() {
        EntityManager entityManager = factory.createEntityManager();
        Query gradeQuery = entityManager.createQuery("SELECT g.subject.name, AVG(g.grade) FROM Grades g GROUP BY g.subject.name");
        List<Object[]> gradeAverage = gradeQuery.getResultList();
        System.out.println("--- Srednie ocen ---");
        gradeAverage.forEach(row -> {
            String subjectName = (String) row[0]; // g.subject.name
            Double averageGrade = (Double) row[1]; // AVG(g.grade)

            System.out.println(subjectName + ": " + averageGrade);
        });
        entityManager.close();
    }

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory("students");
        setUp();

        printAllStudents();

        printSubjectGradeAverage();

        deleteSubject("Podstawy Inżynierii Oprogramowania");
        printAllStudents();
    }
}
