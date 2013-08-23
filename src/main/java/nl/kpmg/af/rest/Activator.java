package nl.kpmg.af.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author janos4276
 *
 */

/**
 * This class represents the "main" application entry point.
 * It is required to activate/register the rest service(s).
 * This is auto-magically taken care of by JBoss AS as of
 * version 7.1.1 FINAL and there is no need to configure the
 * service in WEB-INF.xml. So yeah it's empty for a reason.
 * Not sure if this will work in other AS such as Apache Tomcat
 * or JBoss AS < 7.1.1.
 *
 */
@ApplicationPath("/rest")
public class Activator extends Application { }
