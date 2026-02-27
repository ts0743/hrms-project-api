package com.osi.hris.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PerformanceRequestDTO {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Goal is required")
    private String goal;

    @NotBlank(message = "Rating is required")
    private String rating;

    private String feedback;

    @NotNull(message = "Review date is required")
    private LocalDate reviewDate;

    // Getters and setters
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
