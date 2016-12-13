package com.dragon.orientationProvider;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.dragon.navigation.Control.Data;

/**
 * The orientation provider that delivers the current orientation from the {@link Sensor#TYPE_GRAVITY
 * Gravity} and {@link Sensor#TYPE_MAGNETIC_FIELD Compass}.
 * 
 * @author Alexander Pacha
 * 
 */
public class GravityCompassProvider extends OrientationProvider {

    /**
     * Compass values
     */
    private float[] magnitudeValues = new float[3];

    /**
     * Gravity values
     */
    private float[] gravityValues = new float[3];

    /**
     * Initialises a new GravityCompassProvider
     * 
     * @param sensorManager The android sensor manager
     */
    public GravityCompassProvider(SensorManager sensorManager) {
        super(sensorManager);

        //Add the compass and the gravity sensor
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnitudeValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravityValues = event.values.clone();
        }

        if (magnitudeValues != null && gravityValues != null) {
            float[] i = new float[16];
            float[] values = new float[3];
            // Fuse gravity-sensor (virtual sensor) with compass
            SensorManager.getRotationMatrix(currentOrientationRotationMatrix.matrix, i, gravityValues, magnitudeValues);
            // Transform rotation matrix to quaternion
            SensorManager.getOrientation(currentOrientationRotationMatrix.matrix, values);
            for(int j=0;j<3;j++){
                Data.q[j]=(float)Math.toDegrees(values[j]);
            }
            Data.currentAzimuth=Data.q[0];
            currentOrientationQuaternion.setRowMajor(currentOrientationRotationMatrix.matrix);
        }
    }
}
