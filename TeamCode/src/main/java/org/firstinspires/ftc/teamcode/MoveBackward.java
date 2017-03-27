package org.firstinspires.ftc.teamcode;

/**
 * Created by spencersharp on 3/10/17.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Move Backward", group="Autonomous")
public class MoveBackward extends TrashCanProtoOpMode {
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        while(opModeIsActive()) {
            motorL.setPower(-1);
            motorR.setPower(1);
            telemetry.update();
            idle();
        }
    }
}
