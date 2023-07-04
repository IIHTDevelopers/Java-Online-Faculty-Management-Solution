package com.assessment.onlinefacultyapplication.testutils;

import java.util.ArrayList;
import java.util.List;

import com.onlinefacultyapplication.model.Department;
import com.onlinefacultyapplication.model.Teacher;

public class MasterData {
	public static Department getDepartment() {
		Department department = new Department(1, "CS", getTeachersList());
		return department;
	}

	public static List<Department> getDepartmentsList() {
		List<Department> departments = new ArrayList<>();
		Department department = new Department(1, "CS", getTeachersList());
		departments.add(department);

		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher(1, "Sir PQR", "OOP", "Professor", 2);
		teachers.add(teacher);

		teacher = new Teacher(2, "Sir HIJ", "Java", "Assistant Professor", 2);
		teachers.add(teacher);
		department = new Department(2, "IT", teachers);
		departments.add(department);
		return departments;
	}

	public static Teacher getTeacher() {
		Teacher teacher = new Teacher(1, "Sir ABC", "OOP", "Professor", 1);
		return teacher;
	}

	public static List<Teacher> getTeachersList() {
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher(1, "Sir ABC", "OOP", "Professor", 1);
		teachers.add(teacher);

		teacher = new Teacher(2, "Sir XYZ", "Java", "Assistant Professor", 1);
		teachers.add(teacher);
		return teachers;
	}
}
