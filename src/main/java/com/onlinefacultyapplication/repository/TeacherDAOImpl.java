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

public class TeacherDAOImpl implements TeacherDAO {
	private final String url;
	private final String username;
	private final String password;

	public TeacherDAOImpl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public Teacher createTeacher(Teacher teacher) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "INSERT INTO teacher (name, subject, designation, department_id) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, teacher.getName());
			statement.setString(2, teacher.getSubject());
			statement.setString(3, teacher.getDesignation());
			statement.setInt(4, teacher.getDepartment());
			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating teacher failed, no rows affected.");
			}
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				teacher.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating teacher failed, no ID obtained.");
			}
			return teacher;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Teacher getTeacherById(int id) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "SELECT * FROM teacher WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Teacher teacher = new Teacher();
				teacher.setId(resultSet.getInt("id"));
				teacher.setName(resultSet.getString("name"));
				teacher.setSubject(resultSet.getString("subject"));
				teacher.setDesignation(resultSet.getString("designation"));
				int departmentId = resultSet.getInt("department_id");
				DepartmentDAO departmentDAO = new DepartmentDAOImpl(this.url, this.username, this.password);
				Department department = departmentDAO.getDepartmentById(departmentId);
				teacher.setDepartment(departmentId);
				return teacher;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "SELECT * FROM teacher";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Teacher teacher = new Teacher();
				teacher.setId(resultSet.getInt("id"));
				teacher.setName(resultSet.getString("name"));
				teacher.setSubject(resultSet.getString("subject"));
				teacher.setDesignation(resultSet.getString("designation"));
				int departmentId = resultSet.getInt("department_id");
				DepartmentDAO departmentDAO = new DepartmentDAOImpl(this.url, this.url, this.password);
				teacher.setDepartment(departmentId);
				teachers.add(teacher);
			}
			return teachers;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Teacher> getTeachersByDepartment(int departmentId) {
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
	public void updateTeacher(Teacher teacher) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "UPDATE teacher SET name = ?, subject = ?, designation = ?, department_id = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, teacher.getName());
			statement.setString(2, teacher.getSubject());
			statement.setString(3, teacher.getDesignation());
			statement.setInt(4, teacher.getDepartment());
			statement.setInt(5, teacher.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteTeacher(int id) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "DELETE FROM teacher WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Teacher> searchTeachersByName(String name) {
		List<Teacher> teachers = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM teacher WHERE name LIKE ?");
			statement.setString(1, "%" + name + "%");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Teacher teacher = extractTeacherFromResultSet(resultSet);
				teachers.add(teacher);
			}
			return teachers;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Teacher> searchTeachersBySubject(String subject) {
		List<Teacher> teachers = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM teacher WHERE subject LIKE ?");
			statement.setString(1, "%" + subject + "%");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Teacher teacher = extractTeacherFromResultSet(resultSet);
				teachers.add(teacher);
			}
			return teachers;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	private Teacher extractTeacherFromResultSet(ResultSet resultSet) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setId(resultSet.getInt("id"));
		teacher.setName(resultSet.getString("name"));
		teacher.setSubject(resultSet.getString("subject"));
		teacher.setDesignation(resultSet.getString("designation"));
		teacher.setDepartment(resultSet.getInt("department_id"));
		return teacher;
	}
}
