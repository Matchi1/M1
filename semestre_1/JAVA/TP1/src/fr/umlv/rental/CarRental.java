package fr.umlv.rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarRental {
    private final ArrayList<Vehicule> cars;

    public CarRental () {
        this.cars = new ArrayList<>();
    }

    public void add(Vehicule car){
        Objects.requireNonNull(car);
        cars.add(car);
    }

    public String toString(){
    	/*
        StringBuilder str = new StringBuilder();
        if(cars.isEmpty())
        	return "";
        for( var car : cars ) {
            str.append(car).append('\n');
        }
        str.deleteCharAt(str.length() - 1);
    	*/
        return cars.stream()
        		.map(car -> car.toString())
        		.collect(Collectors.joining("\n"));
    }

    public void remove(Vehicule car){
        Objects.requireNonNull(car);
        if(!cars.contains(car)) {
        	throw new IllegalStateException("there is no such car : " + car);
        }
        cars.remove(car);
    }

    public List<Vehicule> findAllByYear(int year){
        return cars.stream()
                .filter(c -> c.year() == year)
                .toList();
    }

    public int insuranceCostAt(int year){
        return cars.stream()
                .mapToInt(c -> c.insuranceCostAt(year))
                .sum();
    }

    public Optional<Car> findACarByModel(String model){
    	if(model == null)
    		throw new NullPointerException("model should not be null");
        return cars.stream()
        			.flatMap(v -> {
        				if(v instanceof Car car && car.model().equals(model)) {
        					return Stream.of(car);
        				}
        				return null;
        			})
                    .findAny();
    }

}
