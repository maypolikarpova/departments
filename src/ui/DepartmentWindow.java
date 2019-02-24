package ui;

import domain.Department;
import domain.Employee;
import repository.ConnectionManager;
import repository.DepartmentRepository;
import repository.EmployeeRepository;
import ui.popUpWindows.*;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class DepartmentWindow {
    private DepartmentWindow mainWindow;
    private JFrame frame;
    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;
    private ConnectionManager connectionManager;

    private JTable departmentTable;
    private JButton departmentEditButton;
    private JButton employeeEditButton;

    private int selectedDepartmentId;

    public DepartmentWindow() {
        mainWindow = this;
        connectionManager = new ConnectionManager();
        connectionManager.connect("Projects", "root", "qwertyuiop");

        departmentRepository = new DepartmentRepository(connectionManager);
        employeeRepository = new EmployeeRepository(connectionManager);

        initialize();
    }

    public void refreshDepartmentTable() {
        departmentTable.setModel(fillDepartmentTable((DefaultTableModel) departmentTable.getModel()));
    }

    private void initialize() {
        frame = new JFrame();
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
        actionPanel.add(addDepartmentButton());
        actionPanel.add(addEmployeeButton());
        actionPanel.add(addEditDepartmentButton());

        departmentPanel.add(controlPanel);
        departmentPanel.add(actionPanel);

        return departmentPanel;
    }


    private JComponent addEmployeePanel() {
        JPanel employeePanel = new JPanel(false);
        employeePanel.setLayout(new BorderLayout(0, 0));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel actionPanel = new JPanel();

        actionPanel.add(addChooseDepartmentBox());
        actionPanel.add(addEmployeeTable(), BorderLayout.CENTER);
        actionPanel.add(addFilterByStartDatesButton(), BorderLayout.EAST);
        actionPanel.add(addFilterByEndDatedButton(), BorderLayout.EAST);
        actionPanel.add(addSortByDateButton(), BorderLayout.EAST);
        actionPanel.add(addEditEmployeeButton());

        employeePanel.add(controlPanel);
        employeePanel.add(actionPanel);

        return employeePanel;
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
                departmentEditButton.setVisible(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        tablePanel.add(scrollPane);

        refreshDepartmentTable();
        return tablePanel;
    }

    private DefaultTableModel fillDepartmentTable(DefaultTableModel departmentTableModel) {
        departmentTableModel.setRowCount(0);
        ArrayList<Department> departments = departmentRepository.getDepartments();

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

    private JButton addEmployeeButton() {
        JButton addEmployeeButton = new JButton("Додати співробітників");
        addEmployeeButton.addActionListener(e -> {
            new AddEmployeeWindow(mainWindow, departmentRepository);
        });

        return addEmployeeButton;
    }

    private JButton addEditDepartmentButton() {
        departmentEditButton = new JButton("Редагувати");
        departmentEditButton.addActionListener(e -> {
            new EditDepartmentWindow(mainWindow, departmentRepository);
        });
        departmentEditButton.setVisible(false);

        return departmentEditButton;
    }

    private JComboBox addChooseDepartmentBox() {
        String[] items = {"1", "2", "3"};
        JComboBox comboBox = new JComboBox(items);

        ActionListener actionListener = e -> {
            JComboBox box = (JComboBox) e.getSource();
            String item = (String) box.getSelectedItem();
            selectedDepartmentId = Integer.valueOf(item);
        };
        comboBox.addActionListener(actionListener);

        return comboBox;
    }

    private JPanel addEmployeeTable() {
        JPanel tablePanel = new JPanel();
        JTable employeeTable = new JTable();
        tablePanel.setLayout(new GridLayout());
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setDefaultEditor(Object.class, null);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] columnNames = {"Номер", "ПІБ", "Посада", "Стать", "Дата народження", "Дата початку роботи", "Дата закінчення"};
        DefaultTableModel departmentTableModel = (DefaultTableModel) employeeTable.getModel();
        departmentTableModel.setColumnIdentifiers(columnNames);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (employeeTable.getSelectedRow() != -1) {
                employeeEditButton.setVisible(true);
            }
        });

        employeeTable.setModel(fillEmployeeTable((DefaultTableModel) employeeTable.getModel()));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        tablePanel.add(scrollPane);

        return tablePanel;
    }

    private DefaultTableModel fillEmployeeTable(DefaultTableModel employeeTableModel) {
        employeeTableModel.setRowCount(0);
        ArrayList<Employee> employees = employeeRepository.getEmployees();

        for (Employee employee : employees) {
            String[] tableRow = new String[4];
            tableRow[0] = Integer.toString(employee.getId());
            tableRow[1] = employee.getName();
            tableRow[2] = employee.getGender();
            tableRow[3] = employee.getBirthDate().toString();
            employeeTableModel.addRow(tableRow);
        }

        return employeeTableModel;
    }

    private JButton addFilterByStartDatesButton() {
        departmentEditButton = new JButton("Фільтрувати за датою початку роботи");
        departmentEditButton.addActionListener(e -> {
            new FilterByStartDateWindow(mainWindow, departmentRepository);
        });

        return departmentEditButton;
    }

    private JButton addFilterByEndDatedButton() {
        departmentEditButton = new JButton("Фільтрувати за датою закінчення роботи");
        departmentEditButton.addActionListener(e -> {
            new FilterByEndDateWindow(mainWindow, departmentRepository);
        });

        return departmentEditButton;
    }

    private JButton addSortByDateButton() {
        departmentEditButton = new JButton("Сортувати за складом на певну дату");
        departmentEditButton.addActionListener(e -> {
            new SortByDateWindow(mainWindow, departmentRepository);
        });

        return departmentEditButton;
    }

    private JButton addEditEmployeeButton() {
        employeeEditButton = new JButton("Редагувати");
        employeeEditButton.addActionListener(e -> {
            new EditEmployeeButton(mainWindow, employeeRepository);
        });
        employeeEditButton.setVisible(false);

        return employeeEditButton;
    }

}
