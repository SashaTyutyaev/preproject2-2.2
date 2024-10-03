package preproject.__2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CarRepository carRepository;

    @Override
    public Car addCar(CarDto carDto) {
        Car savedCar = carRepository.save(CarMapper.fromDto(carDto));
        log.info("Car with id {} successfully saved", savedCar.getId());
        return savedCar;
    }

    @Override
    public List<CarDto> getCars(Integer limit) {
        List<Car> cars;
        if (limit == null) {
            cars = carRepository.findAll();
            log.info("Size of cars list without limit - {}", cars.size());
        } else {
            cars = carRepository.findAllWithLimit(limit);
            log.info("Size of cars list with limit - {}", cars.size());
        }
        return cars.stream()
                .map(CarMapper::toDto)
                .toList();
    }
}
