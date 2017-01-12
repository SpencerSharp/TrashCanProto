package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.Device;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by spencersharp on 1/10/17.
 */
public class TrashCanProtoOpMode extends LinearOpMode {
    DcMotor motorL;
    DcMotor motorR;
    DeviceInterfaceModule cdim;
    BNO055IMU gyro;

    Orientation angles;
    Acceleration gravity;
    BNO055IMU.Parameters parameters;

    public void initializeMotors() {
        motorL = hardwareMap.dcMotor.get("motorL");
        motorR = hardwareMap.dcMotor.get("motorR");
    }

    public void initializeSensors() {
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        gyro = hardwareMap.get(BNO055IMU.class, "gyro");
        gyro.initialize(parameters);

        angles   = gyro.getAngularOrientation();
        gravity  = gyro.getGravity();
    }

    public void runOpMode() throws InterruptedException {
        initializeMotors();
        initializeSensors();
        waitForStart();
    }


    public void updateValues() {
        angles = gyro.getAngularOrientation();
    }

    public double getGyroYaw() {
        updateValues();
        double value = angles.firstAngle * -1;
        if(angles.firstAngle < -180)
            value -= 360;
        return value;
    }

    private void composeTelemetry() {
        telemetry.addLine()
                .addData("L", new Func<String>() {
                    @Override public String value() {
                        return "L: " + motorL.getCurrentPosition();
                    }
                });
        telemetry.addLine()
                .addData("R", new Func<String>() {
                    @Override public String value() {
                        return "R: " + motorR.getCurrentPosition();
                    }
                });
        telemetry.addLine()
                .addData("yaw", new Func<String>() {
                    @Override public String value() {
                        return "yaw: " + getGyroYaw();
                    }
                });
    }

}