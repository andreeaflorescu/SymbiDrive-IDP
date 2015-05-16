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
 * (build: 2015-05-05 20:00:12 UTC)
 * on 2015-05-16 at 14:53:55 UTC 
 * Modify at your own risk.
 */

package com.appspot.bustling_bay_88919.symbidrive.model;

/**
 * Model definition for SymbidriveFindPoolRequest.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the symbidrive. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SymbidriveFindPoolRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime date;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime delta;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("end_point_lat")
  private java.lang.Double endPointLat;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("end_point_lon")
  private java.lang.Double endPointLon;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("start_point_lat")
  private java.lang.Double startPointLat;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("start_pointsource_point_lon")
  private java.lang.Double startPointsourcePointLon;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("walking_distance")
  private java.lang.Double walkingDistance;

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getDate() {
    return date;
  }

  /**
   * @param date date or {@code null} for none
   */
  public SymbidriveFindPoolRequest setDate(com.google.api.client.util.DateTime date) {
    this.date = date;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getDelta() {
    return delta;
  }

  /**
   * @param delta delta or {@code null} for none
   */
  public SymbidriveFindPoolRequest setDelta(com.google.api.client.util.DateTime delta) {
    this.delta = delta;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getEndPointLat() {
    return endPointLat;
  }

  /**
   * @param endPointLat endPointLat or {@code null} for none
   */
  public SymbidriveFindPoolRequest setEndPointLat(java.lang.Double endPointLat) {
    this.endPointLat = endPointLat;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getEndPointLon() {
    return endPointLon;
  }

  /**
   * @param endPointLon endPointLon or {@code null} for none
   */
  public SymbidriveFindPoolRequest setEndPointLon(java.lang.Double endPointLon) {
    this.endPointLon = endPointLon;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getStartPointLat() {
    return startPointLat;
  }

  /**
   * @param startPointLat startPointLat or {@code null} for none
   */
  public SymbidriveFindPoolRequest setStartPointLat(java.lang.Double startPointLat) {
    this.startPointLat = startPointLat;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getStartPointsourcePointLon() {
    return startPointsourcePointLon;
  }

  /**
   * @param startPointsourcePointLon startPointsourcePointLon or {@code null} for none
   */
  public SymbidriveFindPoolRequest setStartPointsourcePointLon(java.lang.Double startPointsourcePointLon) {
    this.startPointsourcePointLon = startPointsourcePointLon;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getWalkingDistance() {
    return walkingDistance;
  }

  /**
   * @param walkingDistance walkingDistance or {@code null} for none
   */
  public SymbidriveFindPoolRequest setWalkingDistance(java.lang.Double walkingDistance) {
    this.walkingDistance = walkingDistance;
    return this;
  }

  @Override
  public SymbidriveFindPoolRequest set(String fieldName, Object value) {
    return (SymbidriveFindPoolRequest) super.set(fieldName, value);
  }

  @Override
  public SymbidriveFindPoolRequest clone() {
    return (SymbidriveFindPoolRequest) super.clone();
  }

}