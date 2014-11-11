package fr.jerep6.ogi.framework.spring;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * Use TransportClient because batchs need to call Elasticsearch
 *
 * @author jerep6
 */
public class ElasticsearchClientFactoryBean implements FactoryBean<Client> {
	private Logger		LOGGER	= LoggerFactory.getLogger(getClass());

	@Value("${elasticsearch.clusterName}")
	private String		clusterName;

	protected Client	client;

	@PostConstruct
	protected void buildClient() throws Exception {
		Settings settings = ImmutableSettings.settingsBuilder()//
				.put("cluster.name", clusterName)//
				.put("client.transport.sniff", true)//
				.build();
		client = new TransportClient(settings)//
				.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
	}

	@PreDestroy
	protected void destroy() {
		client.close();
	}

	@Override
	public Client getObject() throws Exception {
		return client;
	}

	@Override
	public Class<?> getObjectType() {
		return Client.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}