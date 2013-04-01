package fr.jerep6.ogi.framework.test;

import java.io.File;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * By default, the framework will create and roll back a transaction for each test. Ce comportemen peut être modifié via
 * les annotations suivantes :
 * 
 * @Rollback : Sur chaque méthode. Indicates whether the transaction for the annotated test method should be rolled back
 *           after the test method has completed. If true, the transaction is rolled back; otherwise, the transaction is
 *           committed. Use @Rollback to override the default rollback flag configured at the class level.
 * @Rollback(false)
 * @TransactionConfiguration : Defines class-level metadata for configuring transactional tests. Specifically, the bean
 *                           name of the PlatformTransactionManager that is to be used to drive transactions can be
 *                           explicitly configured if the bean name of the desired PlatformTransactionManager is not
 *                           "transactionManager". In addition, you can change the defaultRollback flag to false.
 *                           Typically, @TransactionConfiguration is used in conjunction with @ContextConfiguration.
 * @TransactionConfiguration(transactionManager="txMgr", defaultRollback=false)
 * 
 * @author jerep6
 * 
 */
public abstract class AbstractTransactionalTest extends AbstractTransactionalJUnit4SpringContextTests {
	protected Logger						LOGGER	= LoggerFactory.getLogger(AbstractTransactionalTest.class);

	@Autowired
	private DataSource						dataSource;

	private DatabaseDataSourceConnection	dataSourceConnection;

	/** DataSet contenant les enregistrements du test : permet de compter le nbre d'enregistrements, les valeurs ... */
	protected IDataSet						dataSet;

	/** @return le chemin du fichier contenant les données dbunit */
	protected abstract String[] getDatasetFileName();

	@BeforeTransaction
	public void setUp() {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		try {
			IDataSet[] d = new IDataSet[getDatasetFileName().length];

			for (int i = 0; i < getDatasetFileName().length; i++) {
				d[i] = builder.build(new File(getDatasetFileName()[i]));
			}

			// Le dernier paramètre indique que lorsque des tables portant le même nom se trouve dans plusieurs fichiers
			// xml les données sont fusionnées pour ne former qu'une seule table finale
			dataSet = new CompositeDataSet(d, true);

			// Création d'une connexion tel que l'attend dbunit à partir de la datasource
			dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
			// Utilisation de Mysql
			dataSourceConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new MySqlDataTypeFactory());
			DatabaseOperation.CLEAN_INSERT.execute(dataSourceConnection, dataSet);
		} catch (Exception e) {
			LOGGER.error("Erreur setUp", e);
		}
	}

	/**
	 * Utilisation de @AfterTransaction plutôt que de @After car avec @After exception suivante lorsque le test ajoute
	 * une ligne en BD : java.sql.SQLException: Lock wait timeout exceeded; try restarting transaction
	 * 
	 * Hypothèse : @Before, la méthode de test et @After s'exécutent dans une transaction à part. Sauf que la méthode @After
	 * se lance avant que la transaction de test ne soit finie. De ce fait, elle tente de supprimer des lignes alors que
	 * la transaction de test n'est pas terminé => blocage
	 */
	@AfterTransaction
	public void tearDown() {
		try {
			DatabaseOperation.DELETE_ALL.execute(dataSourceConnection, dataSet);
		} catch (Exception e) {
			LOGGER.error("Erreur tearDown", e);
		}
	}
}