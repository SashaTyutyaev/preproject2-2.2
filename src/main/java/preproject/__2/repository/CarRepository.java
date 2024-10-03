package preproject.__2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import preproject.__2.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query(value = "select * from Cars limit ?1", nativeQuery = true)
    List<Car> findAllWithLimit(int limit);
}
