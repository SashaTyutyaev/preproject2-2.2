package preproject.__2.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import preproject.__2.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query(value = "select * from Cars limit ?1", nativeQuery = true)
    List<Car> findAllWithLimit(@Param("limit") int limit, Sort sort);

    @Query(value = "select * from Cars order by ?2 asc limit ?1", nativeQuery = true)
    List<Car> findAllWithLimitAndSortBy(int limit, String sortBy);

    @Query(value = "select * from Cars limit ?1", nativeQuery = true)
    List<Car> findAllWithLimit(int limit);

    List<Car> findAll(Sort sort);

    @Query(value = "select * from Cars as c order by ?1", nativeQuery = true)
    List<Car> findAllWithSortBy(String sortBy);
}
