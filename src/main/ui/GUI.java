package ui;

import com.sun.javafx.robot.impl.FXRobotHelper;
import model.Employee;
import model.State;
import model.leave.Leave;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

import static model.Role.HUMAN_RESOURCES;
import static model.Role.LEGAL_ASSISTANT;

public class GUI extends JPanel
        implements
        ListSelectionListener,
        ActionListener {

    private State state;
    private JList employees;
    private JList leave;

    private Employee selectedEmployee;

    private DefaultListModel listModelEmployees;
    private DefaultListModel listModelLeave;

    protected JButton createEmployee;
    protected JButton createLeave;
    protected JButton editEmployee;
    protected JButton selectEmployee;
    private static final String CREATE_LEAVE = "Create New Leave";
    private static final String CREATE_EMPLOYEE = "Create New Employee";
    private static final String EDIT_EMPLOYEE = "Edit Employee";
    private static final String EMPLOYEE_INFO = "Employee Info";
    private static final String SELECT_EMPLOYEE = "Select Employee";


    //EFFECTS: Create new GUI
    public GUI() {
        setLayout(new GridBagLayout());

        //Temp setup
        this.state = new State(LocalDate.now());
        state.addEmployee(new Employee("2002-07-08", LEGAL_ASSISTANT, "Jerry", 6.5, "Jane", "Criminal Law"));
        state.addEmployee(new Employee(LocalDate.now(), HUMAN_RESOURCES, "Harold", 7.5, "Kane", "Realty"));

        //Left frame of the GUI
        JPanel employeeList = new JPanel();
        employeeList.setLayout(new GridBagLayout());

        //Right Frame of the GUI
        JPanel rightHalf = new JPanel();

        //creating list of employees
        listModelEmployees = new DefaultListModel();
        for (Employee e :
                state.getSetOfEmployees()) {
            listModelEmployees.addElement(e);
        }


        employees = new JList(listModelEmployees);
        employees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employees.setSelectedIndex(0);
        employees.addListSelectionListener(this);

        Employee selectedEmployee = (Employee) employees.getSelectedValue();

        createEmployee = new JButton(CREATE_EMPLOYEE);
        createEmployee.setVerticalTextPosition(AbstractButton.CENTER);
        createEmployee.setHorizontalTextPosition(AbstractButton.LEADING);
        createEmployee.setMnemonic(KeyEvent.VK_D);
        createEmployee.setActionCommand("create employee");

        listModelLeave = new DefaultListModel();
        for (Leave l :
                selectedEmployee.getLeaveTaken()) {
            listModelLeave.addElement(l);
        }

        leave = new JList(listModelLeave);
        leave.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leave.setSelectedIndex(0);
        leave.addListSelectionListener(this);

        createLeave = new JButton(CREATE_LEAVE);
        createLeave.setVerticalTextPosition(AbstractButton.CENTER);
        createLeave.setHorizontalTextPosition(AbstractButton.LEADING);
        createLeave.setMnemonic(KeyEvent.VK_M);
        createLeave.setActionCommand("create leave");

        editEmployee = new JButton(EDIT_EMPLOYEE);
        editEmployee.setVerticalTextPosition(AbstractButton.CENTER);
        editEmployee.setHorizontalTextPosition(AbstractButton.LEADING);
        editEmployee.setMnemonic(KeyEvent.VK_0);
        editEmployee.setActionCommand("edit employee");

        selectEmployee = new JButton(SELECT_EMPLOYEE);
        selectEmployee.setVerticalTextPosition(AbstractButton.CENTER);
        selectEmployee.setHorizontalTextPosition(AbstractButton.LEADING);
        selectEmployee.setMnemonic(KeyEvent.VK_1);
        selectEmployee.setActionCommand("select employee");

        //Action listener
        createLeave.addActionListener(this);
        editEmployee.addActionListener(this);
        selectEmployee.addActionListener(this);


        JScrollPane employeeListScrollPane = new JScrollPane(employees);
        JScrollPane leaveListScrollPane = new JScrollPane(leave);

        //rightHalf.add(employeeInformation());
        //rightHalf.add(employeeLeaveListView());
        //rightHalf.add(createLeaveButton());

        JComponent employeeInfo = employeeInformation(selectedEmployee);

        JComponent leaveInfo = new JPanel();
        leaveInfo.setBorder(BorderFactory.createTitledBorder("Leave Information"));
        leaveInfo.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        leaveInfo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);



        //Constraints for Create leave button
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        leaveInfo.add(createLeave,c);

        //Constraints for employee info
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        leaveInfo.add(employeeInfo,c);

        //Constraints for leaveListScrollPane
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 255;
        leaveInfo.add(leaveListScrollPane,c);

        //For createEmployeeButton
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 0;
        employeeList.add(createEmployee,c);

        //For selecte employee button
        c.gridx = 1;
        employeeList.add(selectEmployee,c);

        //For Delete employee button
        c.gridx = 2;
       // employeeList.add();

        //For Edit employee button
        c.gridx = 3;
        employeeList.add(editEmployee,c);

        //For the list of Employees
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 345;
        c.gridy = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        employeeList.add(employeeListScrollPane,c);

        //For Halves
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 0;
        c.ipadx = 0;
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(employeeList,c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(leaveInfo,c);

    }

    //EFFECTS: create and show GUI
    /**
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MAP - Attendance Program for Mid-Sized Business");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add Contents
        frame.add(new GUI());

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }
     */

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MAP - Attendance Program for Mid-Sized Business");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add Contents
        JComponent newContentPane = new GUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }

    private static Component createNewEmployeeButton() {

        return null;
    }

    private static Component employeeLeaveListView() {

        return null;
    }

    private static void newLeaveLabel() {

    }

    private static void datePicker() {

    }

    private static void typeComboBox() {

    }

    private static void commentTextField() {

    }

    private static Component createLeaveButton() {

        return null;
    }

    private static void createLeavePopup() {

    }

    private static void passwordField() {

    }

    private static JPanel employeeInformation(Employee selectedEmployee) {
        JLabel employeeSickLeave = new JLabel("Remaining Sick Leave: " + selectedEmployee.getSickLeaveLeft());
        JLabel employeeHolidayLeave = new JLabel("Remaining Holiday: " + selectedEmployee.getHolidayLeft());
        JLabel employeeName = new JLabel("Name: " + selectedEmployee.getName());
        JLabel employeeWorkHours = new JLabel("Work Hours Per Day: " + selectedEmployee.getWorkHours());
        JLabel employeeSupervisor = new JLabel("Supervisor: " + selectedEmployee.getSupervisor());
        JLabel employeeDepartment = new JLabel("Department: " + selectedEmployee.getDepartment());
        JLabel employeeYearsOfService = new JLabel("Years of Service: " + selectedEmployee.getYearsOfService());

        JPanel employeeInfo = new JPanel();
        employeeInfo.setBorder(BorderFactory.createTitledBorder(EMPLOYEE_INFO));
        employeeInfo.setLayout(new GridLayout(0,2));

        employeeInfo.add(employeeName);
        employeeInfo.add(employeeWorkHours);
        employeeInfo.add(employeeSupervisor);
        employeeInfo.add(employeeDepartment);
        employeeInfo.add(employeeYearsOfService);
        employeeInfo.add(employeeSickLeave);
        employeeInfo.add(employeeHolidayLeave);

        return employeeInfo;
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
