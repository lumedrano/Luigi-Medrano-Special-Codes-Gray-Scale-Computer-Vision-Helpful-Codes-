package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


@Autonomous(name="IMUTest", group="Linear Opmode")
@Disabled
public class IMUReferenceCode extends LinearOpMode {
      

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftfront = null;
    private DcMotor rightfront = null;
    private DcMotor leftrear = null;
    private DcMotor rightrear = null;
    private BNO055IMU imu = null;

    @Override
    public void runOpMode() {
        
        telemetry.addData("You are big cool ;)", "Don't init before match uWu uWu xD xD");
        telemetry.addData("Status", "force top commencing");
        telemetry.update();
        
      

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftfront  = hardwareMap.dcMotor.get("leftfront");
        rightfront = hardwareMap.dcMotor.get("rightfront");
        leftrear = hardwareMap.dcMotor.get("leftrear");
        rightrear = hardwareMap.dcMotor.get("rightrear"); 
        //Upmotor = hardwareMap.get(DcMotor.class, "Up_Motor");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftfront.setDirection(DcMotorSimple.Direction.REVERSE);
    leftrear.setDirection(DcMotorSimple.Direction.REVERSE);

        // This code sets up the gyro sensor / IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
driveHeadingTime(.4, 90, 2.0);
leftfront.setPower(0);
rightfront.setPower(0);
leftrear.setPower(0);
rightrear.setPower(0);



    }


    // This method drives the robot at a given speed and heading for a specified period of time
    public void driveHeadingTime(double throttle, double heading, double time) {
        runtime.reset();
        while (opModeIsActive()) {
            if (runtime.seconds() > time) break;
            driveHeading(throttle, heading);
        }
    }


    // This method adjusts the speed and direction of the robot to move towards a desired heading.
    // It's generally intended to be called repeatedly from the body of a 'while' or 'for' loop.
    //    (See example in driveHeadingTime(...) above.)
    public void driveHeading(double throttle, double heading) {
        // get the current angle of the robot
        double Z = imu.getAngularOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.DEGREES).firstAngle;

        // display the robot's current angle (note: telemetry is slow)
         telemetry.addData("Big degree Z", Z);
         telemetry.update();

        // calculate how much turn is needed to approach heading
        double turn = (Z - heading) * .05;

        // change the power to the drive motors
leftfront.setPower(throttle + turn);
rightfront.setPower(throttle - turn);
leftrear.setPower(throttle + turn);
rightrear.setPower(throttle - turn);
leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
leftrear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
rightrear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        
        //leftDrive.setPower(.5);
        //rightDrive.setPower(.5);
    }
}
