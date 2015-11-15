package fr.jerep6.ogi.batch.annoncesjaunes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import fr.jerep6.ogi.persistance.bo.RealProperty;

@Getter
@ToString
public class RealPropertyCSV {
	/** Property for composite item processor */
	private RealProperty	property;

	// 1 -- 10
	@Setter
	private String			agenceId;						// OBLI. Identifiant agence fourni par nos services
	@Setter
	private String			bienReference;					// OBLI. Reference fonctionnelle du bien
	@Setter
	private String			typeAnnonce; 					// OBLI. Vente, Location, Location de vacances
	@Setter
	private String			typeBien;						// OBLI. Appartement, Chalet, Chambres d'hôtes - gîte, Château - propriété, Ferme, Hôtel particulier, Immeuble, Loft - atelier, Maison - villa, Parking, Terrain, Local Commercial, Autres
	@Setter
	private String mandatExclusif;
	@Setter
	private String titre;
	@Setter
	private String descriptif;
	@Setter
	private String dateDisponibilite;
	private String adresseRue;
	@Setter
	private String adresseCP;								// OBLI.
	
	// 11 -- 20
	@Setter
	private String adresseVille;
	private String adressePays;
	private String adresseQuartier;
	private String urlVisiteVirtuelle;
	private String descriptifVisiteVirtuelle;
	private String contactNom;
	private String contactTelFixe;
	private String contactTelMobile;
	private String contactEmail;
	private String anciennete;

	// 21 -- 30
	@Setter
	private String etatGeneral;								// Neuf, Très bon état, Bon état, Rénové, Travaux à prévoir
	private String refaitNeuf;
	@Setter
	private String chauffage;								// Collectif, Collectif fuel, Collectif gaz, Electrique, Fuel, Gaz, Gaz + électrique, Individuel électrique, Individuel gaz, Sol, Solaire, Autres
	private String situationLocationVacances;
	
	@Setter
	private String loyerCC;									// OBLI. soit l'un
	@Setter
	private String loyerSC;									// OBLI. soit l'autre 
	@Setter
	private String charges;									// OBLI. saisir si loyer sans charge
	@Setter
	private String depotDeGarantie;
	private String taxeHabitation;
	@Setter
	private String prix;									// OBLI. si vente

	// 31 -- 40
	private String rente;
	private String taxeFonciere;
	@Setter
	private String honoraires;
	@Setter
	private String anneeConstruction;
	@Setter
	private String meuble;
	private String duplex;
	private String triplex;
	private String loft;
	private String sansVisaVis;
	private String vueMer;

	// 41 -- 50
	@Setter
	private String etage;
	@Setter
	private String etageNbre;
	@Setter
	private String surfaceHabitable;
	private String surfaceTotale;
	private String surfaceCarrez;
	private String surfaceSejour;
	private String surfaceSalleAManger;
	private String surfaceTerrassesBalcons;
	private String surfaceCave;
	@Setter
	private String nbreSalleDeBain;

	// 51 -- 60
	@Setter
	private String nbreSalleEau;
	@Setter
	private String nbreWC;
	@Setter
	private String nbrePiece;
	@Setter
	private String nbreChambre;
	private String nbreBureau;
	private String doubleSejour;
	private String denierEtage;
	private String mitoyenne;
	private String plainPied;
	private String avecEtage;
	
	// 61 -- 70
	private String avecSousSol;
	@Setter
	private String orientationSud;
	@Setter
	private String orientationOuest;
	@Setter
	private String orientationNord;
	@Setter
	private String orientationEst;
	private String interphone;
	private String digicode;
	private String alarme;
	private String ascenseur;
	private String gardien;

	// 71 -- 80
	private String cheminee;
	private String parquet;
	private String terrasse;
	private String climatisation;
	private String balcon;
	private String jardin;
	private String piscine;
	private String tennis;
	private String garage;
	private String parking;

	// 81 -- 90
	private String grenier;
	private String cave;
	private String placards;
	private String cuisineEquipee;
	private String cuisineAmericaine;
	@Setter
	private String surfaceTerrain;
	@Setter
	private String terrainConstructible;
	private String capaciteAccueil;
	private String prixSemaineHaute;
	private String prixQuinzaineHaute;

	// 91 -- 100
	private String prixMoisHaute;
	private String prixSemaineBasse;
	private String prixQuinzaineBasse;
	private String prixMoisBasse;
	private String dateDebutLocation;
	private String dateFinLocation;
	private String internet;
	private String telephone;
	private String television;
	private String cableSatellite;

	// 101 -- 110
	private String laveLinge;
	private String laveVaisselle;
	private String congelateur;
	private String lingeMaison;
	private String four;
	private String microonde;
	private String barbecue;
	private String equipementBebe;
	private String femmeMenage;
	private String animauxAutorises;

	// 111 -- 120
	private String nonFumeur;
	private String chequesVacances;
	private String procheSki;
	private String amenagementHandicapes;
	private String dureeMinimale;
	@Setter
	private String photo1;
	@Setter
	private String photo2;
	@Setter
	private String photo3;
	@Setter
	private String photo4;
	@Setter
	private String photo5;

	// 121 -- 130
	@Setter
	private String photo6;;
	@Setter
	private String dpeKwh;
	@Setter
	private String dpeKwhClassification;
	@Setter
	private String dpeGes;
	@Setter
	private String dpeGesClassification;
	private String adresseVilleInternet;
	private String adresseCPInternet;
	private String dpeCategorie;
	private String ventePourcentageHonoraires;
	@Setter
	private String copropriete;									// O, Ne renseigner que si la valeur est OUI = O
	
	// 131 -- 140
	@Setter
	private String coproprieteNbreLots;
	@Setter
	private String coproprieteChargeAnnuelle;
	@Setter
	private String coproprieteSyndicatProcedure;
	private String statutAgentCommercial;

	public RealPropertyCSV(RealProperty property) {
		super();
		this.property = property;
	}

	public List<String> getPhotos() {
		List<String> lWithNull = new ArrayList<>(20);
		lWithNull.add(photo1);
		lWithNull.add(photo2);
		lWithNull.add(photo3);
		lWithNull.add(photo4);
		lWithNull.add(photo5);
		lWithNull.add(photo6);

		return lWithNull.stream().filter(p -> p != null).collect(Collectors.<String> toList());
	}
}
