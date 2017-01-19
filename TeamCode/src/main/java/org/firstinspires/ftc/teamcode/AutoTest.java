package org.firstinspires.ftc.teamcode;

/**
 * Created by spencersharp on 1/10/17.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Auto Test", group="Autonomous")
public class AutoTest extends TrashCanProtoOpMode
{
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        moveForward(.5, 12);
        rotatePID(.5, 90);
        moveForward(.5, 12);
        stopMotors();
    }
}
