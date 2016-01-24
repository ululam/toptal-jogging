package com.toptal.entrance.alexeyz.db;

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
    /**
     * @param userId Id of current user
     *
     * @return Weekly reports for all the joggings found
     */
    @Query("SELECT new com.toptal.entrance.alexeyz.domain.Week(MIN(date), count(*), sum(jog.distance), sum(jog.time)) " +
            "FROM Jog jog " +
            "WHERE jog.userId = :userId " +
            "GROUP BY CONCAT(YEAR(date), WEEK(date)) ORDER by CONCAT(YEAR(date), WEEK(date)) ASC")
    List<Week> getWeeks(@Param("userId") long userId);

    /**
     * Find all jogs for the given user
     * @param fromDate Start date
     * @param toDate End date
     * @param userId Id of a user
     *
     * @return Non-paged list of all found jogs
     */
    @Query("FROM Jog jog " +
            "WHERE jog.date >= :fromDate AND jog.date <= :toDate AND jog.userId = :userId " +
            "ORDER BY jog.date")
    List<Jog> findAllWithParameters(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("userId") long userId);

    /**
     * Find all jogs for all users
     * @param fromDate Start date
     * @param toDate End date
     *
     * @return Non-paged list of all found jogs
     */
    @Query("FROM Jog jog " +
            "WHERE jog.date >= :fromDate AND jog.date <= :toDate " +
            "ORDER BY jog.date")
    List<Jog> findAllWithParameters(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    /**
     * Deletes all the Jogs for the given user Id
     * @param userId Id of a user
     */
    void deleteByUserId(long userId);
}
