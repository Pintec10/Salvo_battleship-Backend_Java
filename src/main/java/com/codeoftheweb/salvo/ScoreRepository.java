package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Set;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Set<Score> findByGame(Game game);
    Set<Score> findByPlayer (Player player);
}
