package CoachBot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class AnotherIMU extends LinearOpMode{
private ElapsedTime runtime = new ElapsedTime();
private DcMotor leftdrive = null;
private DcMotor rightdrive = null;
private BNO055IMU imu;

public void drive(double leftdrivepower, double rightdrivepower, int msec) throws InterruptedException {
    leftdrive.setPower(leftdrivepower);
    rightdrive.setPower(rightdrivepower);
    sleep(msec);
    
}
    
    @Override
public void runOpMode() throws InterruptedException{
    leftdrive = hardwareMap.dcMotor.get("leftdrive");
    rightdrive = hardwareMap.dcMotor.get("rightdrive");
    rightdrive.setDirection(DcMotorSimple.Direction.REVERSE);
    
    
    imu = hardwareMap.get(BNO055IMU.class, "imu");
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    imu.initialize(parameters);
    
    double forward = 0;
        double turn = 0;
        
        Orientation angles = imu.getAngularOrientation();
        telemetry.addData("angles", angles.toString());
        telemetry.update();
        
        double heading = angles.firstAngle;
    waitForStart();
    //driveHeadingTime(0.25, -90, 6); //goes a quarter of motors power at a -90 degree angle and goes straight for 6 seconds.
    driveHeading(.3, -90);//should turn at a -90 degree angle at .3 percent of motors power.
    

    
}
public void driveHeading(double throttle, double heading) {
        // get the current angle of the robot
        double Z = imu.getAngularOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.DEGREES).firstAngle;
     telemetry.addData("Big degree Z", Z);
         telemetry.update();
         double turn = (Z - heading) * .05;
         leftdrive.setPower(throttle + turn);
        rightdrive.setPower(throttle - turn);
}
  // This method drives the robot at a given speed and heading for a specified period of time
    public void driveHeadingTime(double throttle, double heading, double time) {
        runtime.reset();
        while (opModeIsActive()) {
            if (runtime.seconds() > time) break; //change to msec to see if it will stop the breaking and swirving to go smoother on straights.
            driveHeading(throttle, heading);
        }
        

}}

    
