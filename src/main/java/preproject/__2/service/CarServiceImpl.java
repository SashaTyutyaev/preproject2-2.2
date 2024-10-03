package preproject.__2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import preproject.__2.exceptions.IllegalSortException;
import preproject.__2.model.Car;
import preproject.__2.model.dto.CarDto;
import preproject.__2.model.dto.CarMapper;
import preproject.__2.repository.CarRepository;

import java.util.Comparator;
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

    @Override
    public Car addCar(CarDto carDto) {
        Car savedCar = carRepository.save(CarMapper.fromDto(carDto));
        log.info("Car with id {} successfully saved", savedCar.getId());
        return savedCar;
    }

    @Override
    public List<CarDto> getCars(Integer limit, String sortBy) {
        List<Car> cars;
        if (limit == null || limit >= maxCar) {
            cars = carRepository.findAll();
            log.info("Getting cars without limit");
        } else {
            cars = carRepository.findAllWithLimit(limit);
            log.info("Getting cars with limit");
        }
        manageSortBy(sortBy, cars);
        return cars.stream()
                .map(CarMapper::toDto)
                .toList();
    }

    private void manageSortBy(String sortBy, List<Car> cars) {
        if (sortBy == null) {
            log.info("Getting cars without sortBy");
        } else {
            chooseSortParameter(sortBy, cars);
            log.info("Getting cars with sortBy");
        }
    }

    private void chooseSortParameter(String sortBy, List<Car> cars) {
        if (!enabledFields.contains(sortBy)) {
            throw new IllegalSortException("Illegal sort parameter");
        }

        switch (sortBy) {
            case "model":
                cars.sort(Comparator.comparing(Car::getModel));
            case "color":
                cars.sort(Comparator.comparing(Car::getColor));
        }
    }
}
