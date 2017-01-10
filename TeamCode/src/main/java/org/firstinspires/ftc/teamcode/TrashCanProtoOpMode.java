package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by spencersharp on 1/10/17.
 */
public class TrashCanProtoOpMode extends LinearOpMode
{
    DcMotor motorL;
    DcMotor motorR;

    public void initializeMotors()
    {
        motorL = hardwareMap.dcMotor.get("motorL");
        motorR = hardwareMap.dcMotor.get("motorR");
    }

    public void runOpMode()
    {
        initializeMotors();
        waitForStart();

    }
}
