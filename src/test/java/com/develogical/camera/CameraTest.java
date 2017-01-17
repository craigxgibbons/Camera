package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        Sensor sensor  = context.mock(Sensor.class);
        MemoryCard memoryCard  = context.mock(MemoryCard.class);

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerUp();
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
    }

    @Test
    public void switchingTheCameraOffPowersOffTheSensor() {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard memoryCard  = context.mock(MemoryCard.class);

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerDown();
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOff();
    }

    @Test
    public void pressingTheShutterWhenThePowerIsOnCopiesData() {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard memoryCard  = context.mock(MemoryCard.class);

        context.checking(new Expectations() {{
            allowing(sensor).powerUp();

            exactly(1).of(sensor).readData();

            byte[] data = new byte[0];
            will(returnValue(data));

            exactly(1).of(memoryCard).write(data);
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
        camera.pressShutter();
    }

    @Test
    public void pressingTheShutterWhenThePowerIsOffDoesNothing()  {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard memoryCard  = context.mock(MemoryCard.class);

        context.checking(new Expectations() {{
            allowing(sensor).powerDown();

            exactly(0).of(sensor).readData();
            never(memoryCard);
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOff();
        camera.pressShutter();
    }

    @Test
    public void noPowerDownWhileWriting() throws InterruptedException {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard memoryCard  = context.mock(MemoryCard.class);

        context.checking(new Expectations() {{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData();

            byte[] data = new byte[0];
            will(returnValue(data));

            exactly(1).of(memoryCard).write(data);

            exactly(1).of(sensor).powerDown();
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
    }
}
