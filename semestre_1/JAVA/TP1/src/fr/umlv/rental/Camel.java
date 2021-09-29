package fr.umlv.rental;

public record Camel(int year) implements Vehicule {
	
    @Override
    public int insuranceCostAt(int year) {
    	if(year < this.year)
    		throw new IllegalArgumentException("Year should be higher than the date of birth of the camel");
        return (year - this.year) * 100;
    }
    
    @Override
    public String toString(){
       return "camel " + year;
    }
}
