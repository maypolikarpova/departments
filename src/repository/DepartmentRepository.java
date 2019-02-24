package repository;

import domain.Department;

import java.awt.print.Book;
import java.sql.*;
import java.util.ArrayList;

public class DepartmentRepository {

    private ConnectionManager connectionManager;

    public DepartmentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Department getDepartment(int departmentId) {
        Department department = null;
        try {
            String sql = "SELECT departmentId, name, phone FROM Department WHERE departmentId = " + departmentId;
            connectionManager.getStatement().execute(sql);
            ResultSet resultSet = connectionManager.getStatement().getResultSet();
            if ((resultSet != null) && (resultSet.next())) {
                department = new Department(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return department;
    }

    public int insertDepartment(String departmentName, String phone) {
        int departmentId = connectionManager.getNextId("Department", "department_id");
        int rows = 0;
        try {
            connectionManager.setPreparedStatement(connectionManager.getConnection().prepareStatement(
                    "INSERT INTO Department (department_id, department_name, department_phone) VALUES(" + departmentId + ",?,?)"
            ));
            connectionManager.getPreparedStatement().setString(1, departmentName);
            connectionManager.getPreparedStatement().setString(2, phone);
            rows = connectionManager.getPreparedStatement().executeUpdate();
            if (rows == 0) departmentId = 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return departmentId;
    }

    public void updateDepartment(int departmentId, String name, String phone) {
        try {
            String sql = "UPDATE Departmetns SET";
            boolean updatesDefined = false;
            if (name != null) {
                sql += " name = '" + name + "'";
                updatesDefined = true;
            }
            if (phone != null) {
                if (updatesDefined) sql += ",";
                sql += " phoneNumber = '" + phone + "'";
                updatesDefined = true;
            }
            sql += " WHERE readerId = " + departmentId;
            if (updatesDefined) {
                connectionManager.getStatement().executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDepartment(int departmentId) {
        try {
            String sql = "delete from Departmet where departmentId = " + departmentId;
            connectionManager.getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Department> getDepartments() {
        ArrayList<Department> departments = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Department";
            connectionManager.getStatement().execute(sql);
            ResultSet resultSet = connectionManager.getStatement().getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    departments.add(new Department(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3)));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return departments;
    }

}