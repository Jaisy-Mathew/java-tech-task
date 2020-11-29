package com.rezdy.lunch.repository;

import com.rezdy.lunch.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
@Repository
@Transactional
public interface LunchRepository extends JpaRepository<Recipe, String> {
    @Query(value = "select distinct r1.* from  recipe r1 where r1.title not in\n" +
                  "(select distinct r.* from (select * from ingredient i where i.use_by< (:date)) a\n" +
            "inner join recipe_ingredient ri on a.title=ri.ingredient\n" +
            "inner join recipe r on r.title =ri.recipe)",
            nativeQuery = true)
    List<Recipe>  findNonExpiredRecipeList(@Param("date") LocalDate date);
}
