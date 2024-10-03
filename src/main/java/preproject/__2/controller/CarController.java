package preproject.__2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import preproject.__2.model.Car;
import preproject.__2.model.dto.CarDto;
import preproject.__2.service.CarService;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public Car addCar(@RequestBody CarDto carDto) {
        return carService.addCar(carDto);
    }

    @GetMapping
    public String getCars(@RequestParam(required = false) Integer limit,
                          @RequestParam(required = false) String sortBy,
                          Model model) {
        model.addAttribute("cars", carService.getCars(limit, sortBy));
        return "cars";
    }

}
