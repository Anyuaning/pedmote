package com.anyuaning.osp.model.sport;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table JOGGING.
 */
public class Jogging {

    private Long id;
    private Long steps;
    private Float distance;
    private Long pace;
    private Float speed;
    private Float calories;

    public Jogging() {
    }

    public Jogging(Long id) {
        this.id = id;
    }

    public Jogging(Long id, Long steps, Float distance, Long pace, Float speed, Float calories) {
        this.id = id;
        this.steps = steps;
        this.distance = distance;
        this.pace = pace;
        this.speed = speed;
        this.calories = calories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSteps() {
        return steps;
    }

    public void setSteps(Long steps) {
        this.steps = steps;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Long getPace() {
        return pace;
    }

    public void setPace(Long pace) {
        this.pace = pace;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

}
