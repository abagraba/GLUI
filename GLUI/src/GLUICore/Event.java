package GLUICore;

public class Event {

	private boolean consumed;

	public boolean isConsumed() {
		return consumed;
	}

	public void consume() {
		consumed = true;
	}
}
