package PracticeCodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp

public class PracticeBot extends OpMode{
DcMotorEx leftdrive;
DcMotor rightdrive;
//DcMotor lift;
Servo myservo;

double servoPosition = 0;
boolean lastA = false;

double motorVelocity = 0;// initial velocity to set motor. (This means - 0 ticks/sec)
boolean lastPower = false; Always set boolean of the last iteration press to false. In this case (false = powered off). 

public void init(){
leftdrive = hardwareMap.get(DcMotorEx.class, "leftdrive");
rightdrive = hardwareMap.dcMotor.get("rightdrive");
//lift = hardwareMap.dcMotor.get("lift");
myservo = hardwareMap.servo.get("myservo");
leftdrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//sets the motor we are using for velocity to reset once we press initialize.

}
public void loop(){

boolean thisA = gamepad1.a;
if(thisA && !lastA){

    if(servoPosition == 0){
        servoPosition = 1;
    }
    else{servoPosition = 0;}
}
lastA = thisA;
myservo.setPosition(servoPosition);


//leftdrive.setTargetPosition(2000); // we can set  target position we want to reach when we add a velocity later on to the designated motor.


boolean thisPower = gamepad1.b;
if(thisPower && !lastPower) // if the current button press and the last iteration press do not match then do whats in the curly brackets.(!) means "does not" like how (!=) means "does not equal".
{
    if(motorVelocity == 0) //If the button presses do not match and the motor velocity is 0, then they will offset and the motor will start to run 600 ticks/sec. This can also be set to -600 for backwards direction.
{
        motorVelocity = 600;
      
    }    
    else{motorVelocity = 0;}// Else, if the button is pressed again and set back to false then the motor velocity will be set to 0.
}
leftdrive.setMode(DcMotor.RunMode.RUN_TO_POSITION); // this line should only be added in the case of when you want to have a target position like 2000 ticks, but can set the velocity to 600 ticks/sec to reach that position.
lastPower = thisPower; // After the motor button is pressed again to match, lastPower and thisPower will equal each other.
leftdrive.setVelocity(motorVelocity);// we will set our designated motor velocity to the function we called(motorVelocity) in public class.



//leftdrive.setPower(-gamepad1.left_stick_y);
rightdrive.setPower(gamepad1.right_stick_y);
/*if(gamepad1.y){lift.setPower(.5);}
else if(gamepad1.a){lift.setPower(-.4);}
else{lift.setPower(0);}*/



}
}
    
