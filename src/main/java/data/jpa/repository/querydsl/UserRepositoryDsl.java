package data.jpa.repository.querydsl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import data.jpa.domain.querydsl.User;

public interface UserRepositoryDsl extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

}
