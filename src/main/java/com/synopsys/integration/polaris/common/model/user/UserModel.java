/**
 * polaris-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.polaris.common.model.user;

import com.google.gson.annotations.SerializedName;
import com.synopsys.integration.polaris.common.api.PolarisComponent;
import com.synopsys.integration.polaris.common.api.generated.auth.UserRelationships;

public class UserModel extends PolarisComponent {
    @SerializedName("attributes")
    private UserAttributesModel attributes = null;
    @SerializedName("id")
    private String id;
    @SerializedName("relationships")
    private UserRelationships relationships = null;
    @SerializedName("type")
    private String type;

    /**
     * Get attributes
     * @return attributes
     */
    public UserAttributesModel getAttributes() {
        return attributes;
    }

    public void setAttributes(final UserAttributesModel attributes) {
        this.attributes = attributes;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get relationships
     * @return relationships
     */
    public UserRelationships getRelationships() {
        return relationships;
    }

    public void setRelationships(final UserRelationships relationships) {
        this.relationships = relationships;
    }

    /**
     * Get type
     * @return type
     */
    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

}