package com.osi.hris.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDate;

@Entity
@Table(name = "Performance")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String rating;  
   

    private String feedback;

    @Column(nullable = false)
    private LocalDate reviewDate;

    
    public Performance() {}

    
    public Performance(Long employeeId, String goal, String rating, String feedback,
                       LocalDate reviewDate) {
        this.employeeId = employeeId;
        this.goal = goal;
        this.rating = rating;
        this.feedback = feedback;
        this.reviewDate = reviewDate;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
   }
