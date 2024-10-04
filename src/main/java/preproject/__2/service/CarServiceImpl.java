package preproject.__2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

    @Value("${howToSort}")
    private List<Boolean> howToSortFields;

    private final CarRepository carRepository;

    @Override
    public Car addCar(CarDto carDto) {
        Car savedCar = carRepository.save(CarMapper.fromDto(carDto));
        log.info("Car with id {} successfully saved", savedCar.getId());
        return savedCar;
    }

    @Override
    public List<CarDto> getCars(Integer limit, String sortBy, Boolean howToSort) {
        List<Car> cars;
        Sort sort;
        if (limit == null || limit >= maxCar) {
            if (sortBy != null) {
                if (howToSort) {
                    sort = Sort.by(Sort.Direction.ASC, sortBy);
                    log.info("Using sort with object Sort");
                    cars = carRepository.findAll(sort);
                } else {
                    log.info("Using sort with sql order by");
                    cars = carRepository.findAllWithSortBy(sortBy);
                }
                log.info("Successfully retrieved {} cars with sort and without limit", cars.size());
            } else {
                cars = carRepository.findAll();
                log.info("Successfully retrieved {} cars without sort and limit", cars.size());
            }
        } else {
            if (sortBy != null) {
                if (howToSort) {
                    sort = Sort.by(Sort.Direction.ASC, sortBy);
                    log.info("Using sort with object Sort");
                    cars = carRepository.findAllWithLimit(limit, sort);
                } else {
                    log.info("Using sort with sql order by");
                    cars = carRepository.findAllWithLimitAndSortBy(limit, sortBy);
                }
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

    private <T> T chooseSort(Sort sort, String sortBy, Boolean useObjectSort) {
        if (useObjectSort) {
            return (T) sortBy;
        } else {
            return (T) sort;
        }
    }
}
