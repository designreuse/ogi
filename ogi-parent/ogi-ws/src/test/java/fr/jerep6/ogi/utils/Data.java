package fr.jerep6.ogi.utils;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Diagnosis;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Type;

public class Data {
	private static RealPropertyLivable	farm;
	private static Address				addressTyrosse;
	private static Category				categoryHouse;

	static {
		// ##### Diagnosis #####
		Diagnosis diag1 = new Diagnosis();
		diag1.setTechid(1);
		diag1.setLabel("Termite");

		Diagnosis diag2 = new Diagnosis();
		diag2.setTechid(1);
		diag2.setLabel("Electricité");

		Set<Diagnosis> diags = new HashSet<>();
		diags.add(diag1);
		diags.add(diag2);

		// ##### Category #####
		categoryHouse = new Category();
		categoryHouse.setTechid(1);
		categoryHouse.setCode(EnumCategory.HOUSE);
		categoryHouse.setLabel("Maison");
		categoryHouse.setDiagnosis(diags);

		// ##### Type #####
		Type t1 = new Type();
		t1.setTechid(1);
		t1.setLabel("Ferme");
		t1.setCategory(categoryHouse);

		Type t2 = new Type();
		t2.setTechid(2);
		t2.setLabel("Villa");
		t2.setCategory(categoryHouse);

		// ##### Equipments #####
		Equipment eqpt1 = new Equipment();
		eqpt1.setTechid(1);
		eqpt1.setCategory(categoryHouse);
		eqpt1.setLabel("Interphone");

		Equipment eqpt2 = new Equipment();
		eqpt2.setTechid(2);
		eqpt2.setCategory(categoryHouse);
		eqpt2.setLabel("Cheminée");

		// ##### Descriptions #####
		Description d1 = new Description();
		d1.setTechid(1);
		d1.setLabel("Superbe ferme !!!");
		d1.setType(EnumDescriptionType.SHOP_FRONT);

		Description d2 = new Description();
		d2.setTechid(2);
		d2.setLabel("Description site web");
		d2.setType(EnumDescriptionType.WEBSITE_OWN);

		// ##### Adress #####
		addressTyrosse = new Address();
		addressTyrosse.setNumber("27bis");
		addressTyrosse.setStreet("Avenue Nationale");
		addressTyrosse.setPostalCode("40230");
		addressTyrosse.setCity("Saint Vincent de Tyrosse");

		farm = new RealPropertyLivable("ref1", categoryHouse, t1);
		farm.setEquipments(Sets.newHashSet(eqpt1, eqpt2));
		farm.setLandArea(3400);
		farm.setHousingEstate(false);
		farm.setDescriptions(Sets.newHashSet(d1, d2));
		farm.setAddress(addressTyrosse);
	}

	public static Address getAddressTyrosse() {
		return addressTyrosse;
	}

	public static Category getCategoryHouse() {
		return categoryHouse;
	}

	public static RealPropertyLivable getFarm() {
		return farm;
	}

}
