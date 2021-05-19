package ru.guybydefault.foody.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.guybydefault.foody.domain.Recipe;

import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {
  Optional<Recipe> findById(String id);
}
