package fr.jerep6.ogi.service.external.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDPE;
import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessError;
import fr.jerep6.ogi.exception.technical.NetworkTechnicalException;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.framework.utils.JSONUtils;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyBuilt;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.service.external.AcimfloReponse;
import fr.jerep6.ogi.service.external.ServiceAcimflo;
import fr.jerep6.ogi.transfert.WSResult;

@Service("serviceAcimflo")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceAcimfloImpl extends AbstractService implements ServiceAcimflo {
	private final Logger	LOGGER			= LoggerFactory.getLogger(ServiceAcimfloImpl.class);

	private String			login			= "http://acimflo.local:50000/admin/verifIdentification.html";
	private String			create			= "http://acimflo.local:50000/adminVente/ajouterBienSQL.html";
	private String			update			= "http://acimflo.local:50000/adminVente/modifierBienSQL.html";
	private String			updateReferer	= "http://acimflo.local:50000/adminVente/modifierBien/${reference}.html";
	private String			verifReference	= "http://acimflo.local:50000/?c=adminBien&m=verifReference&reference=${reference}";
	private String			imgApercu		= "http://acimflo.local:50000/Biens/Vente/${reference}/thumbs/Apercu.jpg";

	private void connect(HttpClient client) throws BusinessException {
		try {
			HttpPost post = new HttpPost(login);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("login", "acimflo"));
			urlParameters.add(new BasicNameValuePair("mdp", "acimflo"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			// Execute request
			HttpResponse response = client.execute(post);
			LOGGER.info("Login to acimflo admin : response code=" + response.getStatusLine().getStatusCode());

			// Parse html to determine if connection is successful or not
			Document doc = Jsoup.parse(response.getEntity().getContent(), "UTF-8", "");
			Boolean logged = !doc.select("#menu").isEmpty();
			LOGGER.info("Login successful = " + logged);

			if (!logged) {
				throw new BusinessException(EnumBusinessError.ACIMFLO_IDENTIFIANTS_KO);
			}
		} catch (IOException e) {
			throw new NetworkTechnicalException(e);
		}
	}

	private WSResult create(HttpClient client, RealProperty prp) {
		WSResult result;
		try {
			HttpPost httpPost = new HttpPost(create);

			// FileBody uploadFilePart = new FileBody(uploadFile);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			builder.addPart("vendu", new StringBody("non", ContentType.TEXT_PLAIN));

			if (prp instanceof RealPropertyLivable) {
				RealPropertyLivable liv = (RealPropertyLivable) prp;
				builder.addPart("surfaceHabitable", new StringBody(Objects.firstNonNull(liv.getArea(), "").toString(),
						ContentType.TEXT_PLAIN));
				builder.addPart("nbreChambre", new StringBody(Objects.firstNonNull(liv.getNbBedRoom(), "").toString(),
						ContentType.TEXT_PLAIN));
				builder.addPart("nbreSDB", new StringBody(Objects.firstNonNull(liv.getNbBathRoom(), "").toString(),
						ContentType.TEXT_PLAIN));
				builder.addPart("nbreWC", new StringBody(Objects.firstNonNull(liv.getNbWC(), "").toString(),
						ContentType.TEXT_PLAIN));
			}
			builder.addPart("surfaceTerrain", new StringBody(Objects.firstNonNull(prp.getLandArea(), "").toString(),
					ContentType.TEXT_PLAIN));
			builder.addPart("prix", new StringBody(prp.getSale().getPriceFinal().toString(), ContentType.TEXT_PLAIN));
			builder.addPart("surfaceDependance", new StringBody("", ContentType.TEXT_PLAIN));
			builder.addPart("reference", new StringBody(prp.getReference(), ContentType.TEXT_PLAIN));
			builder.addPart("referenceOriginale", new StringBody(prp.getReference(), ContentType.TEXT_PLAIN));
			builder.addPart("nomVille", new StringBody(Objects.firstNonNull(prp.getAddress(), new Address()).getCity(),
					ContentType.TEXT_PLAIN));
			builder.addPart("nomStyle", new StringBody(Objects.firstNonNull(prp.getType(), new Type()).getLabel(),
					ContentType.TEXT_PLAIN));
			builder.addPart("idType", new StringBody("1", ContentType.TEXT_PLAIN));
			builder.addPart("commentaire", new StringBody(prp.getDescriptions().iterator().next().getLabel(),
					ContentType.TEXT_PLAIN));

			httpPost.setEntity(builder.build());

			HttpResponse response = client.execute(httpPost);

			// Parse html to get message
			Document doc = Jsoup.parse(response.getEntity().getContent(), "UTF-8", "");
			String msg = doc.select(".msg").html();
			LOGGER.info("Msg = " + msg);
			if (Strings.isNullOrEmpty(msg)) {
				LOGGER.error("Empty return message for updating acimflo :" + doc.toString());
				result = new WSResult("KO", doc.toString());
			} else {
				result = new WSResult("OK", msg);
			}

		} catch (IOException e) {
			LOGGER.error("Error creating property " + prp.getReference() + " on acimflo", e);
			result = new WSResult("OK", e.getMessage());
		}
		return result;
	}

	@Override
	public void createOrUpdate(Set<RealProperty> properties) {
		CookieHandler.setDefault(new CookieManager());
		HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

		connect(client);

		for (RealProperty prp : properties) {
			if (prpExist(client, prp.getReference())) {
				update(client, prp);
			} else {
				// Create property
				create(client, prp);
			}

		}
	}

	private String getType(Category category) {
		switch (category.getCode()) {
			case APARTMENT:
				return "2";
			case PLOT:
				return "3";
			case HOUSE:
			default:
				return "1";
		}
	}

	private boolean prpExist(HttpClient client, String prpReference) {
		boolean exist = false;

		try {
			HttpGet httpget = new HttpGet(verifReference.replace("${reference}", prpReference));

			HttpResponse response = client.execute(httpget);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			AcimfloReponse reponse = JSONUtils.toObject(result.toString(), AcimfloReponse.class);
			LOGGER.info("Existance of reference {} : {}", prpReference, reponse.getReponse());

			exist = reponse.getReponse();
		} catch (IOException e) {
			throw new NetworkTechnicalException(e);
		}
		return exist;
	}

	private WSResult update(HttpClient client, RealProperty prp) {
		WSResult result;
		try {
			HttpPost httpPost = new HttpPost(update);

			// FileBody uploadFilePart = new FileBody(uploadFile);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			builder.addPart("vendu", new StringBody("non", ContentType.TEXT_PLAIN));

			if (prp instanceof RealPropertyLivable) {
				RealPropertyLivable liv = (RealPropertyLivable) prp;
				builder.addPart("surfaceHabitable", new StringBody(Objects.firstNonNull(liv.getArea(), "").toString(),
						ContentType.TEXT_PLAIN));
				builder.addPart("nbreChambre", new StringBody(Objects.firstNonNull(liv.getNbBedRoom(), "").toString(),
						ContentType.TEXT_PLAIN));
				builder.addPart("nbreSDB", new StringBody(Objects.firstNonNull(liv.getNbBathRoom(), "").toString(),
						ContentType.TEXT_PLAIN));
				builder.addPart("nbreWC", new StringBody(Objects.firstNonNull(liv.getNbWC(), "").toString(),
						ContentType.TEXT_PLAIN));
			}

			builder.addPart("surfaceTerrain", new StringBody(Objects.firstNonNull(prp.getLandArea(), "").toString(),
					ContentType.TEXT_PLAIN));
			builder.addPart("prix", new StringBody(prp.getSale().getPriceFinal().toString(), ContentType.TEXT_PLAIN));
			builder.addPart("reference", new StringBody(prp.getReference(), ContentType.TEXT_PLAIN));
			builder.addPart("referenceOriginale", new StringBody(prp.getReference(), ContentType.TEXT_PLAIN));
			builder.addPart("nomVille", new StringBody(Objects.firstNonNull(prp.getAddress(), new Address()).getCity(),
					ContentType.TEXT_PLAIN));
			builder.addPart("nomStyle", new StringBody(Objects.firstNonNull(prp.getType(), new Type()).getLabel(),
					ContentType.TEXT_PLAIN));
			builder.addPart("commentaire", new StringBody(prp.getDescription(EnumDescriptionType.WEBSITE_OWN)
					.getLabel(), ContentType.TEXT_PLAIN));

			builder.addPart("idType", new StringBody(getType(prp.getCategory()), ContentType.TEXT_PLAIN));
			builder.addPart("surfaceDependance", new StringBody("", ContentType.TEXT_PLAIN));

			builder.addPart("MAX_FILE_SIZE", new StringBody("5010000", ContentType.TEXT_PLAIN));

			// ###### Photos ######

			// Il ne faut pas uploader l'image d'apercu lors d'une modification car sinon elle sera présente deux fois.
			// Une fois en Apercu.jpg et une deuxième fois sous son vrai nom
			String urlApercu = imgApercu.replace("${reference}", prp.getReference());
			HttpGet getApercu = new HttpGet(urlApercu);
			HttpResponse apercuResponse = client.execute(getApercu);
			LOGGER.info("Update acimflo prp {} : response code for {} = {}", urlApercu, apercuResponse.getStatusLine()
					.getStatusCode());
			boolean uploadApercu = apercuResponse.getStatusLine().getStatusCode() != 200;

			Integer apercu = 1;
			Integer i = 1;
			for (fr.jerep6.ogi.persistance.bo.Document aPhoto : prp.getPhotos()) {
				Path p = aPhoto.getAbsolutePath();
				ContentType mime = ContentType.create(Files.probeContentType(p));

				// Upload photo number 1 only if apercu.jpg doesn't exist
				if (uploadApercu && aPhoto.getOrder().equals(1) || !aPhoto.getOrder().equals(1)) {
					builder.addPart("photos[]", new FileBody(p.toFile(), mime, p.getFileName().toString()));
				}

				// If photo is apercu so save its rank
				if (aPhoto.getOrder().equals(1)) {
					apercu = i;
				}
				i++;
			}
			builder.addPart("apercu", new StringBody(apercu.toString(), ContentType.TEXT_PLAIN));

			if (prp instanceof RealPropertyBuilt) {
				RealPropertyBuilt built = (RealPropertyBuilt) prp;

				ContentBody dpeBody = new StringBody("", ContentType.TEXT_PLAIN);
				Path dpeKwh = built.getDpeFile().get(EnumDPE.KWH_260);
				if (dpeKwh != null) {
					ContentType mime = ContentType.create(Files.probeContentType(dpeKwh));
					String fileName = dpeKwh.getFileName().toString();
					dpeBody = new FileBody(dpeKwh.toFile(), mime, fileName);
				}
				builder.addPart("dpe", dpeBody);

				builder.addPart("nbreGarage", new StringBody(Objects.firstNonNull(built.getNbGarage(), "").toString(),
						ContentType.TEXT_PLAIN));
			}

			httpPost.setEntity(builder.build());
			httpPost.addHeader("Referer", updateReferer.replace("${reference}", prp.getReference()));

			HttpResponse response = client.execute(httpPost);
			LOGGER.info("Update acimflo prp {} : response code = {}", prp.getReference(), response.getStatusLine()
					.getStatusCode());

			// Parse html to get message
			Document doc = Jsoup.parse(response.getEntity().getContent(), "UTF-8", "");
			String msg = doc.select(".msg").html();
			LOGGER.info("Msg = " + msg);
			if (Strings.isNullOrEmpty(msg)) {
				LOGGER.error("Empty return message for updating acimflo :" + doc.toString());
				result = new WSResult("KO", doc.toString());
			} else {
				result = new WSResult("OK", msg);
			}

		} catch (IOException e) {
			LOGGER.error("Error updating property " + prp.getReference() + " on acimflo", e);
			result = new WSResult("KO", e.getMessage());
		}

		return result;
	}
}