package fr.jerep6.ogi.utils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Diagnosis;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.bo.RealPropertyDiagnosis;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.bo.id.DiagnosisRealPropertyId;

public class Data {
	private static RealPropertyLivable	farm;
	private static Address				addressTyrosse;
	private static Category				categoryHouse;
	private static Type					typeFarm;

	static {
		// ##### Diagnosis #####
		Diagnosis diagTermite = new Diagnosis();
		diagTermite.setTechid(1);
		diagTermite.setLabel("Termite");

		Diagnosis diagElectric = new Diagnosis();
		diagElectric.setTechid(1);
		diagElectric.setLabel("Electricité");

		Set<Diagnosis> diags = new HashSet<>();
		diags.add(diagTermite);
		diags.add(diagElectric);

		// ##### Category #####
		categoryHouse = new Category();
		categoryHouse.setTechid(1);
		categoryHouse.setCode(EnumCategory.HOUSE);
		categoryHouse.setLabel("Maison");
		categoryHouse.setDiagnosis(diags);

		// ##### Type #####
		typeFarm = new Type();
		typeFarm.setTechid(1);
		typeFarm.setLabel("Ferme");
		typeFarm.setCategory(categoryHouse);

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

		// ##### Address #####
		addressTyrosse = new Address();
		addressTyrosse.setNumber("27bis");
		addressTyrosse.setStreet("Avenue Nationale");
		addressTyrosse.setPostalCode("40230");
		addressTyrosse.setCity("Saint Vincent de Tyrosse");

		farm = new RealPropertyLivable("ref1", categoryHouse, typeFarm);
		farm.setEquipments(Sets.newHashSet(eqpt1, eqpt2));
		farm.setLandArea(3400F);
		farm.setHousingEstate(false);
		farm.setDescriptions(Sets.newHashSet(d1, d2));
		farm.setAddress(addressTyrosse);

		RealPropertyDiagnosis d = new RealPropertyDiagnosis();
		d.setDate(Calendar.getInstance());
		d.setPk(new DiagnosisRealPropertyId(farm, diagTermite));
		farm.setDiagnosisProperty(ImmutableSet.of(d));
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

	public static Type getTypeFarm() {
		return typeFarm;
	}

}
