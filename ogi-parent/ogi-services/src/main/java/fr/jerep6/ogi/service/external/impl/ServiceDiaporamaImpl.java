package fr.jerep6.ogi.service.external.impl;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorPartner;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorProperty;
import fr.jerep6.ogi.exception.technical.NetworkTechnicalException;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.framework.utils.StringUtils;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.service.external.transfert.AcimfloResultDelete;
import fr.jerep6.ogi.service.external.transfert.AcimfloResultExist;
import fr.jerep6.ogi.transfert.WSResult;
import fr.jerep6.ogi.utils.HttpClientUtils;

@Service("serviceDiaporama")
public class ServiceDiaporamaImpl extends AbstractService implements ServicePartner {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceDiaporamaImpl.class);

	@Value("${partner.diaporama.connect.url}")
	private String			loginUrl;
	@Value("${partner.diaporama.connect.login}")
	private String			login;
	@Value("${partner.diaporama.connect.pwd}")
	private String			pwd;

	@Value("${partner.diaporama.create.url}")
	private String			createUrl;
	@Value("${partner.diaporama.create.referer}")
	private String			createReferer;

	@Value("${partner.diaporama.update.url}")
	private String			updateUrl;
	@Value("${partner.diaporama.update.referer}")
	private String			updateReferer;

	@Value("${partner.diaporama.delete.url}")
	private String			deleteUrl;

	@Value("${partner.diaporama.exist.url}")
	private String			verifReference;

	@Value("${partner.diaporama.apercu.url}")
	private String			imgApercu;

	private WSResult broadcast(HttpClient client, RealProperty prp, String url, String referer) {
		LOGGER.info("Broadcast to Diaporama. url = {} : referer = {}", url, referer);

		WSResult result;
		HttpPost httpPost = new HttpPost(url);
		try {
			if (prp.getSale() == null || prp.getSale().getPriceFinal() == null) {
				throw new BusinessException(EnumBusinessErrorProperty.NO_SALE, prp.getReference());
			}

			// FileBody uploadFilePart = new FileBody(uploadFile);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			builder.addPart("prix", new StringBody(prp.getSale().getPriceFinal().toString(), ContentType.TEXT_PLAIN));
			builder.addPart("reference", new StringBody(prp.getReference(), ContentType.TEXT_PLAIN));
			builder.addPart("referenceOriginale", new StringBody(prp.getReference(), ContentType.TEXT_PLAIN));
			builder.addPart("idType", new StringBody(getType(prp.getCategory()), ContentType.TEXT_PLAIN));

			// ###### Photos ######
			builder.addPart("MAX_FILE_SIZE", new StringBody("5010000", ContentType.TEXT_PLAIN));

			// Il ne faut pas uploader l'image d'apercu lors d'une modification car sinon elle sera présente deux fois.
			// Une fois en Apercu.jpg et une deuxième fois sous son vrai nom
			String urlApercu = imgApercu.replace("$reference", prp.getReference());
			HttpGet getApercu = new HttpGet(urlApercu);
			HttpResponse apercuResponse = client.execute(getApercu);
			LOGGER.info("Response code for {} = {}", urlApercu, apercuResponse.getStatusLine().getStatusCode());
			boolean uploadApercu = apercuResponse.getStatusLine().getStatusCode() != 200;

			Integer apercu = 1;
			Integer i = 1;
			for (fr.jerep6.ogi.persistance.bo.Document aPhoto : prp.getPhotos()) {
				Path p = aPhoto.getAbsolutePath();
				ContentType mime = ContentType.create(Files.probeContentType(p));

				// Upload photo number 1 only if apercu.jpg doesn't exist
				if (uploadApercu && aPhoto.getOrder().equals(1) || !aPhoto.getOrder().equals(1)) {
					builder.addPart("photos[]",
							new FileBody(p.toFile(), mime, StringUtils.stripAccents(p.getFileName().toString())));
				}

				// If photo is order 1 (ie apercu) => save its rank
				if (aPhoto.getOrder().equals(1)) {
					apercu = i;
				}
				i++;
			}
			builder.addPart("apercu", new StringBody(apercu.toString(), ContentType.TEXT_PLAIN));

			httpPost.setEntity(builder.build());
			httpPost.addHeader("Referer", referer);

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
			LOGGER.error("Error broadcast property " + prp.getReference() + " on diaporama", e);
			result = new WSResult(prp.getReference(), "KO", e.getMessage());
		} finally {
			httpPost.releaseConnection();
		}

		return result;
	}

	private void connect(HttpClient client) throws BusinessException {
		LOGGER.info("Connect to Diaporama. url = {}", loginUrl);

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
				LOGGER.error("Login to Diaporama failed : " + doc.toString());
				throw new BusinessException(EnumBusinessErrorPartner.DIAPORAMA_IDENTIFIANTS_KO);
			}
		} catch (IOException e) {
			throw new NetworkTechnicalException(e);
		} finally {
			post.releaseConnection();
		}
	}

	@Override
	public WSResult createOrUpdate(RealProperty prp) {
		CookieHandler.setDefault(new CookieManager());
		HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

		// Connection to acimflo => session id is keeped
		connect(client);

		WSResult result;
		if (prpExist(client, prp.getReference())) {
			// Update property
			String refererer = updateReferer.replace("$reference", prp.getReference());
			result = broadcast(client, prp, updateUrl, refererer);
		} else {
			// Create property
			String refererer = createReferer.replace("$reference", prp.getReference());
			result = broadcast(client, prp, createUrl, refererer);
		}
		return result;
	}

	@Override
	public WSResult delete(RealProperty prp) {
		CookieHandler.setDefault(new CookieManager());
		HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

		// Connection to diaporama => session id is keeped
		connect(client);

		return delete(prp, client);
	}

	private WSResult delete(RealProperty prp, HttpClient client) {
		String reference = prp.getReference();

		// If property doesn't exist => nothing to do
		if (!exist(prp)) {
			return new WSResult(prp.getReference(), "OK", "Le bien n'existe pas sur le diaporama");
		}

		WSResult ws;
		HttpGet httpGet = new HttpGet(deleteUrl.replace("$reference", reference));
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

	@Override
	public Boolean exist(RealProperty prp) {
		Preconditions.checkNotNull(prp);
		CookieHandler.setDefault(new CookieManager());
		HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

		// Connection to diaporama => session id is keeped
		connect(client);

		return prpExist(client, prp.getReference());
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
		LOGGER.info("Test if reference {} exist on Diaporama.", prpReference);
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

	@Override
	public void validate(RealProperty item) throws MultipleBusinessException {}
}