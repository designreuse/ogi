package fr.jerep6.ogi.service.external.impl;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

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
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDPE;
import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorPartner;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorProperty;
import fr.jerep6.ogi.exception.technical.NetworkTechnicalException;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.framework.utils.ObjectUtils;
import fr.jerep6.ogi.framework.utils.StringUtils;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyBuilt;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.service.external.transfert.AcimfloResultDelete;
import fr.jerep6.ogi.service.external.transfert.AcimfloResultExist;
import fr.jerep6.ogi.transfert.WSResult;
import fr.jerep6.ogi.utils.Functions;
import fr.jerep6.ogi.utils.HttpClientUtils;

@Service("serviceAcimflo")
public class ServiceAcimfloImpl extends AbstractService implements ServicePartner {
	private final Logger						LOGGER		= LoggerFactory.getLogger(ServiceAcimfloImpl.class);
	public static final String					MODE_RENT	= "RENT";
	public static final String					MODE_SALE	= "SALE";

	@Value("${partner.acimflo.connect.url}")
	private String								loginUrl;
	@Value("${partner.acimflo.connect.login}")
	private String								login;
	@Value("${partner.acimflo.connect.pwd}")
	private String								pwd;

	@Value("${partner.acimflo.delete.url}")
	private String								deleteUrl;

	@Value("${partner.acimflo.exist.url}")
	private String								verifReference;

	@Value("${partner.reference.sale.suffix}")
	private String								suffixSale;

	@Value("${partner.reference.rent.suffix}")
	private String								suffixRent;

	@Resource(name = "mapAcimfloUrl")
	private Map<String, Map<String, String>>	config;

	private WSResult broadcast(HttpClient client, RealProperty prp, String reference, String mode, String prefixeMap) {
		String referer = config.get(mode).get(prefixeMap + ".referer").replace("$reference", reference);
		String url = config.get(mode).get(prefixeMap + ".url").replace("$reference", reference);
		String imgApercu = config.get(mode).get("apercu.url").replace("$reference", reference);

		LOGGER.info("Broadcast to Acimflo. url = {} : referer = {}", url, referer);

		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		// builder.addTextBody("json_text",json_text,ContentType.APPLICATION_JSON);

		// Construct form data
		buildCommon(client, prp, reference, builder, imgApercu);
		switch (mode) {
			case MODE_SALE:
				buildSale(client, prp, builder);
				break;
			case MODE_RENT:
				buildRent(client, prp, builder);
				break;
		}

		// Send request
		WSResult result;
		try {
			httpPost.setEntity(builder.build());
			httpPost.addHeader("Referer", referer);

			// Delete all documents before update/create property on partner. This is for update.
			deleteDocuments(client, mode, reference);

			HttpResponse response = client.execute(httpPost);
			LOGGER.info("Response code for create/update = {}", response.getStatusLine().getStatusCode());

			// Parse html to get message
			Document doc = Jsoup.parse(response.getEntity().getContent(), "UTF-8", "");
			String msg = doc.select(".msg").html();
			LOGGER.info("Result msg = " + msg);
			if (Strings.isNullOrEmpty(msg) || msg.toLowerCase().contains("erreur")) {
				LOGGER.error("Empty result msg :" + doc.toString());
				result = new WSResult(prp.getReference(), "KO", msg);
			} else {
				result = new WSResult(prp.getReference(), "OK", msg);
			}
		} catch (IOException e) {
			LOGGER.error("Error broadcast property " + prp.getReference() + " on acimflo", e);
			result = new WSResult(prp.getReference(), "KO", e.getMessage());
		} finally {
			httpPost.releaseConnection();
		}
		return result;

	}

	private void buildCommon(HttpClient client, RealProperty prp, String reference, MultipartEntityBuilder builder,
			String imgApercu) {
		try {

			if (prp instanceof RealPropertyLivable) {
				RealPropertyLivable liv = (RealPropertyLivable) prp;
				builder.addPart("surfaceHabitable", new StringBody(Objects.firstNonNull(liv.getArea(), "").toString(),
						HttpClientUtils.TEXT_PLAIN_UTF8));
				builder.addPart("nbreChambre", new StringBody(Objects.firstNonNull(liv.getNbBedRoom(), "").toString(),
						HttpClientUtils.TEXT_PLAIN_UTF8));
				// Acimflo handle only bathroom. Sum of bathroom and showerroom
				Integer showerRoom = liv.getNbShowerRoom() == null ? 0 : liv.getNbShowerRoom();
				Integer bathRoom = liv.getNbBathRoom() == null ? 0 : liv.getNbBathRoom();
				builder.addPart("nbreSDB", new StringBody(Integer.valueOf(bathRoom + showerRoom).toString(),
						HttpClientUtils.TEXT_PLAIN_UTF8));

				builder.addPart("nbreWC", new StringBody(Objects.firstNonNull(liv.getNbWC(), "").toString(),
						HttpClientUtils.TEXT_PLAIN_UTF8));
			}

			builder.addPart("surfaceTerrain", new StringBody(Objects.firstNonNull(prp.getLandArea(), "").toString(),
					HttpClientUtils.TEXT_PLAIN_UTF8));

			builder.addPart("reference", new StringBody(reference, HttpClientUtils.TEXT_PLAIN_UTF8));
			builder.addPart("referenceOriginale", new StringBody(reference, HttpClientUtils.TEXT_PLAIN_UTF8));
			builder.addPart("idType", new StringBody(getType(prp.getCategory()), HttpClientUtils.TEXT_PLAIN_UTF8));
			builder.addPart("surfaceDependance", new StringBody(Objects.firstNonNull(prp.getDependencyArea(), "")
					.toString(), ContentType.TEXT_PLAIN));

			String addr = "";
			if (prp.getAddress() != null) {
				addr = Objects.firstNonNull(prp.getAddress().getCity(), "");
			}
			builder.addPart("nomVille", new StringBody(addr, HttpClientUtils.TEXT_PLAIN_UTF8));

			String style = "";
			if (prp.getType() != null) {
				style = Objects.firstNonNull(prp.getType().getLabel(), "");
			}
			builder.addPart("nomStyle", new StringBody(style, HttpClientUtils.TEXT_PLAIN_UTF8));

			Description description = prp.getDescription(EnumDescriptionType.WEBSITE_OWN);
			String desc = "";
			if (description != null) {
				desc = Objects.firstNonNull(description.getLabel(), "");
			}
			builder.addPart("commentaire", new StringBody(desc, HttpClientUtils.TEXT_PLAIN_UTF8));

			// ###### Photos ######
			builder.addPart("MAX_FILE_SIZE", new StringBody("5010000", HttpClientUtils.TEXT_PLAIN_UTF8));

			Integer apercu = 1;
			Integer i = 1;
			for (fr.jerep6.ogi.persistance.bo.Document aPhoto : prp.getPhotos()) {
				Path p = aPhoto.getAbsolutePath();
				ContentType mime = ContentType.create(Files.probeContentType(p));

				builder.addPart("photos[]",
						new FileBody(p.toFile(), mime, StringUtils.stripAccents(p.getFileName().toString())));

				// If photo is order 1 (ie apercu) => save its rank
				if (aPhoto.getOrder().equals(1)) {
					apercu = i;
				}
				i++;
			}
			builder.addPart("apercu", new StringBody(apercu.toString(), HttpClientUtils.TEXT_PLAIN_UTF8));

			if (prp instanceof RealPropertyBuilt) {
				RealPropertyBuilt built = (RealPropertyBuilt) prp;

				ContentBody dpeBody = new StringBody("", HttpClientUtils.TEXT_PLAIN_UTF8);
				Path dpeKwh = built.getDpeFile().get(EnumDPE.KWH_260);
				if (dpeKwh != null) {
					ContentType mime = ContentType.create(Files.probeContentType(dpeKwh));
					String fileName = dpeKwh.getFileName().toString();
					dpeBody = new FileBody(dpeKwh.toFile(), mime, fileName);
				}
				builder.addPart("dpe", dpeBody);

				builder.addPart("nbreGarage", new StringBody(Objects.firstNonNull(built.getNbGarage(), "").toString(),
						HttpClientUtils.TEXT_PLAIN_UTF8));
			}
		} catch (IOException e) {
			LOGGER.error("", e);
		}
	}

	private void buildRent(HttpClient client, RealProperty prp, MultipartEntityBuilder builder) {
		Preconditions.checkNotNull(prp.getRent());

		Rent rent = prp.getRent();
		builder.addPart("prix", new StringBody(ObjectUtils.toString(rent.getPrice()), HttpClientUtils.TEXT_PLAIN_UTF8));
		builder.addPart("disponible", new StringBody(toBoolean(rent.getFreeDate() != null),
				HttpClientUtils.TEXT_PLAIN_UTF8));
		builder.addPart("prix", new StringBody(ObjectUtils.toString(rent.getPrice()), HttpClientUtils.TEXT_PLAIN_UTF8));
		builder.addPart("honoraires", new StringBody(ObjectUtils.toString(rent.getCommission()),
				HttpClientUtils.TEXT_PLAIN_UTF8));
		builder.addPart("charges", new StringBody(ObjectUtils.toString(rent.getServiceCharge()),
				HttpClientUtils.TEXT_PLAIN_UTF8));

		if (rent.getFreeDate() != null) {
			SimpleDateFormat spFormater = new SimpleDateFormat("dd/MM/yyyy");
			builder.addPart("libre_le", new StringBody(spFormater.format(rent.getFreeDate().getTime()),
					HttpClientUtils.TEXT_PLAIN_UTF8));
		}
	}

	private void buildSale(HttpClient client, RealProperty prp, MultipartEntityBuilder builder) {
		Preconditions.checkNotNull(prp.getSale());
		Sale s = prp.getSale();
		builder.addPart("prix", new StringBody(s.getPriceFinal().toString(), HttpClientUtils.TEXT_PLAIN_UTF8));

		builder.addPart("vendu", new StringBody(toBoolean(s.getSold()), HttpClientUtils.TEXT_PLAIN_UTF8));
	}

	private void connect(HttpClient client) throws BusinessException {
		LOGGER.info("Connect to Acimflo. url = {}", loginUrl);

		HttpPost post = new HttpPost(loginUrl);
		try {
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("login", login));
			urlParameters.add(new BasicNameValuePair("mdp", pwd));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			// Execute request
			HttpResponse response = client.execute(post);
			LOGGER.info("Connection response code = " + response.getStatusLine().getStatusCode());

			// Parse html to determine if connection is successful or not
			Document doc = Jsoup.parse(response.getEntity().getContent(), "UTF-8", "");
			Boolean logged = !doc.select("#menu").isEmpty();
			LOGGER.info("Login successful = " + logged);

			if (!logged) {
				LOGGER.error("Login to acimflo failed : " + doc.toString());
				throw new BusinessException(EnumBusinessErrorPartner.ACIMFLO_IDENTIFIANTS_KO);
			}
		} catch (IOException e) {
			throw new NetworkTechnicalException(e);
		} finally {
			post.releaseConnection();
		}
	}

	@Override
	public WSResult createOrUpdate(RealProperty prp) {
		validate(prp);

		CookieHandler.setDefault(new CookieManager());

		HttpClient client = HttpClientUtils.buildClient();

		// Connection to acimflo => session id is keeped
		connect(client);

		WSResult result = new WSResult(prp.getReference(), WSResult.RESULT_OK, "");

		// Treat sale
		if (prp.getSale() != null) {
			String reference = Functions.computeSaleReference(prp.getReference());
			if (prpExist(client, reference)) {
				// Update property
				result = result.combine(broadcast(client, prp, reference, MODE_SALE, "update"));
			} else {
				// Create property
				result = result.combine(broadcast(client, prp, reference, MODE_SALE, "create"));
			}
		}

		// Treat rent
		if (prp.getRent() != null) {
			String reference = Functions.computeRentReference(prp.getReference());

			if (prpExist(client, reference)) {
				// Update property
				result = result.combine(broadcast(client, prp, reference, MODE_RENT, "update"));
			} else {
				// Create property
				result = result.combine(broadcast(client, prp, reference, MODE_RENT, "create"));
			}
		}

		return result;
	}

	@Override
	public WSResult delete(RealProperty prp) {
		CookieHandler.setDefault(new CookieManager());
		HttpClient client = HttpClientUtils.buildClient();

		// Connection to acimflo => session id is keeped
		connect(client);

		WSResult r = new WSResult(prp.getReference(), "OK", "");
		// Delete sale
		if (prp.getSale() != null) {
			r = r.combine(delete(prp, Functions::computeSaleReference, client));
		}
		if (prp.getRent() != null) {
			r = r.combine(delete(prp, Functions::computeRentReference, client));
		}

		return r;
	}

	private WSResult delete(RealProperty prp, Function<String, String> computeReference, HttpClient client) {
		String reference = computeReference.apply(prp.getReference());

		// If property doesn't exist => nothing to do
		if (!prpExist(client, reference)) {
			return new WSResult(prp.getReference(), "OK", "Le bien n'existe pas sur le site acimflo");
		}

		HttpGet httpGet = new HttpGet(deleteUrl.replace("$reference", reference));
		WSResult ws;
		try {
			HttpResponse response = client.execute(httpGet);

			AcimfloResultDelete result = HttpClientUtils.convertToJson(response, AcimfloResultDelete.class);
			LOGGER.info("Delete of reference {} : {}. Msg = {}", new Object[] { prp.getReference(),
					result.getSuccess(), result.getPhrase() });

			if (result.getSuccess()) {
				ws = new WSResult(prp.getReference(), "OK", result.getPhrase());
			} else {
				ws = new WSResult(prp.getReference(), "KO", result.getPhrase());
			}

		} catch (IOException e) {
			throw new NetworkTechnicalException(e);
		} finally {
			httpGet.releaseConnection();
		}

		return ws;
	}

	private void deleteDocuments(HttpClient client, String mode, String reference) throws IOException {
		String url = config.get(mode).get("document.delete.url").replace("$reference", reference);

		HttpGet httpGet = new HttpGet(url);
		client.execute(httpGet);

		httpGet.releaseConnection();
	}

	@Override
	public Boolean exist(RealProperty prp) {
		CookieHandler.setDefault(new CookieManager());
		HttpClient client = HttpClientUtils.buildClient();

		// Connection to acimflo => session id is keeped
		connect(client);

		Boolean existSale = false;
		if (prp.getSale() != null) {
			existSale = prpExist(client, Functions.computeSaleReference(prp.getReference()));
		}

		Boolean existRent = false;
		if (prp.getRent() != null) {
			existRent = prpExist(client, Functions.computeRentReference(prp.getReference()));
		}

		return existSale || existRent;
	}

	/**
	 * Convert OGI type into acimflo type.
	 * 
	 * @param category
	 * @return
	 */
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

	/**
	 * Determine if property exist on Acimflo.
	 * 
	 * @param client
	 *            http client
	 * @param prpReference
	 *            reference of property
	 * @return
	 */
	private boolean prpExist(HttpClient client, String prpReference) {
		LOGGER.info("Test if reference {} exist on Acimflo.", prpReference);
		boolean exist = false;

		try {
			HttpGet httpget = new HttpGet(verifReference.replace("$reference", prpReference));
			HttpResponse response = client.execute(httpget);

			AcimfloResultExist reponse = HttpClientUtils.convertToJson(response, AcimfloResultExist.class);
			LOGGER.info("Existance of reference {} : {}", prpReference, reponse.isReponse());

			exist = reponse.isReponse();
		} catch (IOException e) {
			throw new NetworkTechnicalException(e);
		}
		return exist;
	}

	private String toBoolean(Boolean b) {
		return b == null ? "" : b ? "oui" : "non";
	}

	@Override
	public void validate(RealProperty prp) throws MultipleBusinessException {
		MultipleBusinessException mbe = new MultipleBusinessException();

		Description description = prp.getDescription(EnumDescriptionType.WEBSITE_OWN);
		if ((prp.getSale() == null || prp.getSale().getPriceFinal() == null) && prp.getRent() == null) {
			mbe.add(EnumBusinessErrorProperty.NO_SALE, prp.getReference());
		}
		if (description == null || description.getLabel() == null) {
			mbe.add(EnumBusinessErrorProperty.NO_DESCRIPTION_WEBSITE_OWN, prp.getReference());
		}

		if (prp.getRent() != null) {
			if (prp.getRent().getPrice() == null || prp.getRent().getPrice() <= 0F) {
				mbe.add(EnumBusinessErrorProperty.NO_RENT_PRICE, prp.getReference());
			}

			if (prp.getRent().getCommission() == null || prp.getRent().getCommission() < 0F) {
				mbe.add(EnumBusinessErrorProperty.NO_RENT_COMMISSION, prp.getReference());
			}
		}

		if (prp.getSale() != null) {
			if (prp.getSale().getPrice() == null || prp.getSale().getPrice() <= 0F) {
				mbe.add(EnumBusinessErrorProperty.NO_SALE_PRICE, prp.getReference());
			}
		}

		mbe.checkErrors();
	}
}