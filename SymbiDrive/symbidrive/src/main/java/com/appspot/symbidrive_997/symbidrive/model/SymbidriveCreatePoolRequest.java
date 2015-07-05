/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-03-26 20:30:19 UTC)
 * on 2015-07-05 at 15:10:59 UTC 
 * Modify at your own risk.
 */

package com.appspot.symbidrive_997.symbidrive.model;

/**
 * Model definition for SymbidriveCreatePoolRequest.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the symbidrive. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SymbidriveCreatePoolRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime date;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("destination_point_lat")
  private java.lang.Double destinationPointLat;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("destination_point_lon")
  private java.lang.Double destinationPointLon;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("driver_id")
  private java.lang.String driverId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("is_weekly")
  private java.lang.Boolean isWeekly;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("route_id") @com.google.api.client.json.JsonString
  private java.lang.Long routeId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long seats;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("source_point_lat")
  private java.lang.Double sourcePointLat;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("source_point_lon")
  private java.lang.Double sourcePointLon;

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getDate() {
    return date;
  }

  /**
   * @param date date or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setDate(com.google.api.client.util.DateTime date) {
    this.date = date;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getDestinationPointLat() {
    return destinationPointLat;
  }

  /**
   * @param destinationPointLat destinationPointLat or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setDestinationPointLat(java.lang.Double destinationPointLat) {
    this.destinationPointLat = destinationPointLat;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getDestinationPointLon() {
    return destinationPointLon;
  }

  /**
   * @param destinationPointLon destinationPointLon or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setDestinationPointLon(java.lang.Double destinationPointLon) {
    this.destinationPointLon = destinationPointLon;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDriverId() {
    return driverId;
  }

  /**
   * @param driverId driverId or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setDriverId(java.lang.String driverId) {
    this.driverId = driverId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getIsWeekly() {
    return isWeekly;
  }

  /**
   * @param isWeekly isWeekly or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setIsWeekly(java.lang.Boolean isWeekly) {
    this.isWeekly = isWeekly;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getRouteId() {
    return routeId;
  }

  /**
   * @param routeId routeId or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setRouteId(java.lang.Long routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getSeats() {
    return seats;
  }

  /**
   * @param seats seats or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setSeats(java.lang.Long seats) {
    this.seats = seats;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getSourcePointLat() {
    return sourcePointLat;
  }

  /**
   * @param sourcePointLat sourcePointLat or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setSourcePointLat(java.lang.Double sourcePointLat) {
    this.sourcePointLat = sourcePointLat;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getSourcePointLon() {
    return sourcePointLon;
  }

  /**
   * @param sourcePointLon sourcePointLon or {@code null} for none
   */
  public SymbidriveCreatePoolRequest setSourcePointLon(java.lang.Double sourcePointLon) {
    this.sourcePointLon = sourcePointLon;
    return this;
  }

  @Override
  public SymbidriveCreatePoolRequest set(String fieldName, Object value) {
    return (SymbidriveCreatePoolRequest) super.set(fieldName, value);
  }

  @Override
  public SymbidriveCreatePoolRequest clone() {
    return (SymbidriveCreatePoolRequest) super.clone();
  }

}
