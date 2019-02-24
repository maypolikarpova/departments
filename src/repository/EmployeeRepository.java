package repository;

import domain.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    private ConnectionManager connectionManager;

    public EmployeeRepository(ConnectionManager connectionManager) {
       this.connectionManager = connectionManager;
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Employee";
            connectionManager.getStatement().execute(sql);
            ResultSet resultSet = connectionManager.getStatement().getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    employees.add(new Employee(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDate(4)));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }
}