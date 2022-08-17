package CoachBot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class Encoders extends LinearOpMode{
    DcMotor leftdrive;
    DcMotor rightdrive;
    
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_TETRIX    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_TETRIX * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.2;// is the speed when going straight in encoder drive
    static final double     TURN_SPEED              = 0.5;// is the speed when turning in encoder drive
    
    @Override
    public void runOpMode() throws InterruptedException{
        leftdrive = hardwareMap.dcMotor.get("leftdrive");
        leftdrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftdrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        rightdrive = hardwareMap.dcMotor.get("rightdrive");
         rightdrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         rightdrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightdrive.setDirection(DcMotor.Direction.REVERSE);
        
        
         telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
        } // keeps the phones busy while initialized to keep phones from disconnecting randomly during competition
        
        encoderDrive(DRIVE_SPEED, 5, 5, 5.0);
    }
        public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS)// method to use encoders and either use drive or turn speed for distance and degree with encoders.
 {
        int newLeftTarget;
        int newRightTarget;
        leftdrive.setPower(0);
        rightdrive.setPower(0);

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftdrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget =rightdrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftdrive.setTargetPosition(newLeftTarget);
            rightdrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftdrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightdrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftdrive.setPower(Math.abs(speed));
            rightdrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (leftdrive.isBusy() && rightdrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            leftdrive.getCurrentPosition(),
                                            rightdrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftdrive.setPower(0);
            rightdrive.setPower(0);

            // Turn off RUN_TO_POSITION
            leftdrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightdrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        

    }

                             }
}
