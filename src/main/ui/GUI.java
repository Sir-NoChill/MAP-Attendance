package ui;

import exceptions.EmployeeNotFoundException;
import exceptions.InvalidLeaveAmountException;
import exceptions.RoleNotFoundException;
import model.Employee;
import model.State;
import model.leave.Leave;
import model.leave.LeaveType;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

import static java.awt.GridBagConstraints.BOTH;
import static java.lang.Double.parseDouble;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.SwingConstants.HORIZONTAL;
import static javax.swing.SwingConstants.VERTICAL;
import javax.swing.ImageIcon;
import static model.Employee.stringToRole;
import static model.Role.LEGAL_ASSISTANT;

public class GUI extends JPanel
        implements
        ListSelectionListener,
        ActionListener {

    private State state = new State(LocalDate.now());

    protected ImageIcon icon = new ImageIcon("components/images/Logovs.png", "logo");

    private JScrollPane employeeListScrollPane;
    private JScrollPane leaveListScrollPane;

    private JList employees;
    private JList leave;

    private static JFrame frame;

    private JPanel employeeInfoFrame;

    private JPanel leaveViewPane;
    private JPanel leaveCreationPane;
    private JComboBox<String> leaveTypeBox;
    private JTextField leaveDateTextField;
    private JTextField leaveCommentsTextField;

    private JPanel employeeCreationPane;
    private JTextField employeeNameField;
    private JTextField employeeAnniversaryField;
    private JComboBox<String> employeeRoleComboBox;
    private JTextField employeeWorkHoursField;
    private JTextField employeeDepartmentField;
    private JTextField employeeSupervisorField;
    protected JButton createEmployeeButton;
    private static final int CREATE_EMPLOYEE_PANE_ROWS = 20;
    private static final int CREATE_EMPLOYEE_PANE_COLUMNS = 2;

    private JPanel editEmployeePane;
    private JTextField employeeEditNameField;
    private JComboBox<String> employeeFieldChosen;
    private JTextField employeeEditFieldField;
    protected JButton editEmployeeButton;
    private static final int EDIT_EMPLOYEE_PANE_ROWS = 20;
    private static final int EDIT_EMPLOYEE_PANE_COLUMNS = 2;

    private JTabbedPane optionsPanes;

    private DefaultListModel listModelEmployees;
    private DefaultListModel listModelLeave;

    protected JButton createLeaveButton;
    protected JButton selectEmployeeButton;
    private static final String CREATE_LEAVE = "Create New Leave";
    private static final String CREATE_EMPLOYEE = "New Employee";
    private static final String EDIT_EMPLOYEE = "Edit Employee";
    private static final String EMPLOYEE_INFO = "Employee Info";
    private static final String SELECT_EMPLOYEE = "Select Employee";

    JLabel employeeYearsOfService = new JLabel();
    JLabel employeeSickLeave = new JLabel();
    JLabel employeeHolidayLeave = new JLabel();
    JLabel employeeName = new JLabel();
    JLabel employeeWorkHours = new JLabel();
    JLabel employeeSupervisor = new JLabel();
    JLabel employeeDepartment = new JLabel();

    //MENU BAR BUILDING
    private ItemListener itemListener;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem save;
    private JMenuItem load;
    private JMenu info;
    private JMenuItem credits;

    //File loading and Saving system
    private final JFileChooser fileChooser;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    //EFFECTS: Generates a GUI
    public GUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Select Employee Button
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.fill = HORIZONTAL;
        add(generateSelectEmployeeButton(),c);

        //Employee View Pane
        c.gridheight = 6;
        c.gridy = 1;
        c.ipadx = 120;
        c.fill = BOTH;
        add(generateEmployeeListScrollPane(),c);

        //Employee Info Pane
        c.gridheight = 3;
        c.gridy = 7;
        c.ipadx = 0;
        c.fill = HORIZONTAL;
        add(generateEmployeeInfoFrame(),c);

        placeOptionsPanes(c);
        add(generateOptionsPanes(),c);

        fileChooser = new JFileChooser();

        setMinimumSize(new Dimension(1000,1000));
    }

    //EFFECTS Places the Right hand option pane in the GUI
    private void placeOptionsPanes(GridBagConstraints c) { //IDEAS make this more general and replace
                                                           // GUI building with it
        //OptionsPane
        c.gridwidth = 1;
        c.gridheight = 11;
        c.gridx = 2;
        c.gridy = 0;
        c.fill = VERTICAL;
    }

    //EFFECTS: creates the menu bar
    private JMenuBar generateMenuBar() {
        menuBar = new JMenuBar();

        menuBar.add(generateFileMenu());
        menuBar.add(generateInfoMenu());

        return menuBar;
    }

    //EFFECTS; generates the "file" menu item
    private JMenu generateFileMenu() {
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(generateSaveMenuItem());
        fileMenu.add(generateLoadMenuItem());

        return fileMenu;
    }

    //EFFECTS: generates the "info" menu item
    private JMenu generateInfoMenu() {
        info = new JMenu("Info");
        info.setMnemonic(KeyEvent.VK_I);

        info.add(generateCreditMenuItem());
        return info;
    }

    //EFFECTS: Generates the save tab in the file menu
    private JMenuItem generateSaveMenuItem() {
        save = new JMenuItem("Save");

        return save;
    }

    //EFFECTS: generate the load tab in the file menu
    private JMenuItem generateLoadMenuItem() {
        load = new JMenuItem("Load");
        load.addActionListener(this);
        load.setActionCommand("load");

        return load;
    }

    //EFFECTS adds the credit item to the info menu
    private JMenuItem generateCreditMenuItem() {
        credits = new JMenuItem("Credits");
        credits.addActionListener(this);
        credits.setActionCommand("Credits");

        return credits;
    }

    //EFFECTS: generates the employee list scroll pane on the right side of the GUI
    private JScrollPane generateEmployeeListScrollPane() {
        //creating list of employees
        listModelEmployees = new DefaultListModel();
        populateEmployeeListModel();

        employees = new JList(listModelEmployees);
        employees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employees.setSelectedIndex(0);
        employees.addListSelectionListener(this);

        employeeListScrollPane = new JScrollPane(
                employees,
                VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        employeeListScrollPane.setMinimumSize(new Dimension(200,500));

        return employeeListScrollPane;
    }

    //MODIFIES: this.listModelEmployees
    //EFFECTS: clears and refills the list of employees
    //         for visibility of new employees and avoiding duplocates
    private void refreshEmployeeListView() {
        listModelEmployees.removeAllElements();
        populateEmployeeListModel();
        employees.setModel(listModelEmployees);
    }

    //MODIFIES: this.listmodelLeave
    //EFFECTS: clears and refills the list of Leave
    //         for visibility of new leave and avoiding duplication
    private void refreshLeaveListView() {
        listModelLeave.removeAllElements();
        populateLeaveListModel();
        leave.setModel(listModelLeave);
    }

    //EFFECTS: creates the employee info  frame that sits below the employee list
    private JPanel generateEmployeeInfoFrame() {
        if (employeeSelectedBool()) {
            Employee selectedEmployee = getSelectedEmployee();
            setEmployeeInformation(selectedEmployee);
        } else {
            setEmployeeInformationDefault();
        }

        employeeInfoFrame = new JPanel();
        employeeInfoFrame.setBorder(BorderFactory.createTitledBorder(EMPLOYEE_INFO));
        employeeInfoFrame.setLayout(new GridLayout(0,2));

        employeeInfoFrame.add(employeeName);
        employeeInfoFrame.add(employeeWorkHours);
        employeeInfoFrame.add(employeeSupervisor);
        employeeInfoFrame.add(employeeDepartment);
        employeeInfoFrame.add(employeeYearsOfService);
        employeeInfoFrame.add(employeeSickLeave);
        employeeInfoFrame.add(employeeHolidayLeave);

        return employeeInfoFrame;
    }

    //EFFECTS: generates the employee options pane
    private JTabbedPane generateOptionsPanes() {
        optionsPanes = new JTabbedPane();

        optionsPanes.addTab("Leave Information",generateLeaveViewPane());
        optionsPanes.addTab("Create Employee",generateEmployeeCreationPane());
        optionsPanes.addTab("Edit Employee",generateEditEmployeePane());

        optionsPanes.setMnemonicAt(0,KeyEvent.VK_Z);
        optionsPanes.setMnemonicAt(1,KeyEvent.VK_C);
        optionsPanes.setMnemonicAt(2, KeyEvent.VK_X);

        optionsPanes.setMinimumSize(new Dimension(300,600));

        return optionsPanes;
    }

    //effects: generates the leaveView pane that sits inside the tabbed pane
    private JPanel generateLeaveViewPane() {
        leaveViewPane = new JPanel();
        leaveViewPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Leave List
        c.gridheight = 4;
        c.gridwidth = 1;
        c.gridy = 0;
        c.gridx = 0;
        leaveViewPane.add(generateLeaveListScrollPane(),c);

        //LeaveCreationPane
        c.gridheight = 1;
        c.gridy = 4;
        leaveViewPane.add(generateLeaveCreationPane(),c);

        return leaveViewPane;
    }

    //effects: creates the employee creation pane
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private JPanel generateEmployeeCreationPane() {
        employeeCreationPane = new JPanel();
        employeeCreationPane.setLayout(new GridLayout(CREATE_EMPLOYEE_PANE_ROWS,CREATE_EMPLOYEE_PANE_COLUMNS));

        JLabel name = new JLabel("Name:");
        employeeCreationPane.add(name);

        employeeNameField = new JTextField(30);
        employeeCreationPane.add(employeeNameField);

        JLabel anniversary = new JLabel("Anniversary:");
        employeeCreationPane.add(anniversary);

        employeeAnniversaryField = new JTextField(10);
        employeeCreationPane.add(employeeAnniversaryField);

        JLabel role = new JLabel("Role:");
        employeeCreationPane.add(role);

        String[] roles = { "Human Resources", "Accountant", "Legal Assistant" };
        employeeRoleComboBox = new JComboBox<>(roles);
        employeeCreationPane.add(employeeRoleComboBox);

        JLabel workHours = new JLabel("Work Hours:");
        employeeCreationPane.add(workHours);

        employeeWorkHoursField = new JTextField(3);
        employeeCreationPane.add(employeeWorkHoursField);

        JLabel supervisor = new JLabel("Supervisor:");
        employeeCreationPane.add(supervisor);

        employeeSupervisorField = new JTextField(30);
        employeeCreationPane.add(employeeSupervisorField);

        JLabel departmentLabel = new JLabel("Department");
        employeeCreationPane.add(departmentLabel);

        employeeDepartmentField = new JTextField(30);
        employeeCreationPane.add(employeeDepartmentField);

        employeeCreationPane.add(new JLabel());

        employeeCreationPane.add(generateCreateEmployeeButton());

        for (int i = 0; i < (CREATE_EMPLOYEE_PANE_ROWS * CREATE_EMPLOYEE_PANE_COLUMNS - 14); i++) {
            employeeCreationPane.add(new JLabel());
        }

        return employeeCreationPane;
    }

    //effects: generates the edit employee panes
    private JPanel generateEditEmployeePane() {
        editEmployeePane = new JPanel();
        editEmployeePane.setLayout(new GridLayout(EDIT_EMPLOYEE_PANE_ROWS,EDIT_EMPLOYEE_PANE_COLUMNS));

        String[] fields = { "Name", "Anniversary", "Role", "Supervisor",
                "Department", "Holiday Left", "Sick Leave Left" };
        employeeFieldChosen = new JComboBox<>(fields);
        employeeFieldChosen.setToolTipText("Select field to edit");
        editEmployeePane.add(employeeFieldChosen);

        employeeEditFieldField = new JTextField(30);
        editEmployeePane.add(employeeEditFieldField);

        editEmployeePane.add(new JLabel());

        editEmployeePane.add(generateEditEmployeeButton());

        for (int i = 0; i < (EDIT_EMPLOYEE_PANE_ROWS * EDIT_EMPLOYEE_PANE_COLUMNS - 4); i++) {
            editEmployeePane.add(new JLabel());
        }

        return editEmployeePane;
    }

    //EFFECTS: generates the leave list in the leaveView Scroll pane
    private JScrollPane generateLeaveListScrollPane() {
        listModelLeave = new DefaultListModel();
        populateLeaveListModel();

        leave = new JList(listModelLeave);
        leave.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leave.addListSelectionListener(this);

        leaveListScrollPane = new JScrollPane(
                leave,
                VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        leaveListScrollPane.setPreferredSize(new Dimension(200,400));
        return leaveListScrollPane;
    }

    //EFFECTS: generates the leave creation frame that sits in the leave view pane
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private JPanel generateLeaveCreationPane() { //IDEAS use teh place method (like for GUI) and refactor
        JLabel leaveDateLabel = new JLabel("Date:");
        JLabel leaveCommentsLabel = new JLabel("Comments:");

        leaveCreationPane = new JPanel();
        leaveCreationPane.setBorder(BorderFactory.createTitledBorder("Create Leave"));
        leaveCreationPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Leave Type Combo box
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        leaveCreationPane.add(generateLeaveTypeComboBox(),c);

        //LeaveDateLabel
        c.gridx = 2;
        c.gridwidth = 1;
        leaveCreationPane.add(leaveDateLabel,c);

        //LeaveCommentsLabel
        c.gridy = 1;
        leaveCreationPane.add(leaveCommentsLabel,c);

        //LeaveDateTextField
        c.gridy = 0;
        c.gridx = 3;
        c.gridwidth = 3;
        leaveCreationPane.add(generateLeaveDateTextField(),c); //FIXME this isn't showing up

        //LeaveCommentsTextField
        c.gridy = 1;
        leaveCreationPane.add(generateLeaveCommentsTextField(),c);

        //LeaveCreateButton
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 6;
        leaveCreationPane.add(generateCreateLeaveButton(),c);

        return leaveCreationPane;
    }

    //EFFECTS: generates the leave type combo bos insid the leave creation pane
    private JComboBox<String> generateLeaveTypeComboBox() {
        String[] leaveTypeStrings = { "Select Type", "Holiday", "Sick" };
        leaveTypeBox = new JComboBox<>(leaveTypeStrings);

        return leaveTypeBox;
    }

    //EFFECTS generates the leave date field that sits in the Leave Creation pane
    //IDEAS should be changed to a java date picker
    private JTextField generateLeaveDateTextField() {
        leaveDateTextField = new JTextField(10); //text obtained by getText() (returns String)
        return leaveDateTextField;
    }

    //EFFECTS generates the comments field that sits inside the leave view editor
    private JTextField generateLeaveCommentsTextField() {
        leaveCommentsTextField = new JTextField(10);
        return leaveCommentsTextField;
    }

    //EFFECTS: generates the select employee button at the top of the program.
    //IDEAS: should be switched witha dynamically changing selection listener on the list
    private JButton generateSelectEmployeeButton() {
        selectEmployeeButton = new JButton(SELECT_EMPLOYEE);
        selectEmployeeButton.setVerticalTextPosition(AbstractButton.CENTER);
        selectEmployeeButton.setHorizontalTextPosition(AbstractButton.LEADING);
        selectEmployeeButton.setMnemonic(KeyEvent.VK_E);
        selectEmployeeButton.setActionCommand("select employee");
        selectEmployeeButton.setToolTipText("(Alt + E) Select an employee");
        selectEmployeeButton.addActionListener(this);

        return selectEmployeeButton;
    }

    //EFFECTS: generates the leaveCreate button in the leave view pane
    private JButton generateCreateLeaveButton() {
        createLeaveButton = new JButton(CREATE_LEAVE);
        createLeaveButton.setVerticalTextPosition(AbstractButton.CENTER);
        createLeaveButton.setHorizontalTextPosition(AbstractButton.LEADING);
        createLeaveButton.setMnemonic(KeyEvent.VK_A);
        createLeaveButton.setActionCommand("create leave");
        createLeaveButton.setToolTipText("(Alt + A) add leave to the selected employee");
        createLeaveButton.addActionListener(this);

        return createLeaveButton;
    }

    //EFFECTS: generates the create employee button in the employeecreation pane
    private JButton generateCreateEmployeeButton() {
        createEmployeeButton = new JButton(CREATE_EMPLOYEE);
        createEmployeeButton.setVerticalTextPosition(AbstractButton.CENTER);
        createEmployeeButton.setHorizontalTextPosition(AbstractButton.LEADING);
        createEmployeeButton.setMnemonic(KeyEvent.VK_S);
        createEmployeeButton.setActionCommand("create employee");
        createEmployeeButton.setToolTipText("(Alt + s) add leave to the selected employee");
        createEmployeeButton.addActionListener(this);

        return createEmployeeButton;
    }

    //EFFECTS: generates the edit employee button in the edit employee frame
    private JButton generateEditEmployeeButton() {
        editEmployeeButton = new JButton(EDIT_EMPLOYEE);
        editEmployeeButton.setVerticalTextPosition(AbstractButton.CENTER);
        editEmployeeButton.setHorizontalTextPosition(AbstractButton.LEADING);
        editEmployeeButton.setMnemonic(KeyEvent.VK_D);
        editEmployeeButton.setActionCommand("edit employee");
        editEmployeeButton.setToolTipText("(Alt + D) add leave to the selected employee");
        editEmployeeButton.addActionListener(this);

        return editEmployeeButton;
    }

    //EFFECTS: Instantiates the GUI in a frame on the computer screen
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("MAP - Attendance Program for Mid-Sized Business");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add Contents
        GUI newContentPane = new GUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.setMinimumSize(new Dimension(700,700));
        frame.setMaximumSize(new Dimension(700,700));
        frame.setPreferredSize(new Dimension(700,700));

        //Setting up the menu bar
        frame.setJMenuBar(newContentPane.generateMenuBar());

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }

    //Stack Exchange
    //EFFECTS: rounds a double to a given number of decimal places
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //EFFECTS: the combination setter method for the information stored in the employeeInformation pane
    private void setEmployeeInformation(Employee selectedEmployee) {
        employeeSickLeave.setText("Remaining Sick Leave: " + round(selectedEmployee.getSickLeaveLeft(),2));
        employeeHolidayLeave.setText("Remaining Holiday: " + round(selectedEmployee.getHolidayLeft(),2));
        employeeName.setText("Name: " + selectedEmployee.getName());
        employeeWorkHours.setText("Work Hours Per Day: " + selectedEmployee.getWorkHours());
        employeeSupervisor.setText("Supervisor: " + selectedEmployee.getSupervisor());
        employeeDepartment.setText("Department: " + selectedEmployee.getDepartment());
        employeeYearsOfService.setText("Years of Service: " + selectedEmployee.getYearsOfService());
    }

    //EFFECTS: the information to be displayed in the absence of a selected employee
    private void setEmployeeInformationDefault() {
        employeeSickLeave.setText("Remaining Sick Leave: None");
        employeeHolidayLeave.setText("Remaining Holiday: None");
        employeeName.setText("Name: None");
        employeeWorkHours.setText("Work Hours Per Day: None");
        employeeSupervisor.setText("Supervisor: None");
        employeeDepartment.setText("Department: None");
        employeeYearsOfService.setText("Years of Service: None");
    }

    //MODIFIES: this.leaveListModel
    //EFFECTS: iterates through the leave in an employee and populates
    private void populateLeaveListModel() {
        boolean selected = employeeSelectedBool();
        if (selected) {
            Employee selectedEmployee = getSelectedEmployee();
            for (Leave leave1 : selectedEmployee.getLeaveTaken()) {
                String toAdd = leave1.getDateOfLeave().toString() + "  "
                        + leave1.getLeaveType().toString().toLowerCase(Locale.ROOT);
                listModelLeave.addElement(toAdd);
            }
        }
    }

    //MODIFIES: this.employeeListModel
    //EFFECTS: iterates through the employees in this.state and populates the employeeListModel with them
    private void populateEmployeeListModel() {
        for (Employee e : state.getSetOfEmployees()) {
            listModelEmployees.addElement(e.getName());
        }
    }

    //PROGRAM ENTRY POINT
    //EFFECTS: entry point for the code
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    //EFFECTS: clears all text fields in the program
    private void clearFields() {
        employeeNameField.setText("");
        employeeAnniversaryField.setText("");
        employeeSupervisorField.setText("");
        employeeDepartmentField.setText("");
        employeeWorkHoursField.setText("");
        leaveDateTextField.setText("");
        leaveCommentsTextField.setText("");
    }

    //EFFECTS: nothing right now, to be updated
    @Override
    public void valueChanged(ListSelectionEvent e) {


    }

    //EFFECTS: Master method for button effects
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().toLowerCase(Locale.ROOT)) {
            case "select employee":
                setEmployeeInformation(Objects.requireNonNull(getSelectedEmployee()));
                refreshLeaveListView();
                break;
            case "create employee":
                createEmployeeGUI();
                break;
            case "edit employee":
                editEmployeeGUI();
                break;
            case "delete employee": //TODO should be implemented at some point
                employees.remove(employees.getSelectedIndex());
                refreshEmployeeListView();
                break;
            case "create leave":
                createLeaveGUI();
                break;
            case "load":
                loadFileGUI();
                break;
            case "save":
                saveFileGUI();
                break;
            case "credits":
                displayCreditsGUI();
                break;
        }
    }

    //MODIFIES: this.state
    //EFFECTS: adds a new employee based on user input in "employeeXxxx" textFields
    private void createEmployeeGUI() {
        try {
            Employee newEmployee = new Employee(
                    employeeAnniversaryField.getText(),
                    stringToRole(employeeRoleComboBox.getSelectedItem().toString()),
                    employeeNameField.getText(),
                    parseDouble(employeeWorkHoursField.getText()),
                    employeeSupervisorField.getText(),
                    employeeDepartmentField.getText());
            this.state.addEmployee(newEmployee);
            clearFields();
            JOptionPane.showMessageDialog(GUI.this, "Employee created");
        } catch (RoleNotFoundException ex) {
            JOptionPane.showMessageDialog(GUI.this, "Something went wrong, sorry");
            System.out.println("Failed to select employee");
        }
        refreshEmployeeListView();
    }

    //MODIFIES: this.state
    //EFFECTS: edits the selected employee with user input
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void editEmployeeGUI() {
        if (employeeSelectedBool()) {
            String s = employeeEditFieldField.getText();
            Employee em = getSelectedEmployee();
            switch ((String) Objects.requireNonNull(employeeFieldChosen.getSelectedItem())) {
                case "Name":
                    em.setName(s);
                    break;
                case "Anniversary":
                    em.setAnniversary(s);
                    break;
                case "Role":
                    changeRoleGUI(s, em);
                    break;
                case "Supervisor":
                    em.setSupervisor(s);
                    break;
                case "Department":
                    em.setDepartment(s);
                    break;
                case "Holiday Left":
                    em.setHolidayLeft(parseDouble(s));
                    break;
                case "Sick Leave Left":
                    em.setSickLeaveLeft(parseDouble(s));
                    break;
                case "Work Hours":
                    em.setWorkHours(parseDouble(s));
                    break;
            }
            JOptionPane.showMessageDialog(GUI.this, "Selected Employee has been edited");
            clearFields();
            setEmployeeInformation(em);
            refreshEmployeeListView();
        }
    }

    //MODIFIES: this.state
    //EFFECTS: changes the role of an employee in the state
    private void changeRoleGUI(String s, Employee em) {
        try {
            em.setRole(stringToRole(s));
            return;
        } catch (RoleNotFoundException ex) {
            // do nothing
            return;
        }
    }

    //MODIFIES: an employee in this.state
    //EFFECTS: creates an instance of leave in a given employee
    private void createLeaveGUI() {
        if (employeeSelectedBool()) {
            LeaveType l;
            String leaveDate = leaveDateTextField.getText();
            String leaveComments = leaveCommentsTextField.getText();
            l = getLeaveType();
            if (l == null) {
                return;
            }
            try {
                regularLeaveTakingGUI(l, leaveDate, leaveComments);
            } catch (InvalidLeaveAmountException ex) {
                int n = JOptionPane.showConfirmDialog(GUI.this,
                        "That employee has no remaining leave of that type, would you like to override?");
                if (n == YES_OPTION) {
                    overrideLeaveTakingGUI(l, leaveDate, leaveComments);
                } else {
                    //do nothing
                    JOptionPane.showMessageDialog(GUI.this, "Leave Not Added");
                }
            }
            refreshLeaveListView();
        }
    }

    //EFFECTS: returns the type of leave based on the leave type combo box
    private LeaveType getLeaveType() {
        LeaveType l;
        if (leaveTypeBox.getSelectedItem().toString().equals("Holiday")) {
            l = LeaveType.HOLIDAY;
        } else if (leaveTypeBox.getSelectedItem().toString().equals("Sick")) {
            l = LeaveType.SICK;
        } else {
            return null;
        }
        return l;
    }

    //MODIFIES: an employee in this.state
    //EFFECTS: same functionalit as regularLeaveTakingGUI, except overrides the invalid leave amount exception
    private void overrideLeaveTakingGUI(LeaveType l, String leaveDate, String leaveComments) {
        getSelectedEmployee().addLeaveToEmployee(
                leaveDate,
                l,
                leaveComments,
                getSelectedEmployee().getWorkHours() * 4);
        refreshLeaveListView();
        JOptionPane.showMessageDialog(GUI.this, "Leave Added");
    }

    //MODIFIES: an employee in this.state
    //EFFECTS: creates an instance of leave for an employee in the state
    private void regularLeaveTakingGUI(LeaveType l, String leaveDate, String leaveComments)
            throws InvalidLeaveAmountException {
        getSelectedEmployee().takeLeave(
                leaveDate,
                l,
                leaveComments,
                getSelectedEmployee().getWorkHours() * 4);
        refreshLeaveListView();
        JOptionPane.showMessageDialog(GUI.this, "Leave Added");
    }

    //MODIFIES: this
    //EFFECTS: loads a state from a file
    private void loadFileGUI() {
        int returnValLoad = fileChooser.showOpenDialog(GUI.this);

        if (returnValLoad == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            jsonReader = new JsonReader(file.getPath());
            String currentDate = state.getCurrentDate().toString();
            try {
                this.state = jsonReader.read();
                refreshEmployeeListView();
                refreshLeaveListView();
                state.update(LocalDate.now());

                JOptionPane.showMessageDialog(GUI.this,"File Loaded "
                        + "and updated from " + currentDate + " to " + LocalDate.now().toString());
            } catch (IOException ex) {
                System.out.println("Unable to read file");
                ex.printStackTrace();
            }
        }
    }

    //EFFECTS: saves a state to file
    private void saveFileGUI() {
        int returnValSave = fileChooser.showSaveDialog(GUI.this);

        if (returnValSave == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            jsonWriter = new JsonWriter(file.getPath());

            jsonWriter.write(this.state);
            JOptionPane.showMessageDialog(GUI.this,"File Saved to: " + file.getAbsolutePath());
        }
    }

    //EFFECTS: returns if an employee is selected
    private boolean employeeSelectedBool() {
        return employees.getSelectedValue() != null;
    }

    //EFFECTS: returns the selected employee
    private Employee getSelectedEmployee() {
        try {
            return state.searchEmployees(employees.getSelectedValue().toString());
        } catch (EmployeeNotFoundException e) {
            return null;
        }
    }

    //EFFECTS: displays the program credits in the info pane
    private void displayCreditsGUI() {
        String message = "Created by Ayrton Chilibeck, for my wonderful mother "
                + "who serves as an inspiration for my work and person.";
        JOptionPane.showMessageDialog(GUI.this,message,"Credits",1,icon);
    }
}
