package Rendering;

import static Util.GLCONST.ARRAY_BUFFER;
import static Util.GLCONST.DYNAMIC;
import static Util.GLCONST.FLOAT;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

import Managers.ShaderManager;
import Util.Debug;
import Util.Quaternionf;
import Util.Vectorf3;

public class InstantiableStaticEntity extends Renderable implements DestructionListener {

	public final LinkedList<Instance> active = new LinkedList<Instance>();
	public final LinkedList<Instance> inactive = new LinkedList<Instance>();

	private final VBOVertexData[] vertexData;
	private final VBOIndexData indexData;
	private final int primitive;
	private FloatBuffer transformData = BufferUtils.createFloatBuffer(0);

	public InstantiableStaticEntity(VBOVertexData[] vertexData, VBOIndexData indexData, int type) {
		this.vertexData = vertexData;
		this.indexData = indexData;
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
		String transformVBO = toString();

		enableState();
		VBO transformData = VBOManager.getVBO(transformVBO);
		if (transformData == null)
			transformData = VBOManager.createVBO(transformVBO, FLOAT);
		if (transformData == null) {
			Debug.log(Debug.INSTANCE_MANAGEMENT, "Failure to allocate transform buffer.");
			disableState();
			return;
		}
		// TODO cache these calls to save time. Update cache whenever instanceProgram changes.
		int pos = ShaderManager.instanceProgram.getAttribute("position");
		int rot = ShaderManager.instanceProgram.getAttribute("rotation");
		int sca = ShaderManager.instanceProgram.getAttribute("scale");
		transformData.bufferData(ARRAY_BUFFER, getTransformData(), DYNAMIC);
		GL20.glVertexAttribPointer(pos, 3, GL11.GL_FLOAT, false, 40, 0);
		GL20.glVertexAttribPointer(rot, 4, GL11.GL_FLOAT, false, 40, 12);
		GL20.glVertexAttribPointer(sca, 3, GL11.GL_FLOAT, false, 40, 28);
		GL20.glEnableVertexAttribArray(pos);
		GL20.glEnableVertexAttribArray(rot);
		GL20.glEnableVertexAttribArray(sca);
		GL33.glVertexAttribDivisor(pos, 1);
		GL33.glVertexAttribDivisor(rot, 1);
		GL33.glVertexAttribDivisor(sca, 1);
		GL31.glDrawElementsInstanced(primitive, indexData.getSize(), GL11.GL_UNSIGNED_INT, 0, active.size());
		disableState();
	}

	private void enableState() {
		for (VBOVertexData vertexDatum : vertexData)
			vertexDatum.enableBuffer();
		indexData.enableBuffer();
	}

	private void disableState() {
		VBOIndexData.disableBuffer();
		for (VBOVertexData vertexDatum : vertexData)
			vertexDatum.disableBuffer();
	}

	private FloatBuffer getTransformData() {
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
				transformData.put(instance.values);
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

	@Override
	public void instanceDestroyed(Instance instance) {
		removeFromActive(instance);
		inactive.remove(instance);
	}

}
