package com.assessment.onlinefacultyapplication.functional;

import static com.assessment.onlinefacultyapplication.testutils.TestUtils.businessTestFile;
import static com.assessment.onlinefacultyapplication.testutils.TestUtils.currentTest;
import static com.assessment.onlinefacultyapplication.testutils.TestUtils.testReport;
import static com.assessment.onlinefacultyapplication.testutils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.jboss.jandex.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import com.onlinefacultyapplication.OnlineFacultyApplication;
import com.onlinefacultyapplication.model.Department;
import com.onlinefacultyapplication.model.Teacher;
import com.onlinefacultyapplication.repository.DepartmentDAO;
import com.onlinefacultyapplication.repository.DepartmentDAOImpl;
import com.onlinefacultyapplication.repository.TeacherDAO;
import com.onlinefacultyapplication.repository.TeacherDAOImpl;

@Component
public class FunctionalTests {

	private DepartmentDAO departmentDAO;
	private TeacherDAO teacherDAO;

	@BeforeEach
	public void setUp() {
		OnlineFacultyApplication mainObj = new OnlineFacultyApplication();
		Properties properties = new Properties();

		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(inputStream);

			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");

			mainObj.createDatabaseIfNotExists(properties);
			mainObj.createTablesIfNotExists(properties);

			departmentDAO = new DepartmentDAOImpl(url, username, password);
			teacherDAO = new TeacherDAOImpl(url, username, password);
//			dishDAO.removeAllDishes();
//			restaurantDAO.deleteAllRestaurants();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateDepartment() throws IOException {
		Department department = new Department();
		department.setName("Test");
		Department createdDepartment = departmentDAO.createDepartment(department);
		try {
			yakshaAssert(currentTest(), createdDepartment != null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetDepartmentById() throws IOException {
		Department department = new Department();
		department.setName("Test");
		departmentDAO.createDepartment(department);
		Department retrievedDepartment = departmentDAO.getDepartmentById(department.getId());
		try {
			yakshaAssert(currentTest(), department.getName().equals(retrievedDepartment.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllDepartments() throws IOException {
		Department department1 = new Department();
		department1.setName("Dep 1");
		Department createdDepartment1 = departmentDAO.createDepartment(department1);

		Department department2 = new Department();
		department2.setName("Dep 2");
		Department createdDepartment2 = departmentDAO.createDepartment(department2);

		if (createdDepartment1 != null) {
			Department departments = departmentDAO.getDepartmentById(createdDepartment1.getId());
			try {
				yakshaAssert(currentTest(), departments.getName().equals(createdDepartment1.getName()) ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateDepartment() throws IOException {
		Department department = new Department();
		department.setName("Test");
		departmentDAO.createDepartment(department);

		String updatedName = "Updated";
		department.setName(updatedName);
		departmentDAO.updateDepartment(department);

		Department retrievedDepartment = departmentDAO.getDepartmentById(department.getId());

		try {
			yakshaAssert(currentTest(), updatedName.equals(retrievedDepartment.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteDepartment() throws IOException {
		Department department = new Department();
		department.setName("Test");
		departmentDAO.createDepartment(department);

		boolean isDeleted = departmentDAO.deleteDepartment(department.getId());

		Department retrievedDepartment = departmentDAO.getDepartmentById(department.getId());

		try {
			yakshaAssert(currentTest(), isDeleted == true && retrievedDepartment == null ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchDepartmentsByName() throws IOException {
		Department department1 = new Department();
		department1.setName("Dep 1");
		departmentDAO.createDepartment(department1);

		Department department2 = new Department();
		department2.setName("Dep 2");
		departmentDAO.createDepartment(department2);

		List<Department> departments = departmentDAO.searchDepartmentsByName("Dep");

		try {
			yakshaAssert(currentTest(),
					departments != null && departments.get(0).getName().equals(department1.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testCreateTeacher() throws IOException {
		Teacher teacher = new Teacher();
		teacher.setName("John");
		teacher.setSubject("Maths");
		teacher.setDesignation("Assistant Professor");
		teacher.setDepartment(1);

		Teacher createdTeacher = teacherDAO.createTeacher(teacher);

//        Assertions.assertNotNull(teacher.getId());
		try {
			yakshaAssert(currentTest(), createdTeacher != null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetTeacherById() throws IOException {
		Teacher teacher = new Teacher();
		teacher.setName("John Doe");
		teacher.setSubject("Maths");
		teacher.setDesignation("Assistant Professor");
		teacher.setDepartment(1);

		teacherDAO.createTeacher(teacher);

		Teacher retrievedTeacher = teacherDAO.getTeacherById(teacher.getId());

//		Assertions.assertEquals(teacher.getName(), retrievedTeacher.getName());
//		Assertions.assertEquals(teacher.getSubject(), retrievedTeacher.getSubject());
//		Assertions.assertEquals(teacher.getDesignation(), retrievedTeacher.getDesignation());
//		Assertions.assertEquals(teacher.getDepartmentId(), retrievedTeacher.getDepartmentId());
		try {
			yakshaAssert(currentTest(),
					teacher != null && teacher.getName().equals(retrievedTeacher.getName())
							&& teacher.getSubject().equals(retrievedTeacher.getSubject())
							&& teacher.getDesignation().equals(retrievedTeacher.getDesignation())
							&& teacher.getDepartment() == retrievedTeacher.getDepartment() ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllTeachers() throws IOException {
		Teacher teacher1 = new Teacher();
		teacher1.setName("John");
		teacher1.setSubject("Maths");
		teacher1.setDesignation("Professor");
		teacher1.setDepartment(1);
		teacherDAO.createTeacher(teacher1);

		Teacher teacher2 = new Teacher();
		teacher2.setName("Jane");
		teacher2.setSubject("Phy");
		teacher2.setDesignation("Lecturer");
		teacher2.setDepartment(2);
		teacherDAO.createTeacher(teacher2);

		List<Teacher> teachers = teacherDAO.getAllTeachers();

//		Assertions.assertEquals(2, teachers.size());
//		Assertions.assertTrue(teachers.contains(teacher1));
//		Assertions.assertTrue(teachers.contains(teacher2));
		try {
			yakshaAssert(currentTest(), teachers != null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateTeacher() throws IOException {
		Teacher teacher = new Teacher();
		teacher.setName("John");
		teacher.setSubject("Maths");
		teacher.setDesignation("Professor");
		teacher.setDepartment(1);
		teacherDAO.createTeacher(teacher);

		String updatedName = "John";
		String updatedSubject = "CS";
		teacher.setName(updatedName);
		teacher.setSubject(updatedSubject);
		teacherDAO.updateTeacher(teacher);

		Teacher retrievedTeacher = teacherDAO.getTeacherById(teacher.getId());

//		Assertions.assertEquals(updatedName, retrievedTeacher.getName());
//		Assertions.assertEquals(updatedSubject, retrievedTeacher.getSubject());
		try {
			yakshaAssert(currentTest(), updatedName.equals(retrievedTeacher.getName())
					&& updatedSubject.equals(retrievedTeacher.getSubject()) ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteTeacher() throws IOException {
		Teacher teacher = new Teacher();
		teacher.setName("John");
		teacher.setSubject("Maths");
		teacher.setDesignation("Assitant Professor");
		teacher.setDepartment(1);
		teacherDAO.createTeacher(teacher);

		boolean isDeleted = teacherDAO.deleteTeacher(teacher.getId());

		Teacher retrievedTeacher = teacherDAO.getTeacherById(teacher.getId());

//		Assertions.assertNull(retrievedTeacher);
		try {
			yakshaAssert(currentTest(), isDeleted == true && retrievedTeacher == null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchTeachersByName() throws IOException {
		Teacher teacher1 = new Teacher();
		teacher1.setName("John");
		teacher1.setSubject("Maths");
		teacher1.setDesignation("Professor");
		teacher1.setDepartment(1);
		teacherDAO.createTeacher(teacher1);

		Teacher teacher2 = new Teacher();
		teacher2.setName("Jane");
		teacher2.setSubject("Physic");
		teacher2.setDesignation("Professor");
		teacher2.setDepartment(2);
		teacherDAO.createTeacher(teacher2);

		List<Teacher> teachers = teacherDAO.searchTeachersByName("John");

//		Assertions.assertEquals(1, teachers.size());
//		Assertions.assertTrue(teachers.contains(teacher1));
		try {
			yakshaAssert(currentTest(), teachers != null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchTeachersBySubject() throws IOException {
		Teacher teacher1 = new Teacher();
		teacher1.setName("John");
		teacher1.setSubject("Maths");
		teacher1.setDesignation("Professor");
		teacher1.setDepartment(1);
		teacherDAO.createTeacher(teacher1);

		Teacher teacher2 = new Teacher();
		teacher2.setName("Jane");
		teacher2.setSubject("Physics");
		teacher2.setDesignation("Assitant Professor");
		teacher2.setDepartment(2);
		teacherDAO.createTeacher(teacher2);

		List<Teacher> teachers = teacherDAO.searchTeachersBySubject("Physics");

//		Assertions.assertEquals(1, teachers.size());
//		Assertions.assertTrue(teachers.contains(teacher2));
		try {
			yakshaAssert(currentTest(), teachers != null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}
}
