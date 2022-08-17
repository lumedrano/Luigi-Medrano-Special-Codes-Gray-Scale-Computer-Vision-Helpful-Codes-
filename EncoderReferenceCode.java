package PracticeCodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
@Disabled
public class EncoderReferenceCode extends LinearOpMode{
DcMotor leftfront;
DcMotor rightfront;
DcMotor leftrear;
DcMotor rightrear;
Servo leftmover;
Servo rightmover;


private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 103.6 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     SLOW_DRIVE_SPEED             = 0.32;// is the speed when going straight in encoder drive
    static final double     FAST_DRIVE_SPEED            = 0.5;
    static final double     STRAFE_SPEED            = 0.2;
    static final double     TURN_SPEED              = 0.5;// is the speed when turning in encoder drive
@Override
public void runOpMode(){
leftfront = hardwareMap.dcMotor.get("leftfront");
leftfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
leftfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

rightfront = hardwareMap.dcMotor.get("rightfront");
rightfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
rightfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

leftrear = hardwareMap.dcMotor.get("leftrear");
leftrear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
leftrear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

rightrear = hardwareMap.dcMotor.get("rightrear");
rightrear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
rightrear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    leftrear.setDirection(DcMotorSimple.Direction.REVERSE);

leftmover = hardwareMap.servo.get("leftmover");
rightmover = hardwareMap.servo.get("rightmover");

leftmover.setPosition(1);
rightmover.setPosition(.2);

 telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
        } // keeps the phones busy while initialized to keep phones from disconnecting randomly during competition
    
      SlideControl(SLOW_DRIVE_SPEED, 20, 5.0);
drive(0, 0, 0, 0, 500000);
}

  public void encoderDrive(double speed, double leftfrontInches, double rightfrontInches, double leftrearInches, double rightrearInches, double timeoutS)// method to use encoders and either use drive or turn speed for distance and degree with encoders.
 {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;
        leftfront.setPower(0);
        rightfront.setPower(0);
        leftrear.setPower(0);
        rightrear.setPower(0);

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = leftfront.getCurrentPosition() + (int)(leftfrontInches * COUNTS_PER_INCH);
            newRightFrontTarget =rightfront.getCurrentPosition() + (int)(rightfrontInches * COUNTS_PER_INCH);
            newLeftRearTarget = leftrear.getCurrentPosition() + (int) (leftrearInches * COUNTS_PER_INCH);
            newRightRearTarget = rightrear.getCurrentPosition() + (int) (rightrearInches * COUNTS_PER_INCH);
            leftfront.setTargetPosition(newLeftFrontTarget);
            rightfront.setTargetPosition(newRightFrontTarget);
            leftrear.setTargetPosition(newLeftRearTarget);
            rightrear.setTargetPosition(newRightRearTarget);
            

            // Turn On RUN_TO_POSITION
            leftfront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightfront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftrear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightrear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            

            // reset the timeout time and start motion.
            runtime.reset();
            leftfront.setPower(Math.abs(speed));
            rightfront.setPower(Math.abs(speed));
            leftrear.setPower(Math.abs(speed));
            rightrear.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

             while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (leftfront.isBusy() && rightfront.isBusy() && rightrear.isBusy() && leftrear.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftFrontTarget, newLeftRearTarget, newRightRearTarget,  newRightFrontTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            leftfront.getCurrentPosition(), 
                                            leftrear.getCurrentPosition(),
                                            rightrear.getCurrentPosition(),
                                            rightfront.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftfront.setPower(0);
            leftrear.setPower(0);
            rightfront.setPower(0);
            rightrear.setPower(0);

            // Turn off RUN_TO_POSITION
            leftfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftrear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightrear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
}

public void drive(double leftfrontpower, double rightfrontpower, double leftrearpower, double rightrearpower, int msec){
leftfront.setPower(leftfrontpower);
rightfront.setPower(rightfrontpower);
leftrear.setPower(leftrearpower);
rightrear.setPower(rightrearpower);
sleep(msec);
leftfront.setPower(0);
rightfront.setPower(0);
leftrear.setPower(0);
rightrear.setPower(0);
leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
leftrear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
rightrear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
}




public void SlideControl(double speed, double leftfrontInches, double timeoutS)// method to use encoders and either use drive or turn speed for distance and degree with encoders.
 {
        int newLeftFrontTarget;
       
        leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
       

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = leftfront.getCurrentPosition() + (int)(leftfrontInches * COUNTS_PER_INCH);
           
            leftfront.setTargetPosition(newLeftFrontTarget);
            
            

            // Turn On RUN_TO_POSITION
            leftfront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            

            // reset the timeout time and start motion.
            runtime.reset();
            leftfront.setPower(Math.abs(speed));
           

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

             while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (leftfront.isBusy())) {

                // Display it for the driver.
               
            }

            // Stop all motion;
            leftfront.setPower(0);
            
            // Turn off RUN_TO_POSITION
            leftfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           
        }
}
}
