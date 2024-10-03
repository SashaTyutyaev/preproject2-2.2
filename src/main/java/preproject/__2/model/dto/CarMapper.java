package preproject.__2.model.dto;

import preproject.__2.model.Car;

public class CarMapper {

    public static CarDto toDto(Car car) {
        return CarDto.builder()
                .model(car.getModel())
                .color(car.getColor())
                .country(car.getCountry())
                .build();
    }

    public static Car fromDto(CarDto carDto) {
        return Car.builder()
                .model(carDto.getModel())
                .color(carDto.getColor())
                .country(carDto.getCountry())
                .build();
    }
}
