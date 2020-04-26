public class RemoteControlTest {
	public static void main(String[] args) {
		// New the Simple Remote Control instance
		SimpleRemoteControl remoteControl = new SimpleRemoteControl();

		// New the light on command instance
		Light light = new Light();
		LightOnCommand lightOnCmd = new LightOnCommand(light);

		// New the garage door open command instance
		GarageDoor garageDoor = new GarageDoor();
		GarageDoorOpenCommand garageDoorOpenCmd = new GarageDoorOpenCommand(garageDoor);

		// Bind the Light On Command to Remote Control
		remoteControl.setCommand(lightOnCmd);
		remoteControl.buttonWasPressed();

		// Bind the Garage Door Open Command to Remote Control
		remoteControl.setCommand(garageDoorOpenCmd);
		remoteControl.buttonWasPressed();
	}
}
