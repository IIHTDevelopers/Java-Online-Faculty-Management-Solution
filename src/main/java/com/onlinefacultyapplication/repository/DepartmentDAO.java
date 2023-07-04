package com.onlinefacultyapplication.repository;

import java.util.List;

import com.onlinefacultyapplication.model.Department;

public interface DepartmentDAO {
	Department createDepartment(Department department);

	Department getDepartmentById(int id);

	List<Department> getAllDepartments();

	void updateDepartment(Department department);

	boolean deleteDepartment(int id);

	List<Department> searchDepartmentsByName(String name);
}
