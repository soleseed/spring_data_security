package data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import data.jpa.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
