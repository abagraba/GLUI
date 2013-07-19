package Rendering;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

import Managers.ShaderManager;
import Util.Quaternionf;
import Util.Vectorf3;

public class InstantiableStaticEntity extends Renderable {

	boolean test = false;

	public final LinkedList<Instance> active = new LinkedList<Instance>();
	public final LinkedList<Instance> inactive = new LinkedList<Instance>();

	private final VBOVertexData[] vertexData;
	private final VBOIndexData indexData;
	private final InstanceData[] instanceData;
	private final int primitive;
	private FloatBuffer transformData = BufferUtils.createFloatBuffer(0);

	public InstantiableStaticEntity(VBOVertexData[] vertexData, VBOIndexData indexData, InstanceData[] instanceData, int type) {
		this.vertexData = vertexData;
		this.indexData = indexData;
		this.instanceData = instanceData;
		primitive = type;
	}

	/**
	 * Creates an instance of this object with the specified position and rotation.
	 * @param position initial position.
	 * @param rotation initial rotation.
	 * @return new instance.
	 * @see InstanceFactory#newInstance(InstantiableStaticEntity, Vectorf3, Quaternionf, Vectorf3)
	 */
	public Instance createInstance(Vectorf3 position, Quaternionf rotation, Vectorf3 scale) {
		return InstanceFactory.newInstance(this, position, rotation, scale);
	}

	protected void activateInstance(Instance instance) {
		inactive.remove(instance);
		active.add(instance);
	}

	protected void deactivateInstance(Instance instance) {
		removeFromActive(instance);
		inactive.add(instance);
	}

	protected void drawInstances() {
		ShaderManager.instanceProgram.use();
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

	public FloatBuffer getRPSData() {
		boolean newArray = false;
		if (transformData.capacity() < active.size() * 10) {
			transformData = BufferUtils.createFloatBuffer(active.size() * 10);
			newArray = true;
		}
		int i = 0;
		boolean needReposition = false;
		for (Instance instance : active) {
			if (newArray || instance.modified) {
				if (needReposition)
					transformData.position(i * 10);
				transformData.put(instance.getPositionArray());
				transformData.put(instance.getRotationArray());
				transformData.put(instance.getScaleArray());
			}
			else
				needReposition = true;
			i++;
		}
		transformData.flip();
		return transformData;
	}

	private void removeFromActive(Instance i) {
		if (active.contains(i)) {
			Instance last = active.removeLast();
			if (last == i)
				return;
			active.set(active.indexOf(i), last);
		}
	}

	@Override
	public void render() {
		drawInstances();
	}

	protected void instanceDestroyed(Instance instance) {
		removeFromActive(instance);
		inactive.remove(instance);
	}

}
