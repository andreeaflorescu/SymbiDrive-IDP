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
 * on 2015-06-24 at 19:09:13 UTC 
 * Modify at your own risk.
 */

package com.appspot.bustling_bay_88919.symbidrive.model;

/**
 * Model definition for SymbidriveAddFeedbackRequest.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the symbidrive. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SymbidriveAddFeedbackRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> feedback;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String socialID;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getFeedback() {
    return feedback;
  }

  /**
   * @param feedback feedback or {@code null} for none
   */
  public SymbidriveAddFeedbackRequest setFeedback(java.util.List<java.lang.String> feedback) {
    this.feedback = feedback;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSocialID() {
    return socialID;
  }

  /**
   * @param socialID socialID or {@code null} for none
   */
  public SymbidriveAddFeedbackRequest setSocialID(java.lang.String socialID) {
    this.socialID = socialID;
    return this;
  }

  @Override
  public SymbidriveAddFeedbackRequest set(String fieldName, Object value) {
    return (SymbidriveAddFeedbackRequest) super.set(fieldName, value);
  }

  @Override
  public SymbidriveAddFeedbackRequest clone() {
    return (SymbidriveAddFeedbackRequest) super.clone();
  }

}
