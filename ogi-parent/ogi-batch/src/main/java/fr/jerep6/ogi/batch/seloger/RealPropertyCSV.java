package fr.jerep6.ogi.batch.seloger;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.common.collect.ImmutableList;

import fr.jerep6.ogi.persistance.bo.RealProperty;

@Getter
@ToString
public class RealPropertyCSV {
	/** Property for composite item processor */
	private RealProperty	property;

	// 1 -- 10
	@Setter
	private String			agenceId;									// OBLI. Identifiant agence fourni par nos
																		// services
	@Setter
	private String			bienReference;								// OBLI. Reference fonctionnelle du bien
	@Setter
	private String			typeAnnonce;
	@Setter
	private String			typeBien;
	@Setter
	private String			adresseCP;
	@Setter
	private String			adresseVille;
	private String			adressePays;
	private String			adresseRue;
	private String			adresseQuartier;
	private String			activiteCommerciale;

	// 11 -- 20
	@Setter
	private String			prix;
	private String			loyerParMois;								// Dans le cas d'une cession de bail
	@Setter
	private String			loyerCC;									// loyer charge comprise OUI NON
	private String			loyerHT;									// loyer hors taxe OUI NON
	@Setter
	private String			honoraires;								// OBLIG. Montant des honoraires en euros TTC
																		// pour une
																		// location
	@Setter
	private String			surface;
	@Setter
	private String			surfaceTerrain;
	@Setter
	private String			nbrePiece;
	@Setter
	private String			nbreChambre;
	@Setter
	private String			libelle;									// OBLI (64 CHAR). Libellé court. Exemple :
																		// Maison 3
																		// pièces

	// 21 -- 30
	@Setter
	private String			descriptif;								// OBLI (4000 CHAR). Ne pas mettre de caractères
																		// spéciaux
	@Setter
	private String			dateDisponibilite;							// Format JJ/MM/AAAA
	@Setter
	private String			charges;
	@Setter
	private String			etage;										// 0 pour RDC
	@Setter
	private String			etageNbre;
	private String			meuble;									// OUI NON
	@Setter
	private String			anneeConstruction;
	private String			refaitANeuf;								// OUI NON
	@Setter
	private String			nbreSalleDeBain;
	@Setter
	private String			nbreSalleEau;

	// 31 -- 40
	private String			nbreWC;
	private String			WCSepares;									// OUI NON
	private String			typeChauffage;
	private String			typeCuisine;
	@Setter
	private String			orientationSud;
	@Setter
	private String			orientationEst;
	@Setter
	private String			orientationOuest;
	@Setter
	private String			orientationNord;
	private String			nbreBalcon;
	private String			sfBalcon;

	// 41 -- 50
	private String			ascenseur;									// OUI NON
	private String			cave;										// OUI NON
	private String			nbreParking;
	private String			nbreBoxe;
	private String			digicode;									// OUI NON
	private String			interphone;
	private String			gardien;
	private String			terrasse;
	private String			prixSemaineBasse;
	private String			prixQuinzaineBasse;

	// 51 -- 60
	private String			prixMoisBasse;
	private String			prixSemaineHaute;
	private String			prixQuinzaineHaute;
	private String			prixMoisHaute;
	private String			nbrePersonnes;
	private String			typeResidence;								// (3 CHAR). Uniquement pour location de
																		// vacances
	private String			situation;									// mer, montagne, campagne ...
	private String			nbreCouvert;
	private String			nbreLitsDouble;
	private String			nbreLitsSimple;

	// 61 -- 70
	private String			alarme;
	private String			cableTV;
	private String			calme;
	private String			climatisation;
	private String			piscine;
	private String			amenagementHadicape;
	private String			animauxAccepte;
	private String			cheminee;
	private String			congelateur;
	private String			four;

	// 71 -- 80
	private String			laveVaisselle;
	private String			microOnde;
	private String			placard;
	private String			telephone;
	private String			procheLac;
	private String			procheTennis;
	private String			procheSki;
	private String			vueDegagee;
	private String			chiffreAffaire;
	private String			longeurFacade;

	// 81 -- 90
	private String			duplex;
	private String			publication;
	private String			mandatExclusif;
	private String			coupDeCoeur;
	@Setter
	private String			photo1;
	@Setter
	private String			photo2;
	@Setter
	private String			photo3;
	@Setter
	private String			photo4;
	@Setter
	private String			photo5;
	@Setter
	private String			photo6;

	// 91 -- 100
	private String			photo7;
	private String			photo8;
	private String			photo9;
	private String			titrePhoto1;
	private String			titrePhoto2;
	private String			titrePhoto3;
	private String			titrePhoto4;
	private String			titrePhoto5;
	private String			titrePhoto6;
	private String			titrePhoto7;

	// 101 -- 110
	private String			titrePhoto8;
	private String			titrePhoto9;
	private String			photoPanoramique;
	private String			urlVisiteVirtuelle;
	private String			telephoneAAfficher;
	private String			contactAAfficher;
	private String			emailAAfficher;
	private String			adresseReelleCP;
	private String			adresseReelleVille;
	private String			intercabinet;								// Utilisé dans le cas ou l’agence dispose d’un
																		// site
																		// géré
																		// par
																		// Seloger.com
																		// avec l’option d’intercabinet

	// 111 -- 120
	private String			intercabinePrive;
	@Setter
	private String			mandatNumero;
	private String			mandatDate;
	private String			mandataireNom;
	private String			mandatairePrenom;
	private String			mandataireRaisonSociale;
	private String			mandataireAdresseRue;
	private String			mandataireAdresseCP;
	private String			mandataireAdresseVille;
	private String			mandataireTelephone;

	// 121 -- 130
	private String			mandataireCommentaire;
	private String			commentairePrivee;
	private String			codeNegociateur;
	private String			langue1Code;
	private String			langue1Proximite;
	private String			langue1Libelle;
	private String			langue1Descriptif;
	private String			langue2Code;
	private String			langue2Proximite;
	private String			langue2Libelle;

	// 131 -- 140
	private String			langue2Descriptif;
	private String			langue3Code;
	private String			langue3Proximite;
	private String			langue3Libelle;
	private String			langue3Descriptif;
	private String			champPersonnalise1;
	private String			champPersonnalise2;
	private String			champPersonnalise3;
	private String			champPersonnalise4;
	private String			champPersonnalise5;

	// 141 -- 150
	private String			champPersonnalise6;
	private String			champPersonnalise7;
	private String			champPersonnalise8;
	private String			champPersonnalise9;
	private String			champPersonnalise10;
	private String			champPersonnalise11;
	private String			champPersonnalise12;
	private String			champPersonnalise13;
	private String			champPersonnalise14;
	private String			champPersonnalise15;

	// 151 -- 160
	private String			champPersonnalise16;
	private String			champPersonnalise17;
	private String			champPersonnalise18;
	private String			champPersonnalise19;
	private String			champPersonnalise20;
	private String			champPersonnalise21;
	private String			champPersonnalise22;
	private String			champPersonnalise23;
	private String			champPersonnalise24;
	private String			champPersonnalise25;

	// 161 -- 170
	@Setter
	private String			depotDeGarantie;
	private String			recent;
	@Setter
	private String			travauxAPrevoir;
	private String			photo10;
	private String			photo11;
	private String			photo12;
	private String			photo13;
	private String			photo14;
	private String			photo15;
	private String			photo16;

	// 171 -- 180
	private String			photo17;
	private String			photo18;
	private String			photo19;
	private String			photo20;
	@Setter
	private String			bienTechid;								// OBLI. Identifiant technique de l'annonce
	@Setter
	private String			dpeKwh;
	@Setter
	private String			dpeKwhClassification;
	@Setter
	private String			dpeGes;
	@Setter
	private String			dpeGesClassification;
	private String			quartierIdentifiant;

	// 181 - 190
	private String			bienSousType;
	private String			periodeDisponibilite;
	private String			periodeBasse;
	private String			periodeHaute;
	private String			viagerPrixBouquet;
	private String			viagerRenteMensuelle;
	private String			viagerAgeHomme;
	private String			viagerAgeFemme;
	private String			entree;									// OUI NON. Le bien dispose d'une entrée
	private String			residence;									// OUI NON. Location vacances uniquement.

	// 191 -- 200
	private String			parquet;									// OUI NON
	private String			visAVis;									// OUI NON
	private String			transportLigne;							// 8
	private String			transportStation;							// Opera
	private String			dureeBail;
	private String			placesEnSalle;								// Restaurant
	private String			monteCharge;
	private String			quai;
	private String			nbreBureau;
	private String			prixDroitEntree;

	// 201 -- 210
	private String			prixMasque;								// OUI / NON : Uniquement pour belles demeures :
	private String			businessLoyerAnnuelGlobal;
	private String			businessChargesAnnuellesGlobales;
	private String			businessLoyerAnnuelAuMettreCarre;
	private String			businessChargesAnnuellesAuMettreCarre;
	private String			businessChargesMensuelleHT;
	private String			businessLoyerAnnuelCC;
	private String			businessLoyerAnnuelHT;
	private String			businessChargesAnnuellesHT;
	private String			businessLoyerAnnuelAuMettreCarreCC;

	// 211 -- 220
	private String			businessLoyerAnnuelAuMettreCarreHT;
	private String			businessChargesAnnuellesAuMettreCarreHT;
	private String			businessDivisible;
	private String			businessSurfaceDivisibleMinimale;
	private String			businessSurfaceDivisibleMaximale;
	private String			surfaceSejour;
	private String			nbreVehicules;								// Uniquement parking
	private String			prixDroitBail;								// Uniquement location
	private String			valeurAchat;								// uniquement produits investissement
	private String			fdcRepartitionCA;							// uniquement fond de commerce

	// 221 -- 230
	private String			terrainAgricole;
	private String			equiementBebe;
	@Setter
	private String			terrainConstructible;
	private String			fdcResultatN2;
	private String			fdcResultatN1;
	private String			fdcResultatN;
	private String			parkingImmeuble;							// uniquement parking
	private String			parkingIsole;
	private String			viagerVenduLibre;
	private String			fdcLogementADisposition;

	// 231 -- 240
	private String			terrainEnPente;
	private String			planEau;
	private String			laveLinge;
	private String			secheLinge;
	private String			connexionInternet;
	private String			CAN2;
	private String			CAN1;
	private String			conditionFinanciere;
	private String			prestationDiverse;
	private String			longueurFacade;

	// 241 -- 250
	private String			montantRapport;
	@Setter
	private String			natureBail;								// Uniquement pour les location. Exemple :
																		// Location
																		// meublée
	private String			natureBailCommercial;
	private String			nbreTerrase;
	private String			prixHT;									// OUI NON
	private String			salleAManger;								// OUI NON
	private String			sejour;									// OUI NON
	private String			terrainDonneSurRue;						// OUI NON
	private String			immeubleDeTypeBureau;						// OUI NON
	private String			terrainViabilise;							// OUI NON

	// 251 -- 255
	private String			equipementVideo;							// OUI NON
	private String			surfaceCave;
	private String			surfaceSalleAManger;
	private String			situationCommerciale;
	private String			surfaceBureauMax;

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
		lWithNull.add(photo7);
		lWithNull.add(photo8);
		lWithNull.add(photo9);
		lWithNull.add(photo10);
		lWithNull.add(photo11);
		lWithNull.add(photo12);
		lWithNull.add(photo13);
		lWithNull.add(photo14);
		lWithNull.add(photo15);
		lWithNull.add(photo16);
		lWithNull.add(photo17);
		lWithNull.add(photo18);
		lWithNull.add(photo19);
		lWithNull.add(photo20);

		List<String> lWithoutNull = new ArrayList<>(20);

		for (String string : lWithNull) {
			if (string != null) {
				lWithoutNull.add(string);
			}
		}

		ImmutableList<String> immutable = ImmutableList.copyOf(lWithoutNull);
		return immutable;
	}
}
