package com.toptal.entrance.alexeyz.repo;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public interface JoggingRepository extends JpaRepository<Jog, Long> {
    @Query("SELECT new com.toptal.entrance.alexeyz.domain.Week(MIN(date), count(*), sum(jog.distance), sum(jog.time)) " +
            "FROM Jog jog GROUP BY CONCAT(YEAR(date), WEEK(date)) ORDER by CONCAT(YEAR(date), WEEK(date)) ASC")
    List<Week> getWeeks();
}
