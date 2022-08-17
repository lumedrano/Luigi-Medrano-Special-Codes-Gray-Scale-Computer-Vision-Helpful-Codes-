package PracticeCodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class ArmEncoderAuto extends LinearOpMode{
DcMotor armmotor;
DcMotor secondarmmotor;
DcMotor rightfront;
@Override
public void runOpMode() throws InterruptedException{
    armmotor = hardwareMap.dcMotor.get("armmotor");
    armmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    armmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    armmotor.setDirection(DcMotor.Direction.REVERSE);
    
    secondarmmotor = hardwareMap.dcMotor.get("secondarmmotor");
    secondarmmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    secondarmmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    
    rightfront = hardwareMap.dcMotor.get("rightfront");
    
 waitForStart();
 //rightfront.setPower(.3);
 //sleep(500);
 //rightfront.setPower(0);
 //sleep(500);
 armmotor.setTargetPosition(500);
 secondarmmotor.setTargetPosition(-500);
armmotor.setPower(.8);
secondarmmotor.setPower(-.8);
 armmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
 secondarmmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
 sleep(5000);
 
while(opModeIsActive() && armmotor.isBusy() && secondarmmotor.isBusy()){
   // rightfront.setPower(.2);
    //sleep(1000);
    //rightfront.setPower(-.2);
    //sleep(2000);
    
    
}
}
}
