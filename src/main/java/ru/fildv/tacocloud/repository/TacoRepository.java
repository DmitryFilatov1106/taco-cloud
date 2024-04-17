package ru.fildv.tacocloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fildv.tacocloud.model.Taco;

public interface TacoRepository extends JpaRepository<Taco, Long> {
}
