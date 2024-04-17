package ru.fildv.tacocloud.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fildv.tacocloud.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
