/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.MongoClientURI;

import nl.kpmg.af.service.data.core.Measurement;
import nl.kpmg.af.service.data.core.repository.MeasurementRepository;
import nl.kpmg.af.service.exception.ApplicationDatabaseConnectionException;

/**
 *
 * @author mhoekstra
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@DirtiesContext
public class MongoDBUtilTest {

	private static DatabaseInitialiser databaseInitialiser;

	@Autowired
	private MongoDBUtil mongoDBUtil;

	@BeforeClass
	public static void setUpClass() throws Exception {
		databaseInitialiser = new DatabaseInitialiser();
		databaseInitialiser.start();
	}

	@AfterClass
	public static void tearDownClass() {
		databaseInitialiser.stop();
	}

	@Test
	public void testGetRepositoryCreatesDynamicRepository() throws ApplicationDatabaseConnectionException {
		MeasurementRepository repository = mongoDBUtil.getRepository("test", "visitLayer");

		assertNotNull(repository);

		List<Measurement> all = repository.findAll();

		assertTrue(all.size() == 3);

		assertNotNull(all.get(0).getVersion());
		assertNotNull(all.get(0).getMeasurementTimestamp());
		assertTrue(all.get(0).containsKey("processingTimestamp"));
		assertTrue(all.get(0).containsKey("value"));
		assertTrue(all.get(0).containsKey("history"));

		assertTrue(all.get(0).getVersion() == 2);
	}

	@Test
	public void testGetRepositoryCreatesDynamicRepositoryForMultipleCollections()
			throws ApplicationDatabaseConnectionException {
		MeasurementRepository repositoryA = mongoDBUtil.getRepository("test", "testA");
		MeasurementRepository repositoryB = mongoDBUtil.getRepository("test", "testB");

		assertNotNull(repositoryA);
		assertNotNull(repositoryB);

		List<Measurement> measurementA = repositoryA.findAll();
		List<Measurement> measurementB = repositoryB.findAll();

		assertNotNull(measurementA);
		assertNotNull(measurementB);

		assertTrue(measurementA.get(0).getValue().get("name").equals("testA"));
		assertTrue(measurementB.get(0).getValue().get("name").equals("testB"));
	}

	@Test
	public void geneateMongoUriTest() throws Exception {
		MongoClientURI uri1, uri2, uri3;
		String hostString;
		List<String> hosts = new ArrayList<String>();

		String testString = "mongodb://db1.example.net,db2.example.net:2500/?replicaSet=test";
		hosts.add("db1.example.net");
		hosts.add("db2.example.net:2500");
		hostString = "db1.example.net,db2.example.net:2500";
		uri1 = new MongoClientURI(testString);
		uri2 = MongoDBUtil.generateMongoUri(null, null, hosts, null, "replicaSet=test");
		uri3 = MongoDBUtil.generateMongoUri(null, null, hostString, null, "replicaSet=test");
		assertEquals(uri1, uri2);
		assertEquals(uri2, uri3);

		String testStringUser = "mongodb://user:pass@db2.example.net:2500/db?replicaSet=test";
		hosts.clear();
		hosts.add("db2.example.net:2500");

		uri1 = new MongoClientURI(testStringUser);
		uri2 = MongoDBUtil.generateMongoUri("user", "pass", hosts, "db", "replicaSet=test");
		uri3 = MongoDBUtil.generateMongoUri("user", "pass", "db2.example.net:2500", "db", "replicaSet=test");
		assertEquals(uri1, uri2);
		assertEquals(uri2, uri3);
	}

}
