package org.firstinspires.ftc.teamcode;

/**
 * Created by spencersharp on 3/10/17.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Arc Right", group="Autonomous")
public class ArcRight extends TrashCanProtoOpMode
{
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        while(opModeIsActive()) {
            motorL.setPower(0);
            motorR.setPower(-1);
            telemetry.update();
            idle();
        }
    }
}
