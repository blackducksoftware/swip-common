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
package com.synopsys.integration.polaris.common.request;

import com.synopsys.integration.rest.HttpMethod;
import com.synopsys.integration.rest.request.Request;

public class PolarisRequestFactory {
    public static String DEFAULT_MIME_TYPE = "application/vnd.api+json";

    public static final String LIMIT_PARAMETER = "page[limit]";
    public static final String OFFSET_PARAMETER = "page[offset]";

    public static final int DEFAULT_LIMIT = 10;
    public static final int DEFAULT_OFFSET = 0;

    public static Request createDefaultPolarisGetRequest(final String requestUri) {
        return createCommonPolarisGetRequest(requestUri, DEFAULT_LIMIT);
    }

    public static Request createCommonPolarisGetRequest(final String requestUri, final int limit) {
        return createCommonPolarisGetRequest(requestUri, limit, DEFAULT_OFFSET);
    }

    public static Request createCommonPolarisGetRequest(final String requestUri, final int limit, final int offset) {
        return createDefaultPagedRequestBuilder(limit, offset)
                       .uri(requestUri)
                       .build();
    }

    public static Request.Builder createDefaultRequestBuilder() {
        return populatePagedRequestBuilder(createDefaultBuilder(), DEFAULT_LIMIT, DEFAULT_OFFSET);
    }

    public static Request.Builder createDefaultPagedRequestBuilder(int limit, int offset) {
        return populatePagedRequestBuilder(createDefaultBuilder(), limit, offset);
    }

    public static Request.Builder populatePagedRequestBuilder(Request.Builder requestBuilder, int limit, int offset) {
        return requestBuilder
                       .addQueryParameter(LIMIT_PARAMETER, Integer.toString(limit))
                       .addQueryParameter(OFFSET_PARAMETER, Integer.toString(offset));
    }

    public static Request.Builder createDefaultBuilder() {
        return new Request.Builder()
                       .mimeType(DEFAULT_MIME_TYPE)
                       .method(HttpMethod.GET);
    }

}
