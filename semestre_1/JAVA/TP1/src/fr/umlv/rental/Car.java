package fr.umlv.rental;

import java.util.Objects;

public record Car(String model, int year) implements Vehicule {
    public Car {
        Objects.requireNonNull(model);
        if (year < 0) {
        	throw new IllegalArgumentException("year should be positive");
        }
    }

    @Override
    public int insuranceCostAt(int year) {
    	if(year < this.year)
    		throw new IllegalArgumentException("Year should be higher than manufactured year of a vehicule");
        return (year - this.year) < 10 ? 200 : 500;
    }

    @Override
    public String toString(){
       return model + " " + year;
    }
}
