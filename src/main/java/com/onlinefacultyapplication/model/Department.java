package com.onlinefacultyapplication.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@NotNull
	@Size(min = 3, max = 10)
	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Teacher> teachers;

	public Department() {
		super();
	}

	public Department(int id, @NotNull @Size(min = 3, max = 10) String name, List<Teacher> teachers) {
		super();
		this.id = id;
		this.name = name;
		this.teachers = teachers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", teachers=" + teachers + "]";
	}

}
