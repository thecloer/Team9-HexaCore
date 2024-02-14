package com.hexacore.tayo.car.dto;

import com.hexacore.tayo.car.dto.GetCarDateRangeResponseDto.CarDateRangeListDto;
import com.hexacore.tayo.car.model.Car;
import com.hexacore.tayo.car.model.CarDateRange;
import com.hexacore.tayo.car.model.CarType;
import com.hexacore.tayo.car.model.FuelType;
import com.hexacore.tayo.user.dto.GetUserSimpleResponseDto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class GetCarResponseDto {

    @NotNull
    private final GetUserSimpleResponseDto host;

        @NotNull
        private final String carName;

        @NotNull
        private final String carNumber;

        @NotNull
        private final List<String> imageUrls;

        @NotNull
        private final Double mileage;

        @NotNull
        private final String fuel;

        @NotNull
        private final String type;

        @NotNull
        private final Integer capacity;

        @NotNull
        private final Integer year;

        @NotNull
        private final Integer feePerHour;

        @NotNull
        private final String address;

    @NotNull
    private final List<List<String>> carDateRanges = new ArrayList<>();

    private final String description;

    public GetCarResponseDto(Car car, List<String> images) {
        this.carName = car.getSubCategory().getName();
        this.carNumber = car.getCarNumber();
        this.imageUrls = images;
        this.mileage = car.getMileage();
        this.type = car.getType().getType();
        this.fuel = car.getFuel().getType();
        this.capacity = car.getCapacity();
        this.year = car.getYear();
        this.feePerHour = car.getFeePerHour();
        this.address = car.getAddress();
        this.description = car.getDescription();
        this.carDateRanges.addAll(new CarDateRangeListDto(car.getCarDateRanges()).getDateRanges());
        this.host = new GetUserSimpleResponseDto(car.getOwner());
    }
}
