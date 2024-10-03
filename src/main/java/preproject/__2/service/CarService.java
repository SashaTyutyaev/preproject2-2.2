package preproject.__2.service;

import preproject.__2.model.Car;
import preproject.__2.model.dto.CarDto;

import java.util.List;

public interface CarService {
    Car addCar(CarDto carDto);
    List<CarDto> getCars(Integer limit, String sortBy);
}
