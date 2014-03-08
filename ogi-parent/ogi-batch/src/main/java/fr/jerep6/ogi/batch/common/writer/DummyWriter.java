package fr.jerep6.ogi.batch.common.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

public class DummyWriter<T> implements ItemWriter<T>, InitializingBean {

	public void afterPropertiesSet() throws Exception {}

	public void write(List<? extends T> items) throws Exception {
		System.out.println(items);
	}

}
