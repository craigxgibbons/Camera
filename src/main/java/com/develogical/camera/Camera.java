package com.develogical.camera;

public class Camera implements WriteListener {

    private MemoryCard memoryCard;
    private Sensor sensor;

    private boolean isOn = false;
    private boolean doneWriting = true;


    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.memoryCard = memoryCard;
        this.sensor = sensor;
    }

    public void pressShutter() {
        if (isOn) {
            memoryCard.write(sensor.readData());
        }
    }


    public void powerOn() {
        sensor.powerUp();
        isOn = true;
    }

    public void powerOff() {
        if (doneWriting) {
            sensor.powerDown();
            isOn = false;
        }
    }


    @Override
    public void writeComplete() {
        doneWriting = true;
        sensor.powerDown();
    }
}

