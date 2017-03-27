package org.firstinspires.ftc.teamcode;

/**
 * Created by spencersharp on 3/10/17.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Arc Left", group="Autonomous")
public class ArcLeft extends TrashCanProtoOpMode
{
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        while(opModeIsActive()) {
            motorL.setPower(1);
            motorR.setPower(0);
            telemetry.update();
            idle();
        }
    }
}
