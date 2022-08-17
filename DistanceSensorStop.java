package PracticeCodes; 

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous /*This code uses the 2mDistanceSensor to read a certain distance whether it is in CM,IN,METER, or MM. 
               When you start the Autonomous, as long as the distance is over 7 Inches then the motors will keep
               spinning. If the distance between the sensor and an object are less than or equal to 7 then the motors will 
               power off.*/

public class DistanceSensorStop extends LinearOpMode{
DcMotor leftdrive;
DcMotor rightdrive;
DistanceSensor sensorDistance;
@Override
public void runOpMode(){
 leftdrive = hardwareMap.dcMotor.get("leftdrive");
 rightdrive = hardwareMap.dcMotor.get("rightdrive");
 rightdrive.setDirection(DcMotorSimple.Direction.REVERSE);
 sensorDistance = hardwareMap.get(DistanceSensor.class, "sensorDistance");
  Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)sensorDistance;
telemetry.addData(">>", "Press start to continue");
telemetry.update();
waitForStart();

 while (opModeIsActive() && sensorDistance.getDistance(DistanceUnit.INCH)>7){

leftdrive.setPower(0.3);
rightdrive.setPower(0.3);


if(sensorDistance.getDistance(DistanceUnit.INCH) <= 7){
telemetry.addData("STATUS", "STOP!!!");
telemetry.update();
leftdrive.setPower(0);
rightdrive.setPower(0);
}
// generic DistanceSensor methods.
telemetry.addData("range", String.format("%.01f in", sensorDistance.getDistance(DistanceUnit.INCH)));
telemetry.update();
}
}
}
