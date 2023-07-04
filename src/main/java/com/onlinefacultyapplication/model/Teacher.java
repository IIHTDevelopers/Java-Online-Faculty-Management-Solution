package com.onlinefacultyapplication.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@NotNull
	@Size(min = 3, max = 10)
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "subject")
	private String subject;

	@NotNull
	@Column(name = "designation")
	private String designation;

	@Column(name = "department_id")
	private int departmentId;

	public Teacher() {
		super();
	}

	public Teacher(int id, @NotNull @Size(min = 3, max = 10) String name, @NotNull String subject,
			@NotNull String designation, int departmentId) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.designation = designation;
		this.departmentId = departmentId;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getDepartment() {
		return departmentId;
	}

	public void setDepartment(int departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", subject=" + subject + ", designation=" + designation
				+ ", departmentId=" + departmentId + "]";
	}

}
