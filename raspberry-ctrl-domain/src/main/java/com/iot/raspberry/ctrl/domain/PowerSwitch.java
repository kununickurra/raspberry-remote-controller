package com.iot.raspberry.ctrl.domain;

/**
 * Domain Model Class.
 * Represents a configured Power Switch and it's current state
 */
public class PowerSwitch {

    private int id;
    private String name;
    private SwitchState state;

    public PowerSwitch(int id, String name, SwitchState state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SwitchState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PowerSwitch)) return false;

        PowerSwitch that = (PowerSwitch) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (state != that.state) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PowerSwitch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
