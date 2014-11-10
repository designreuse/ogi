package fr.jerep6.ogi.framework.spring;

import javax.annotation.PostConstruct;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;

public class ElasticsearchNodeFactoryBean implements FactoryBean<Node> {
	private Logger	LOGGER	= LoggerFactory.getLogger(getClass());

	protected Node	node;

	@Value("${elasticsearch.nodeName}")
	private String	nodeName;

	@Value("${elasticsearch.data}")
	private String	dataPath;

	@Value("${elasticsearch.httpEnable}")
	private Boolean	httpEnable;

	@Value("${elasticsearch.clusterName}")
	private String	clusterName;

	@Override
	public Node getObject() throws Exception {
		return node;
	}

	@Override
	public Class<?> getObjectType() {
		return Node.class;
	}

	@PostConstruct
	protected void init() throws Exception {
		ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
		settings.put("node.name", nodeName);
		settings.put("path.data", dataPath);
		settings.put("http.enabled", httpEnable);

		node = NodeBuilder.nodeBuilder()//
				.settings(settings)//
				.clusterName(clusterName)//
				.data(true)//
				.local(true)//
				.node();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}