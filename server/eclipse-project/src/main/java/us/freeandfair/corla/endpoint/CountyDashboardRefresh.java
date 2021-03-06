/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Aug 12, 2017
 * @copyright 2017 Colorado Department of State
 * @license SPDX-License-Identifier: AGPL-3.0-or-later
 * @creator Daniel M. Zimmerman <dmz@freeandfair.us>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import javax.persistence.PersistenceException;

import org.apache.log4j.Level;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.json.CountyDashboardRefreshResponse;
import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.CountyDashboard;
import us.freeandfair.corla.persistence.Persistence;

/**
 * The endpoint for refreshing the county dashboard status.
 * 
 * @author Daniel M. Zimmerman <dmz@freeandfair.us>
 * @version 1.0.0
 */
// endpoints don't need constructors
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class CountyDashboardRefresh extends AbstractCountyDashboardEndpoint {
  /**
   * {@inheritDoc}
   */
  @Override
  public EndpointType endpointType() {
    return EndpointType.GET;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointName() {
    return "/county-dashboard";
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Level logLevel() {
    return Level.DEBUG;
  }
  
  /**
   * Provides information about the County and Audit Board dashboards.
   * 
   * @param the_request The request.
   * @param the_response The response.
   */
  @Override
  public String endpointBody(final Request the_request, final Response the_response) {
    try {
      final County county = Main.authentication().authenticatedCounty(the_request);

      okJSON(the_response, 
             Main.GSON.toJson(CountyDashboardRefreshResponse.createResponse
                              (Persistence.getByID(county.id(), CountyDashboard.class))));
    } catch (final PersistenceException e) {
      serverError(the_response, "could not obtain dashboard state");
    }
    return my_endpoint_result.get();
  }

  /**
   * This endpoint requires COUNTY authorization.
   */
  @Override
  public AuthorizationType requiredAuthorization() {
    return AuthorizationType.COUNTY;
  }
}
