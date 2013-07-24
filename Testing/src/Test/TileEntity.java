package Test;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

import Rendering.InstanceData;
import Rendering.Renderable;
import Rendering.VBOIndexData;
import Rendering.VBOVertexData;

public class TileEntity extends Renderable {

	boolean test = false;

	public final LinkedList<TileInstance> active = new LinkedList<TileInstance>();
	public final LinkedList<TileInstance> inactive = new LinkedList<TileInstance>();

	private final VBOVertexData[] vertexData;
	private VBOIndexData indexData;
	private final InstanceData[] instanceData;
	private final int primitive;

	public TileEntity(VBOVertexData[] vertexData, VBOIndexData indexData, InstanceData[] instanceData, int type) {
		this.vertexData = vertexData;
		this.indexData = indexData;
		this.instanceData = instanceData;
		primitive = type;
	}

	protected void setIndices(VBOIndexData indices) {
		indexData = indices;
	}

	protected void activateInstance(TileInstance instance) {
		inactive.remove(instance);
		active.add(instance);
	}

	protected void deactivateInstance(TileInstance instance) {
		removeFromActive(instance);
		inactive.add(instance);
	}

	protected void drawInstances() {
		enableState();
		GL31.glDrawElementsInstanced(primitive, indexData.getSize(), GL11.GL_UNSIGNED_INT, 0, active.size());
		disableState();
	}

	private void enableState() {
		for (VBOVertexData vertexDatum : vertexData)
			vertexDatum.enableBuffer();
		indexData.enableBuffer();
		for (InstanceData instanceDatum : instanceData)
			instanceDatum.enableBuffer();
	}

	private void disableState() {
		VBOIndexData.disableBuffer();
		for (VBOVertexData vertexDatum : vertexData)
			vertexDatum.disableBuffer();
	}

	private void removeFromActive(TileInstance tileInstance) {
		int index = active.indexOf(tileInstance);
		if (index != -1) {
			TileInstance last = active.removeLast();
			if (last == tileInstance)
				return;
			active.set(index, last);
		}
	}

	@Override
	public void render() {
		drawInstances();
	}

	protected void instanceDestroyed(TileInstance tileInstance) {
		removeFromActive(tileInstance);
		inactive.remove(tileInstance);
	}

}
