package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by spencersharp on 1/27/17.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="Teleop")
public class TrashTeleOp extends TrashCanProtoOpMode{

    double brakeTime = 1.0;
    public void runOpMode() throws InterruptedException
    {
        super.runOpMode();
        TreeMap<Double,double[]> recordedCommands = new TreeMap<Double,double[]>();
        waitForStart();
        initCurtime();
        boolean isBraking1 = false;
        boolean isBraking2 = false;
        double timeOfBrakingStart1 = getCurTime();
        double timeOfBrakingStart2 = getCurTime();

        double yVal1 = 0.0;
        double sendVal1 = 0.0;
        double yVal2 = 0.0;
        double sendVal2 = 0.0;
        boolean isXPressed = gamepad1.x;
        while(!isXPressed && opModeIsActive())
        {
            initCurtime();
            yVal1 = gamepad1.left_stick_y;
            yVal2 = gamepad1.right_stick_y;
            if(!isBraking1 && (sendVal1 >= 0.05 || sendVal1 <= -0.05) && yVal1 < 0.05 && yVal1 > -0.05) {
                isBraking1 = true;
                timeOfBrakingStart1 = getCurTime();
            }
            if(!isBraking1)
            {
                sendVal1 = yVal1;
                motorR.setPower(yVal1*0.8);
            }
            else if(getCurTime() - timeOfBrakingStart1 < brakeTime)
            {
                yVal1 = sendVal1 * ((getCurTime() - timeOfBrakingStart1)/brakeTime);
                motorR.setPower(yVal1*0.8);
            }
            else
            {
                isBraking1 = false;
                sendVal1 = yVal1;
                motorR.setPower(0.0);
            }
            if(!isBraking2 && (sendVal2 >= 0.05 || sendVal2 <= -0.05) && yVal2 < 0.05 && yVal2 > -0.05) {
                isBraking2 = true;
                timeOfBrakingStart2 = getCurTime();
            }
            if(!isBraking2)
            {
                sendVal2 = yVal2;
                motorL.setPower(-yVal2);
            }
            else if(getCurTime() - timeOfBrakingStart2 < brakeTime)
            {
                yVal2 = sendVal2 * ((getCurTime() - timeOfBrakingStart2)/brakeTime);
                motorL.setPower(-yVal2);
            }
            else
            {
                isBraking2 = false;
                sendVal2 = yVal2;
                motorL.setPower(0.0);
            }
            recordedCommands.put(getCurTime(),new double[]{yVal1*0.8,-yVal2});
            pause(0.05);
        }
        while(opModeIsActive() && recordedCommands.size()>0)
        {
            double[] ray = recordedCommands.pollFirstEntry().getValue();
            motorL.setPower(ray[1]);
            motorR.setPower(ray[0]);
            pause(0.05);
        }
    }
}
