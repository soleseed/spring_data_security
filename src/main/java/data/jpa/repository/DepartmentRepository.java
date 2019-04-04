package data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import data.jpa.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
