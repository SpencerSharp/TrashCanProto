package org.firstinspires.ftc.teamcode;

/**
 * Created by spencersharp on 1/10/17.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Motor Test", group="Autonomous")
public class MotorTest extends TrashCanProtoOpMode
{
    public void runOpMode()
    {
        super.runOpMode();
        while(opModeIsActive())
        {
            motorL.setPower(1.0);
            motorR.setPower(-1.0);
            idle();
        }
    }
}
