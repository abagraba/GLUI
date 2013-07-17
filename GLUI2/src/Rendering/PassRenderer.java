package Rendering;

public class PassRenderer {

	public static final RenderTarget screenBuffer = new RenderTarget() {
		@Override
		protected void passInit() {}

		@Override
		protected void passFinish() {}

		@Override
		public void init() {}
	};

	private static RenderTarget renderTarget = screenBuffer;
	private static RenderTarget newTarget;
	private static boolean passDone = true;

	public static void renderTo(RenderTarget renderTarget) {
		if (PassRenderer.renderTarget == renderTarget)
			return;
		if (!passDone)
			PassRenderer.newTarget = renderTarget;
		else {
			PassRenderer.renderTarget = renderTarget;
			PassRenderer.renderTarget.init();
		}
	}

	protected static void passStart() {
		passDone = false;
		renderTarget.passInit();
	}

	protected static void passDone() {
		passDone = true;
		renderTarget.passFinish();
		if (newTarget != null) {
			renderTarget = newTarget;
			newTarget = null;
			renderTarget.init();
		}
	}

}
