package preproject.__2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import preproject.__2.exceptions.IllegalSortException;
import preproject.__2.model.Car;
import preproject.__2.model.dto.CarDto;
import preproject.__2.model.dto.CarMapper;
import preproject.__2.repository.CarRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    @Value("${maxCar}")
    private Integer maxCar;
    @Value("${enabledFields}")
    private List<String> enabledFields;

    private final CarRepository carRepository;

    @Override
    @Transactional
    public Car addCar(CarDto carDto) {
        Car savedCar = carRepository.save(CarMapper.fromDto(carDto));
        log.info("Car with id {} successfully saved", savedCar.getId());
        return savedCar;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDto> getCars(Integer limit, String sortBy, Boolean howToSort) {
        List<Car> cars;
        if (limit == null || limit >= maxCar) {
            cars = retrieveCarsWithoutLimit(howToSort, sortBy);
        } else {
            cars = retrieveCarsWithLimit(limit, sortBy, howToSort);
        }
        return cars.stream()
                .map(CarMapper::toDto)
                .toList();
    }

    private List<Car> findWithoutLimitWithSort(boolean howToSort, String sortBy) {
        List<Car> cars;
        if (howToSort) {
            Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
            logSort("object Sort");
            cars = carRepository.findAll(sort);
        } else {
            logSort("sql Order by");
            cars = carRepository.findAllWithSortBy(sortBy);
        }
        return cars;
    }

    private List<Car> findWithLimitAndSort(Integer limit, String sortBy, Boolean howToSort) {
        List<Car> cars;
        if (howToSort) {
            Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
            logSort("object Sort");
            cars = carRepository.findAllWithLimit(limit, sort);
        } else {
            logSort("sql Order by");
            cars = carRepository.findAllWithLimitAndSortBy(limit, sortBy);
        }
        return cars;
    }

    private void validateSortParameter(String sortBy) {
        if (!enabledFields.contains(sortBy)) {
            log.error("Invalid sort parameter: {}", sortBy);
            throw new IllegalSortException("Invalid sort parameter: " + sortBy);
        }
    }

    private List<Car> retrieveCarsWithoutLimit(Boolean howToSort, String sortBy) {
        if (sortBy != null) {
            validateSortParameter(sortBy);
            List<Car> cars = findWithoutLimitWithSort(howToSort, sortBy);
            logCars(cars.size(), "without limit with sort");
            return cars;
        } else {
            List<Car> cars = carRepository.findAll();
            logCars(cars.size(), "without limit and sort");
            return cars;
        }
    }

    private List<Car> retrieveCarsWithLimit(Integer limit, String sortBy, Boolean howToSort) {
        if (sortBy != null) {
            validateSortParameter(sortBy);
            List<Car> cars = findWithLimitAndSort(limit, sortBy, howToSort);
            logCars(cars.size(), "with limit and sort");
            return cars;
        } else {
            List<Car> cars = carRepository.findAllWithLimit(limit);
            logCars(cars.size(), "with limit without sort");
            return cars;
        }
    }

    private void logCars(int size, String text) {
        log.info("Successfully retrieved {} cars {}", size, text);
    }

    private void logSort(String text) {
        log.info("Using sort with {}", text);
    }
}
