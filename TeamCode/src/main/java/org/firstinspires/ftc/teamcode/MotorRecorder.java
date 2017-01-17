package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

/**
 * Created by spencersharp on 1/10/17.
 */
public class MotorRecorder extends TrashCanProtoOpMode
{
    public boolean hasStepEnded()
    {

    }

    public void saveProcedureToFile(ArrayList<Step> procedure)
    {
        
    }

    public void runOpMode()
    {
        ArrayList<Step> procedure = new ArrayList<Step>();
        while(opModeIsActive())
        {
            Step step = new Step();
            while(!hasStepEnded())
            {
                Point p = new Point();
                step.concat(p);
                idle();
            }
        }
        saveProcedureToFile(procedure);
    }
}
