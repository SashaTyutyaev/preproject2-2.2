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

    private final CarRepository carRepository;
    @Value("${maxCar}")
    private Integer maxCar;
    @Value("${enabledFields}")
    private List<String> enabledFields;
    @Value("${howToSort}")
    private List<Boolean> howToSortFields;

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
            if (sortBy != null) {
                validateSortParameter(sortBy, howToSort);
                cars = findWithoutLimitWithSort(howToSort, sortBy);
                log.info("Successfully retrieved {} cars with sort and without limit", cars.size());
            } else {
                cars = carRepository.findAll();
                log.info("Successfully retrieved {} cars without sort and limit", cars.size());
            }
        } else {
            if (sortBy != null) {
                validateSortParameter(sortBy, howToSort);
                cars = findWithLimitAndSort(limit, sortBy, howToSort);
                log.info("Successfully retrieved {} cars with sort and limit", cars.size());
            } else {
                cars = carRepository.findAllWithLimit(limit);
                log.info("Successfully retrieved {} cars without sort and with limit", cars.size());
            }
        }
        return cars.stream()
                .map(CarMapper::toDto)
                .toList();
    }

    private List<Car> findWithoutLimitWithSort(boolean howToSort, String sortBy) {
        List<Car> cars;
        if (howToSort) {
            Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
            log.info("Using sort with object Sort");
            cars = carRepository.findAll(sort);
        } else {
            log.info("Using sort with sql order by");
            cars = carRepository.findAllWithSortBy(sortBy);
        }
        return cars;
    }

    private List<Car> findWithLimitAndSort(Integer limit, String sortBy, Boolean howToSort) {
        List<Car> cars;
        if (howToSort) {
            Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
            log.info("Using sort with object Sort");
            cars = carRepository.findAllWithLimit(limit, sort);
        } else {
            log.info("Using sort with sql order by");
            cars = carRepository.findAllWithLimitAndSortBy(limit, sortBy);
        }
        return cars;
    }

    private void validateSortParameter(String sortBy, Boolean howToSort) {
        if (!enabledFields.contains(sortBy)) {
            log.error("Invalid sort parameter: {}", sortBy);
            throw new IllegalSortException("Invalid sort parameter: " + sortBy);
        }

        if (!howToSortFields.contains(howToSort)) {
            log.error("Invalid sort parameter: {}", howToSort);
            throw new IllegalSortException("Invalid sort parameter: " + howToSort);
        }
    }
}
