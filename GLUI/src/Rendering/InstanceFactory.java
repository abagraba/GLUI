package Rendering;

import java.util.LinkedList;

import Util.Quaternionf;
import Util.Vectorf3;

public class InstanceFactory implements DestructionListener {

	private static InstanceFactory factory = new InstanceFactory();
	private static LinkedList<Instance> available = new LinkedList<Instance>();

	private InstanceFactory() {}

	/**
	 * Initializes and returns a new Instance of the specified entity.
	 * @param entity entity that this is an instance of.
	 * @param position initial position.
	 * @param rotation initial rotation.
	 * @return the new instance.
	 */
	public static Instance newInstance(InstantiableStaticEntity entity, Vectorf3 position, Quaternionf rotation,
			Vectorf3 scale) {
		Instance instance = getInstance().set(entity, position, rotation, scale);
		instance.addDestructionListener(factory);
		instance.addDestructionListener(entity);
		instance.activate();
		return instance;
	}

	/**
	 * Initializes and returns a new Instance of the specified entity.
	 * @param entity entity that this is an instance of.
	 * @return the new instance.
	 */
	public static Instance newInstance(InstantiableStaticEntity entity) {
		return newInstance(entity, new Vectorf3(), new Quaternionf(), new Vectorf3(1, 1, 1));
	}

	private static Instance getInstance() {
		if (available.isEmpty())
			return new Instance();
		return available.removeFirst();
	}

	@Override
	public void instanceDestroyed(Instance instance) {
		available.add(instance);
	}

}
