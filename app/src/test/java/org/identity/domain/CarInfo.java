package org.identity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)

@JsonPropertyOrder({"variantReg", "make", "model", "year"})
public class CarInfo {
    private String variantReg;
    private String make;
    private String model;
    private String year;

    public String getVariantReg() {
        return variantReg;
    }

    public void setVariantReg(String variantReg) {
        this.variantReg = variantReg;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s", variantReg, make, model, year);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
        {
            return false;
        }

        if (this.getClass() != other.getClass())
        {
            return false;
        }

        if(!String.valueOf(this.variantReg).equals(String.valueOf(((CarInfo) other).variantReg))) {
            return false;
        }
        if(!String.valueOf(this.make).equals(String.valueOf(((CarInfo) other).make))) {
            return false;
        }
        if(!String.valueOf(this.model).equals(String.valueOf(((CarInfo) other).model))) {
            return false;
        }
        if(!String.valueOf(this.year).equals(String.valueOf(((CarInfo) other).year))) {
            return false;
        }
        return true;
    }
}
