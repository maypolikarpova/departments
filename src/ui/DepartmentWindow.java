package ui;

import domain.Department;
import domain.Employee;
import domain.Position;
import repository.ConnectionManager;
import repository.DepartmentRepository;
import repository.EmployeeRepository;
import repository.PositionRepository;
import ui.department.AddDepartmentWindow;
import ui.department.EditDepartmentWindow;
import ui.employee.AddEmployeeWindow;
import ui.employee.FilterByEndDateWindow;
import ui.employee.FilterByStartDateWindow;
import ui.employee.SortByDateWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DepartmentWindow {
    private DepartmentWindow mainWindow;
    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    private JTable departmentTable;
    private JButton deleteDepartmentButton;
    private JButton editDepartmentButton;
    private JButton addEmployeeButton;
    private JTable employeeTable;
    private PositionRepository positionRepository;
    private JButton filterByStartDatesButton;
    private JButton filterByEndDatedButton;
    private JButton sortByDateButton;

    public DepartmentWindow() {
        mainWindow = this;
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connect("Projects", "root", "qwertyuiop");

        departmentRepository = new DepartmentRepository(connectionManager);
        employeeRepository = new EmployeeRepository(connectionManager);
        positionRepository = new PositionRepository(connectionManager);

        initialize();
    }

    public void refreshDepartmentTable() {
        departmentTable.setModel(fillDepartmentTable((DefaultTableModel) departmentTable.getModel()));
    }

    public void refreshEmployeeTable() {
        employeeTable.setModel(fillEmployeeTable((DefaultTableModel) employeeTable.getModel()));
    }

    private void initialize() {
        JFrame frame = new JFrame();
        frame.setSize(900, 600);
        frame.setTitle("Відділи");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JComponent panel1 = addDepartmentPanel();
        tabbedPane.addTab("Відділ", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = addEmployeePanel();
        tabbedPane.addTab("Управління  співробітниками", panel2);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);
    }

    private JComponent addDepartmentPanel() {
        JPanel departmentPanel = new JPanel(false);
        departmentPanel.setLayout(new BorderLayout(0, 0));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel actionPanel = new JPanel();

        actionPanel.add(addDepartmentTable(), BorderLayout.CENTER);
        actionPanel.add(addDepartmentButton(), BorderLayout.EAST);
        actionPanel.add(deleteDepartment(), BorderLayout.EAST);
        actionPanel.add(editDepartment(), BorderLayout.EAST);

        departmentPanel.add(controlPanel);
        departmentPanel.add(actionPanel);

        return departmentPanel;
    }

    private JButton deleteDepartment() {

        deleteDepartmentButton = new JButton("Видалити");
        deleteDepartmentButton.addActionListener(e -> {
            int selectedDepartmentId = Integer.parseInt(departmentTable.getValueAt(departmentTable.getSelectedRow(), 0).toString());
            departmentRepository.deleteDepartment(selectedDepartmentId);
            refreshDepartmentTable();
        });
        deleteDepartmentButton.setVisible(false);

        return deleteDepartmentButton;
    }

    private JButton editDepartment() {

        editDepartmentButton = new JButton("Редагувати");
        editDepartmentButton.addActionListener(e -> {
            int selectedDepartmentId = Integer.parseInt(departmentTable.getValueAt(departmentTable.getSelectedRow(), 0).toString());
            new EditDepartmentWindow(mainWindow, departmentRepository, selectedDepartmentId);
        });
        editDepartmentButton.setVisible(false);

        return editDepartmentButton;
    }

    private JPanel addDepartmentTable() {

        JPanel tablePanel = new JPanel();
        departmentTable = new JTable();
        tablePanel.setLayout(new GridLayout());

        departmentTable.setFillsViewportHeight(true);
        departmentTable.setDefaultEditor(Object.class, null);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] columnNames = {"Номер", "Назва", "Номер телефону"};
        DefaultTableModel departmentTableModel = (DefaultTableModel) departmentTable.getModel();
        departmentTableModel.setColumnIdentifiers(columnNames);

        departmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (departmentTable.getSelectedRow() != -1) {
                deleteDepartmentButton.setVisible(true);
                editDepartmentButton.setVisible(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        tablePanel.add(scrollPane);

        refreshDepartmentTable();
        return tablePanel;
    }

    private DefaultTableModel fillDepartmentTable(DefaultTableModel departmentTableModel) {
        departmentTableModel.setRowCount(0);
        List<Department> departments = departmentRepository.getDepartments();

        for (Department department : departments) {
            String[] tableRow = new String[3];
            tableRow[0] = Integer.toString(department.getDepartmentId());
            tableRow[1] = department.getName();
            tableRow[2] = department.getPhone();
            departmentTableModel.addRow(tableRow);
        }

        return departmentTableModel;
    }

    private JButton addDepartmentButton() {
        JButton addDepartmentButton = new JButton("Додати новий відділ");
        addDepartmentButton.addActionListener(e -> {
            new AddDepartmentWindow(mainWindow, departmentRepository);
        });

        return addDepartmentButton;
    }

    private JComponent addEmployeePanel() {
        JPanel employeePanel = new JPanel(false);
        employeePanel.setLayout(new BorderLayout(0, 0));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel actionPanel = new JPanel();

        actionPanel.add(addChooseDepartmentBox());
        actionPanel.add(createEmployeeTable(), BorderLayout.CENTER);
        actionPanel.add(addFilterByStartDatesButton(), BorderLayout.EAST);
        actionPanel.add(addFilterByEndDatedButton(), BorderLayout.EAST);
        actionPanel.add(addSortByDateButton(), BorderLayout.EAST);
        actionPanel.add(createAddEmployeeButton());

        employeePanel.add(controlPanel);
        employeePanel.add(actionPanel);

        return employeePanel;
    }

    private JButton createAddEmployeeButton() {
        addEmployeeButton = new JButton("Додати співробітника до відділу");
        addEmployeeButton.addActionListener(e -> {
            int selectedEmployeeId = Integer.parseInt(employeeTable.getValueAt(employeeTable.getSelectedRow(), 0).toString());
            new AddEmployeeWindow(mainWindow, positionRepository, departmentRepository, selectedEmployeeId);
        });

        return addEmployeeButton;
    }

    private JComboBox addChooseDepartmentBox() {

        JComboBox comboBox = new JComboBox(departmentRepository.getDepartments().stream()
                                                   .map(Department::getDepartmentId)
                                                   .map(Objects::toString).toArray());

        ActionListener actionListener = e -> {
            JComboBox box = (JComboBox) e.getSource();
            String item = (String) box.getSelectedItem();
        };
        comboBox.addActionListener(actionListener);

        return comboBox;
    }

    private JPanel createEmployeeTable() {
        JPanel tablePanel = new JPanel();
        employeeTable = new JTable();
        tablePanel.setLayout(new GridLayout());
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setDefaultEditor(Object.class, null);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] columnNames = {"Номер", "ПІБ", "Посада", "Стать", "Дата народження", "Оклад", "Дата початку роботи", "Дата закінчення"};
        DefaultTableModel departmentTableModel = (DefaultTableModel) employeeTable.getModel();
        departmentTableModel.setColumnIdentifiers(columnNames);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (employeeTable.getSelectedRow() != -1) {
                addEmployeeButton.setVisible(true);
            }
        });

        employeeTable.setModel(fillEmployeeTable((DefaultTableModel) employeeTable.getModel()));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        tablePanel.add(scrollPane);

        return tablePanel;
    }

    private DefaultTableModel fillEmployeeTable(DefaultTableModel employeeTableModel) {
        employeeTableModel.setRowCount(0);
        List<Employee> employees = employeeRepository.getEmployees();

        for (Employee employee : employees) {
            String[] tableRow = new String[8];
            Integer employeeId = employee.getId();
            Position position = positionRepository.getPosition(employeeId);

            tableRow[0] = Integer.toString(employeeId);
            tableRow[1] = employee.getName();
            tableRow[3] = employee.getGender();
            tableRow[4] = employee.getBirthDate().toString();

            if (position != null) {
                tableRow[2] = position.getDescription();
                tableRow[5] = position.getSalary().toString();
                tableRow[6] = position.getStartDate().toString();
                Date endDate = position.getEndDate();
                if (endDate != null) {
                    tableRow[7] = endDate.toString();
                }
            }
            
            employeeTableModel.addRow(tableRow);
        }

        return employeeTableModel;
    }

    private JButton addFilterByStartDatesButton() {
        filterByStartDatesButton = new JButton("Фільтрувати за датою початку роботи");
        filterByStartDatesButton.addActionListener(e -> {
            new FilterByStartDateWindow(mainWindow, departmentRepository);
        });

        return filterByStartDatesButton;
    }

    private JButton addFilterByEndDatedButton() {
        filterByEndDatedButton = new JButton("Фільтрувати за датою закінчення роботи");
        filterByEndDatedButton.addActionListener(e -> {
            new FilterByEndDateWindow(mainWindow, departmentRepository);
        });

        return filterByEndDatedButton;
    }

    private JButton addSortByDateButton() {
        sortByDateButton = new JButton("Сортувати за складом на певну дату");
        sortByDateButton.addActionListener(e -> {
            new SortByDateWindow(mainWindow, departmentRepository);
        });

        return sortByDateButton;
    }
}
