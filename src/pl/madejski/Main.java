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

public class Main {

    private static EntityManagerFactory factory;
    private static Random random = new Random();

    private static void setUpStudents(EntityManager entityManager, List<Subject> subjectList) {
        List<String> names = Arrays.asList("Sebastian", "Michał", "Adam", "Ernest");
        List<String> surnames = Arrays.asList("Madejsky", "Lukiansky", "Syllah", "Figurbaum");
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

        subject = new Subject(null, "Zaawansowana Sztuczna Inteligencja");
        entityManager.persist(subject);
        subjectList.add(subject);

        subject = new Subject(null, "Bazy danych dla opornych");
        entityManager.persist(subject);
        subjectList.add(subject);

        setUpStudents(entityManager, subjectList);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory("students");
        setUp();

        EntityManager entityManager = factory.createEntityManager();
        Query studentQuery = entityManager.createQuery("SELECT s FROM Student s");
        studentQuery.getResultList().stream().filter(s -> s != null).forEachOrdered(System.out::println);

        
        entityManager.close();

    }
}
