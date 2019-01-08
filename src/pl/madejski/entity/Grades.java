package pl.madejski.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Grades {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Subject subject;
    private Integer grade;

    public Grades() {
        this(null, null);
    }

    public Grades(Subject subject, Integer grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "\t\t- " + subject.toString() + ": " + getGrade() + "\n";
    }
}
