package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.Device;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="AutoTest", group="Autonomous")
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
        //motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorL.setMaxSpeed(250);
        //motorR.setMaxSpeed(250); //arbitrary
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

    double curTime;

    public void initCurtime()
    {
        if(opModeIsActive())
            curTime = ((double)System.nanoTime())/1000000000.0;
    }

    public double getCurTime()
    {
        return curTime;
    }

    public void pause(double t) throws InterruptedException {
        initCurtime();

        //hacked
        double startTime = getCurTime();
        while (opModeIsActive() && getCurTime() < startTime + t)
        {
            initCurtime();
            idle();
        }
    }

    public void runOpMode() throws InterruptedException {
        initializeMotors();
        //initializeSensors();
        composeTelemetry();
        while(!opModeIsActive() && !isStopRequested())
            telemetry.update();idle();
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

    public void startMotors(double le, double ri) throws InterruptedException {
        motorL.setPower(le);
        motorR.setPower(ri);
    }

    public void stopMotors() throws InterruptedException {
        startMotors(0, 0);
    }

    public void rotatePID(double pow, int deg) throws InterruptedException {

        double power = pow;
        double angleTo = deg;
        double error;
        double inte = 0;
        double inteNoE = 0;
        double der;

        double currentAngle = getGyroYaw();
        double previousError = angleTo - currentAngle;

        telemetry.addData("Current Angle", currentAngle + "");
        telemetry.addData("Angle To", angleTo + "");
        telemetry.update();

        resetStartTime();

        currentAngle = 0;

        while(Math.abs(currentAngle) < Math.abs(angleTo) - 2) {
            currentAngle = getGyroYaw();
            error = Math.abs(angleTo) - Math.abs(currentAngle);
            telemetry.addData("error", error);
            power = (pow * (error) * .001) + .1;                   //update p values
            inte = ((getRuntime()) * error * .005);          //update inte value
            inteNoE = ((getRuntime()) * .055);
            der = (error - previousError) / getRuntime() * 0; //update der value

            power = power + inteNoE + der;

            if(angleTo > 0)
                power *= -1;

            Range.clip(power, -1, 1);
            startMotors(-power, power);
            telemetry.addData("PID", power);
//            opMode.telemetry.addData("integral", inte);
            telemetry.addData("integral without error", inteNoE);
            telemetry.addData("angle", currentAngle + " " + angleTo);

            telemetry.update();
            previousError = error;
            idle();
        }

        stopMotors();
        telemetry.addData("finished", "done");
        telemetry.update();
    }
    public void moveForward(double pow, int inches) throws InterruptedException {
        double startAngle = getGyroYaw();
        double currentAngle;
        double encoderTicks = ((Math.PI * 6) * (1440 * inches) / 6);
        while(encoderAvg() < encoderTicks) {
            currentAngle = getGyroYaw();
            if(currentAngle > startAngle + 2) {
                startMotors(pow * .75, pow);
            }
            else if(currentAngle < startAngle - 2) {
                startMotors(pow, pow * .75);
            }
            else {
                startMotors(pow, pow);
            }
            telemetry.update();
        }
        stopMotors();

    }

    public int encoderAvg() {
        return (Math.abs(motorL.getCurrentPosition()) + Math.abs(motorR.getCurrentPosition())) / 2;
    }

    private void composeTelemetry() {
        telemetry.addLine()
                .addData("L", new Func<String>() {
                    @Override public String value() {
                        return "L: " + motorL.getPower();
                    }
                });
        telemetry.addLine()
                .addData("R", new Func<String>() {
                    @Override public String value() {
                        return "R: " + motorR.getPower();
                    }
                });
        //telemetry.addLine()
        //        .addData("yaw", new Func<String>() {
        //            @Override public String value() {
        //                return "yaw: " + getGyroYaw();
        //            }
        //        });
    }
}