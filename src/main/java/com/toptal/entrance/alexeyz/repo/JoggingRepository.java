package com.toptal.entrance.alexeyz.repo;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public interface JoggingRepository extends JpaRepository<Jog, Long> {
    @Query("SELECT new com.toptal.entrance.alexeyz.domain.Week(MIN(date), count(*), sum(jog.distance), sum(jog.time)) " +
            "FROM Jog jog " +
            "WHERE jog.userId = :userId " +
            "GROUP BY CONCAT(YEAR(date), WEEK(date)) ORDER by CONCAT(YEAR(date), WEEK(date)) ASC")
    List<Week> getWeeks(@Param("userId") long userId);

    @Query("FROM Jog jog " +
            "WHERE jog.date >= :fromDate AND jog.date <= :toDate AND jog.userId = :userId " +
            "ORDER BY jog.date")
    List<Jog> findAllWithParameters(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("userId") long userId);
}
