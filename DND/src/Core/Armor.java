package Core;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Armor extends Item implements ACMod {

	public final String name;
	public final int armorCheck;
	public final int clas;
	public final int ac;
	public final int enchant;
	public final int dexCap;
	public final float spellFail;
	public final String[] enchants;
	public final Size size;

	public static final int LIGHT = 0, MEDIUM = 1, HEAVY = 2;

	public Armor(String name, Size size, int enchant, String[] enchants, int clas, int ac, int armorCheck, int dexCap,
			float spellFail, float price, float weight) {
		super(price, weight);
		this.name = name;
		this.enchant = enchant;
		this.enchants = enchants;
		this.clas = clas;
		this.ac = ac;
		this.armorCheck = armorCheck;
		this.dexCap = dexCap;
		this.spellFail = spellFail;
		this.size = size;
	}

	@Override
	public int getACMod(AC ac) {
		if (ac == AC.TOUCH)
			return 0;
		return this.ac;
	}

	/**
	 * Gets the Armor described by the String.
	 * @param desc description of the armor. Format of: "Size (+1)? Enchantments Name". Name is specified by a single
	 *        word that represents the id of the Armor definition.
	 * @return the Armor described by the String.
	 */
	public static Armor getArmor(String desc) {
		String[] data = desc.split("\\s+");
		int enchant = 0;
		int offset = 1;
		if (data[1].matches("\\+[0-9]+")) {
			enchant = Integer.parseInt(data[1].substring(1));
			offset++;
		}
		String[] enchantments = new String[data.length - (offset + 1)];
		for (int i = 2; i < data.length - 1; i++)
			enchantments[i - 2] = data[i];
		return readArmor(Size.valueOf(data[0]), enchant, enchantments, data[data.length - 1]);
	}

	private static Armor readArmor(Size size, int enchant, String[] enchants, String name) {
		// FIXME unhardcode url
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("src/Core/Armor.xml");
		}
		catch (SAXException e1) {
			e1.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		NodeList list = doc.getElementsByTagName("Armor");
		for (int i = 0; i < list.getLength(); i++) {
			Element e = (Element) list.item(i);
			if (e.getAttribute("id").equals(name)) {
				String nam = e.getElementsByTagName("Name").item(0).getTextContent();
				int clas = getClas(e.getElementsByTagName("Class").item(0).getTextContent());
				int ac = Integer.parseInt(e.getElementsByTagName("AC").item(0).getTextContent());
				int dexCap = Integer.parseInt(e.getElementsByTagName("DEXCap").item(0).getTextContent());
				int armorCheck = Integer.parseInt(e.getElementsByTagName("ArmorCheck").item(0).getTextContent());
				float spellFail = Float.parseFloat(e.getElementsByTagName("SpellFail").item(0).getTextContent());
				float price = Float.parseFloat(e.getElementsByTagName("Price").item(0).getTextContent());
				float weight = Float.parseFloat(e.getElementsByTagName("Weight").item(0).getTextContent());

				price *= size.costMult();
				price += size.costAdd();

				weight *= size.weightMult();

				if (size.ordinal() < Size.Small.ordinal())
					ac /= 2;

				return new Armor(nam, size, enchant, enchants, clas, ac, armorCheck, dexCap, spellFail, price, weight);
			}
		}
		return null;

	}

	private static int getClas(String s) {
		if (s.equals("Light"))
			return LIGHT;
		if (s.equals("Medium"))
			return MEDIUM;
		if (s.equals("Heavy"))
			return HEAVY;
		return -1;
	}

	@Override
	public String toString() {
		String newline = System.getProperty("line.separator");
		String s = size.toString() + " +" + enchant;
		for (String enchantz : enchants)
			s += " " + enchantz;
		s += " " + name + newline;
		s += (clas == LIGHT ? "LIGHT" : clas == MEDIUM ? "MEDIUM" : clas == HEAVY ? "HEAVY" : "Invalid class.") + newline;
		s += "AC: " + ac + newline + "DEX Cap: " + dexCap + newline + "Armor Check Penalty: " + armorCheck + newline
				+ "Spell Fail chance: " + spellFail + newline;
		s += price + "g" + newline;
		s += weight + "lb";
		return s;
	}

	public static void main(String[] args) {
		System.out.println(getArmor("Small +1 Leather"));
	}

}
