package pl.madejski.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String surname;
    private List<Grades> grades;

    public Student() {
        this(null, null, null, new ArrayList<>());
    }

    public Student(Integer id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.grades = new ArrayList<>();
    }

    public Student(Integer id, String name, String surname, List<Grades> grades) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.grades = grades;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Grades> getGrades() {
        return grades;
    }

    public void setGrades(List<Grades> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        String result = "- " + getName() + " " + getSurname() + "\n" +
                "\t - Oceny: \n";
        for(Grades g: getGrades()) {
            result = result + "\t\t- " + g.getSubject().toString() + ": " + g.getGrade() + "\n";
        }
        return result;
    }
}
