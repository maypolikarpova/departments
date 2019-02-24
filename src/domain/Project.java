package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Project {

    private int id;
    private String client;
    private LocalDate startDate;
    private LocalDate  endDatePlanned;
    private LocalDate  endDateActual;
    private BigDecimal price;
    private BigDecimal expense;
    private String estimation;
    private int departmentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDatePlanned() {
        return endDatePlanned;
    }

    public void setEndDatePlanned(LocalDate endDatePlanned) {
        this.endDatePlanned = endDatePlanned;
    }

    public LocalDate getEndDateActual() {
        return endDateActual;
    }

    public void setEndDateActual(LocalDate endDateActual) {
        this.endDateActual = endDateActual;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
