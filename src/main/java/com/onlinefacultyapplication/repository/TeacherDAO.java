package com.onlinefacultyapplication.repository;

import java.util.List;

import com.onlinefacultyapplication.model.Teacher;

public interface TeacherDAO {
	Teacher createTeacher(Teacher teacher);

	Teacher getTeacherById(int id);

	List<Teacher> getAllTeachers();

	List<Teacher> getTeachersByDepartment(int departmentId);

	void updateTeacher(Teacher teacher);

	boolean deleteTeacher(int id);

	List<Teacher> searchTeachersByName(String name);

	List<Teacher> searchTeachersBySubject(String subject);
}
