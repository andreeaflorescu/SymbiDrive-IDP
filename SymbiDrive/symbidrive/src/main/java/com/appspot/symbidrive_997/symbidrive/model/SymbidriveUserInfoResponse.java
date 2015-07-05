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
 * on 2015-07-05 at 12:06:53 UTC 
 * Modify at your own risk.
 */

package com.appspot.symbidrive_997.symbidrive.model;

/**
 * Model definition for SymbidriveUserInfoResponse.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the symbidrive. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SymbidriveUserInfoResponse extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String car;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> feedback;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean isSmoker;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean listenToMusic;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double rating;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String telephone;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String username;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCar() {
    return car;
  }

  /**
   * @param car car or {@code null} for none
   */
  public SymbidriveUserInfoResponse setCar(java.lang.String car) {
    this.car = car;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getFeedback() {
    return feedback;
  }

  /**
   * @param feedback feedback or {@code null} for none
   */
  public SymbidriveUserInfoResponse setFeedback(java.util.List<java.lang.String> feedback) {
    this.feedback = feedback;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getIsSmoker() {
    return isSmoker;
  }

  /**
   * @param isSmoker isSmoker or {@code null} for none
   */
  public SymbidriveUserInfoResponse setIsSmoker(java.lang.Boolean isSmoker) {
    this.isSmoker = isSmoker;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getListenToMusic() {
    return listenToMusic;
  }

  /**
   * @param listenToMusic listenToMusic or {@code null} for none
   */
  public SymbidriveUserInfoResponse setListenToMusic(java.lang.Boolean listenToMusic) {
    this.listenToMusic = listenToMusic;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getRating() {
    return rating;
  }

  /**
   * @param rating rating or {@code null} for none
   */
  public SymbidriveUserInfoResponse setRating(java.lang.Double rating) {
    this.rating = rating;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTelephone() {
    return telephone;
  }

  /**
   * @param telephone telephone or {@code null} for none
   */
  public SymbidriveUserInfoResponse setTelephone(java.lang.String telephone) {
    this.telephone = telephone;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUsername() {
    return username;
  }

  /**
   * @param username username or {@code null} for none
   */
  public SymbidriveUserInfoResponse setUsername(java.lang.String username) {
    this.username = username;
    return this;
  }

  @Override
  public SymbidriveUserInfoResponse set(String fieldName, Object value) {
    return (SymbidriveUserInfoResponse) super.set(fieldName, value);
  }

  @Override
  public SymbidriveUserInfoResponse clone() {
    return (SymbidriveUserInfoResponse) super.clone();
  }

}