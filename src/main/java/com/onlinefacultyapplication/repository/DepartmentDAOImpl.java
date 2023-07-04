package com.onlinefacultyapplication.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.onlinefacultyapplication.model.Department;
import com.onlinefacultyapplication.model.Teacher;

public class DepartmentDAOImpl implements DepartmentDAO {
	private final String url;
	private final String username;
	private final String password;

	public DepartmentDAOImpl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public Department createDepartment(Department department) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "INSERT INTO department (name) VALUES (?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, department.getName());
			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating department failed, no rows affected.");
			}
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				department.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating department failed, no ID obtained.");
			}
			return department;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Department getDepartmentById(int id) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "SELECT * FROM department WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Department department = new Department();
				department.setId(resultSet.getInt("id"));
				department.setName(resultSet.getString("name"));
				department.setTeachers(getTeachersByDepartment(department.getId()));
				return department;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "SELECT * FROM department";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Department department = new Department();
				department.setId(resultSet.getInt("id"));
				department.setName(resultSet.getString("name"));
				department.setTeachers(getTeachersByDepartment(department.getId()));
				departments.add(department);
			}
			return departments;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateDepartment(Department department) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "UPDATE department SET name = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, department.getName());
			statement.setInt(2, department.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteDepartment(int id) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "DELETE FROM department WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private List<Teacher> getTeachersByDepartment(int departmentId) {
		List<Teacher> teachers = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "SELECT * FROM teacher WHERE department_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, departmentId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Teacher teacher = new Teacher();
				teacher.setId(resultSet.getInt("id"));
				teacher.setName(resultSet.getString("name"));
				teacher.setSubject(resultSet.getString("subject"));
				teacher.setDesignation(resultSet.getString("designation"));
				teachers.add(teacher);
			}
			return teachers;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Department> searchDepartmentsByName(String name) {
		List<Department> departments = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM department WHERE name LIKE ?");
			statement.setString(1, "%" + name + "%");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Department department = extractDepartmentFromResultSet(resultSet);
				departments.add(department);
			}
			return departments;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Department extractDepartmentFromResultSet(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("id"));
		department.setName(resultSet.getString("name"));
		return department;
	}

}
