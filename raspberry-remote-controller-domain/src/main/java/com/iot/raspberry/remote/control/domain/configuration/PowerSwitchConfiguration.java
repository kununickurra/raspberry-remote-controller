package com.iot.raspberry.remote.control.domain.configuration;

/**
 * Domain Model Class.
 * Represents a Power switch configuration item.
 */
public class PowerSwitchConfiguration {

    private int id;
    private String name;

    public PowerSwitchConfiguration(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PowerSwitchConfiguration)) return false;

        PowerSwitchConfiguration that = (PowerSwitchConfiguration) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PowerSwitchConfiguration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
