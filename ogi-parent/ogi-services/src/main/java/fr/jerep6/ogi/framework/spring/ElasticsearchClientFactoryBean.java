package fr.jerep6.ogi.framework.spring;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ElasticsearchClientFactoryBean implements FactoryBean<Client> {
	private Logger		LOGGER	= LoggerFactory.getLogger(getClass());

	@Autowired
	private Node		node;

	protected Client	client;

	@PostConstruct
	protected void buildClient() throws Exception {
		if (node == null) {
			throw new Exception("You must define an ElasticSearch Node as a Spring Bean.");
		}
		client = node.client();
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