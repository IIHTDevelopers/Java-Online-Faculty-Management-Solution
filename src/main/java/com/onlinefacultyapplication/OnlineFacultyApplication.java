package com.onlinefacultyapplication;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.jboss.jandex.Main;

import com.onlinefacultyapplication.model.Department;
import com.onlinefacultyapplication.model.Teacher;
import com.onlinefacultyapplication.repository.DepartmentDAO;
import com.onlinefacultyapplication.repository.DepartmentDAOImpl;
import com.onlinefacultyapplication.repository.TeacherDAO;
import com.onlinefacultyapplication.repository.TeacherDAOImpl;

public class OnlineFacultyApplication {
	private static DepartmentDAO departmentDAO;
	private static TeacherDAO teacherDAO;
	private static Scanner scanner;

	public static void main(String[] args) {
		Properties properties = new Properties();

		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(inputStream);

			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");

			createDatabaseIfNotExists(properties);
			createTablesIfNotExists(properties);

			departmentDAO = new DepartmentDAOImpl(url, username, password);
			teacherDAO = new TeacherDAOImpl(url, username, password);

			scanner = new Scanner(System.in);
			boolean exit = false;

			while (!exit) {
				printMenu();
				int choice = getUserChoice();

				switch (choice) {
				case 1:
					createDepartment(scanner);
					break;
				case 2:
					createTeacher(scanner);
					break;
				case 3:
					getDepartmentById(scanner);
					break;
				case 4:
					getTeacherById(scanner);
					break;
				case 5:
					getAllDepartments();
					break;
				case 6:
					getAllTeachers();
					break;
				case 7:
					updateDepartment(scanner);
					break;
				case 8:
					updateTeacher(scanner);
					break;
				case 9:
					deleteDepartment(scanner);
					break;
				case 10:
					deleteTeacher(scanner);
					break;
				case 11:
					searchTeachersByName(scanner);
					break;
				case 12:
					searchTeachersBySubject(scanner);
					break;
				case 13:
					searchDepartmentsByName(scanner);
					break;
				case 0:
					System.out.println("Exiting program...");
					scanner.close();
					System.exit(0);
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	private static int getUserChoice() {
		System.out.print("Enter your choice: ");
		return scanner.nextInt();
	}

	private static void printMenu() {
		System.out.println("1. Create Department");
		System.out.println("2. Create Teacher");
		System.out.println("3. Get Department by ID");
		System.out.println("4. Get Teacher by ID");
		System.out.println("5. Get All Departments");
		System.out.println("6. Get All Teachers");
		System.out.println("7. Update Department");
		System.out.println("8. Update Teacher");
		System.out.println("9. Delete Department");
		System.out.println("10. Delete Teacher");
		System.out.println("11. Search Teachers by Name");
		System.out.println("12. Search Teachers by Subject");
		System.out.println("13. Search Departments by Name");
		System.out.println("0. Exit");
		System.out.print("Enter your choice: ");
	}

	public static void createDatabaseIfNotExists(Properties properties) {
		String url = properties.getProperty("jdbc.url");
		String username = properties.getProperty("jdbc.username");
		String password = properties.getProperty("jdbc.password");
		String databaseName = properties.getProperty("jdbc.database");

		try (Connection connection = DriverManager.getConnection(url, username, password);
				Statement statement = connection.createStatement()) {
			String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
			statement.executeUpdate(createDatabaseSql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void createTablesIfNotExists(Properties properties) {
		String url = properties.getProperty("jdbc.url");
		String username = properties.getProperty("jdbc.username");
		String password = properties.getProperty("jdbc.password");
		String databaseName = properties.getProperty("jdbc.database");

		String connectionString = url + "/" + databaseName;

		try (Connection connection = DriverManager.getConnection(url, username, password);
				Statement statement = connection.createStatement()) {

			String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
			statement.executeUpdate(createDatabaseSql);

			String useDatabaseSql = "USE " + databaseName;
			statement.executeUpdate(useDatabaseSql);

			String createDepartmentTableSql = "CREATE TABLE IF NOT EXISTS department ("
					+ "id INT PRIMARY KEY AUTO_INCREMENT," + "name VARCHAR(10) NOT NULL)";

			statement.executeUpdate(createDepartmentTableSql);

			String createTeacherTableSql = "CREATE TABLE IF NOT EXISTS teacher (" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "name VARCHAR(10) NOT NULL," + "subject VARCHAR(255) NOT NULL,"
					+ "designation VARCHAR(20) NOT NULL," + "department_id INT NOT NULL,"
					+ "FOREIGN KEY (department_id) REFERENCES department(id))";

			statement.executeUpdate(createTeacherTableSql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void createDepartment(Scanner scanner) {
		scanner.nextLine();
		System.out.print("Enter department name: ");
		String name = scanner.nextLine();
		Department department = new Department();
		department.setName(name);
		department = departmentDAO.createDepartment(department);
		if (department != null) {
			System.out.println("Department created successfully with ID: " + department.getId());
		} else {
			System.out.println("Failed to create department.");
		}
	}

	private static void createTeacher(Scanner scanner) {
		scanner.nextLine();
		System.out.print("Enter teacher name: ");
		String name = scanner.nextLine();
		System.out.print("Enter subject: ");
		String subject = scanner.nextLine();
		System.out.print("Enter designation (Assistant Professor/Professor/Lecturer): ");
		String designation = scanner.nextLine();
		System.out.print("Enter department ID: ");
		int departmentId = scanner.nextInt();

		Department department = departmentDAO.getDepartmentById(departmentId);
		if (department == null) {
			System.out.println("Department with ID " + departmentId + " does not exist.");
			return;
		}

		Teacher teacher = new Teacher();
		teacher.setName(name);
		teacher.setSubject(subject);
		teacher.setDesignation(designation);
		teacher.setDepartment(departmentId);

		teacher = teacherDAO.createTeacher(teacher);
		if (teacher != null) {
			System.out.println("Teacher created successfully with ID: " + teacher.getId());
		} else {
			System.out.println("Failed to create teacher.");
		}
	}

	private static void getDepartmentById(Scanner scanner) {
		System.out.print("Enter department ID: ");
		int id = scanner.nextInt();
		Department department = departmentDAO.getDepartmentById(id);
		if (department != null) {
			System.out.println("Department details:");
			System.out.println("ID: " + department.getId());
			System.out.println("Name: " + department.getName());
			System.out.println("Teachers: " + department.getTeachers());
		} else {
			System.out.println("Department with ID " + id + " does not exist.");
		}
	}

	private static void getTeacherById(Scanner scanner) {
		System.out.print("Enter teacher ID: ");
		int id = scanner.nextInt();
		Teacher teacher = teacherDAO.getTeacherById(id);
		if (teacher != null) {
			System.out.println("Teacher details:");
			System.out.println("ID: " + teacher.getId());
			System.out.println("Name: " + teacher.getName());
			System.out.println("Subject: " + teacher.getSubject());
			System.out.println("Designation: " + teacher.getDesignation());
		} else {
			System.out.println("Teacher with ID " + id + " does not exist.");
		}
	}

	private static void getAllDepartments() {
		List<Department> departments = departmentDAO.getAllDepartments();
		System.out.println("Departments:");
		for (Department department : departments) {
			System.out.println("ID: " + department.getId() + ", Name: " + department.getName() + ", Teachers: "
					+ department.getTeachers());
		}
	}

	private static void getAllTeachers() {
		List<Teacher> teachers = teacherDAO.getAllTeachers();
		System.out.println("Teachers:");
		for (Teacher teacher : teachers) {
			System.out.println("ID: " + teacher.getId() + ", Name: " + teacher.getName() + ", Subject: "
					+ teacher.getSubject() + ", Designation: " + teacher.getDesignation());
		}
	}

	private static void updateDepartment(Scanner scanner) {
		System.out.print("Enter department ID: ");
		int id = scanner.nextInt();
		Department department = departmentDAO.getDepartmentById(id);
		if (department != null) {
			scanner.nextLine();
			System.out.print("Enter new department name: ");
			String name = scanner.nextLine();
			department.setName(name);
			departmentDAO.updateDepartment(department);
			System.out.println("Department updated successfully.");
		} else {
			System.out.println("Department with ID " + id + " does not exist.");
		}
	}

	private static void updateTeacher(Scanner scanner) {
		System.out.print("Enter teacher ID: ");
		int id = scanner.nextInt();
		Teacher teacher = teacherDAO.getTeacherById(id);
		if (teacher != null) {
			scanner.nextLine();
			System.out.print("Enter new teacher name: ");
			String name = scanner.nextLine();
			System.out.print("Enter new subject: ");
			String subject = scanner.nextLine();
			System.out.print("Enter new designation (Assistant Professor/Professor/Lecturer): ");
			String designation = scanner.nextLine();
			System.out.print("Enter new department ID: ");
			int departmentId = scanner.nextInt();

			Department department = departmentDAO.getDepartmentById(departmentId);
			if (department == null) {
				System.out.println("Department with ID " + departmentId + " does not exist.");
				return;
			}

			teacher.setName(name);
			teacher.setSubject(subject);
			teacher.setDesignation(designation);
			teacher.setDepartment(departmentId);

			teacherDAO.updateTeacher(teacher);
			System.out.println("Teacher updated successfully.");
		} else {
			System.out.println("Teacher with ID " + id + " does not exist.");
		}
	}

	private static void deleteDepartment(Scanner scanner) {
		System.out.print("Enter department ID: ");
		int id = scanner.nextInt();
		departmentDAO.deleteDepartment(id);
		System.out.println("Department deleted successfully.");
	}

	private static void deleteTeacher(Scanner scanner) {
		System.out.print("Enter teacher ID: ");
		int id = scanner.nextInt();
		teacherDAO.deleteTeacher(id);
		System.out.println("Teacher deleted successfully.");
	}

	private static List<Teacher> searchTeachersByName(Scanner scanner) {
		scanner.nextLine();
		System.out.print("Enter teacher name: ");
		String searchName = scanner.nextLine();
		List<Teacher> teachersByName = teacherDAO.searchTeachersByName(searchName);
		if (teachersByName.isEmpty()) {
			System.out.println("No teachers found with the given name.");
		} else {
			System.out.println("Teachers found with the given name:");
			for (Teacher teacher : teachersByName) {
				System.out.println(teacher);
			}
		}
		return teachersByName;
	}

	private static List<Teacher> searchTeachersBySubject(Scanner scanner) {
		scanner.nextLine();
		System.out.print("Enter subject: ");
		String searchSubject = scanner.nextLine();
		List<Teacher> teachersBySubject = teacherDAO.searchTeachersBySubject(searchSubject);
		if (teachersBySubject.isEmpty()) {
			System.out.println("No teachers found with the given subject.");
		} else {
			System.out.println("Teachers found with the given subject:");
			for (Teacher teacher : teachersBySubject) {
				System.out.println(teacher);
			}
		}
		return teachersBySubject;
	}

	private static List<Department> searchDepartmentsByName(Scanner scanner) {
		scanner.nextLine();
		System.out.print("Enter name: ");
		String searchName = scanner.nextLine();
		List<Department> departmentsByName = departmentDAO.searchDepartmentsByName(searchName);
		if (departmentsByName.isEmpty()) {
			System.out.println("No departments found with the given name.");
		} else {
			System.out.println("Deparments found with the given name:");
			for (Department deparment : departmentsByName) {
				System.out.println(deparment);
			}
		}
		return departmentsByName;
	}
}
