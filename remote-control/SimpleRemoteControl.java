public class SimpleRemoteControl {
	Command slot = null;

	public SimpleRemoteControl() {}

	public void setCommand(Command command) {
		slot = command;
	}

	public void buttonWasPressed() {
		slot.execute();
	}
}
