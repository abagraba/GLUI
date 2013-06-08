package GLUICore;

/**
 * Event class. Defines the core components of an Event.
 */
public class Event {

	protected boolean consumed;

	/**
	 * Returns whether or not the event has been consumed and should be used.
	 * @return state of consumption.
	 */
	public boolean isConsumed() {
		return consumed;
	}

	/**
	 * Consume the event. Consumed events should thrown a {@link GLUICore.Event.ConsumedException} if accessed after
	 * consumption.
	 */
	public void consume() {
		consumed = true;
	}

	/**
	 * Runtime Exception thrown when there is an invalid access to a consumed {@link GLUICore.Event}.
	 */
	public static final class ConsumedException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a ConsumedException with the provided String.
		 * @param error error String.
		 */
		public ConsumedException(String error) {
			super(error);
		}
	}
}
