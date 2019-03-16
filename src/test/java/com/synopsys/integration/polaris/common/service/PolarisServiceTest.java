package com.synopsys.integration.polaris.common.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.LogLevel;
import com.synopsys.integration.log.PrintStreamIntLogger;
import com.synopsys.integration.polaris.common.api.generated.common.BranchV0;
import com.synopsys.integration.polaris.common.api.generated.common.BranchV0Resources;
import com.synopsys.integration.polaris.common.api.generated.common.ResourcesPagination;
import com.synopsys.integration.polaris.common.request.PolarisRequestFactory;
import com.synopsys.integration.polaris.common.rest.AccessTokenPolarisHttpClient;
import com.synopsys.integration.polaris.common.rest.AccessTokenPolarisHttpClientTestIT;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.rest.request.Request;
import com.synopsys.integration.rest.support.AuthenticationSupport;

public class PolarisServiceTest {
    private PolarisRequestFactory polarisRequestFactory = new PolarisRequestFactory();

    @Test
    public void createDefaultPolarisGetRequestTest() {
        final Request request = polarisRequestFactory.createDefaultPolarisGetRequest("https://google.com");
        assertNotNull(request);
    }

    @Test
    public void executeGetRequestTestIT() throws IntegrationException {
        String baseUrl = System.getenv(AccessTokenPolarisHttpClientTestIT.ENV_POLARIS_URL);
        String accessToken = System.getenv(AccessTokenPolarisHttpClientTestIT.ENV_POLARIS_ACCESS_TOKEN);

        final Gson gson = new Gson();
        final IntLogger logger = new PrintStreamIntLogger(System.out, LogLevel.INFO);
        final AccessTokenPolarisHttpClient httpClient = new AccessTokenPolarisHttpClient(logger, 100, true, ProxyInfo.NO_PROXY_INFO, baseUrl, accessToken, gson, new AuthenticationSupport());

        final PolarisService polarisService = new PolarisService(httpClient, gson);

        final String requestUri = baseUrl + "/api/common/v0/branches";
        final Request request = polarisRequestFactory.createDefaultPolarisGetRequest(requestUri);

        final BranchV0Resources branchV0Resources = polarisService.get(BranchV0Resources.class, request);
        final List<BranchV0> branchV0ResourceList = branchV0Resources.getData();
        assertNotNull(branchV0ResourceList);
        final ResourcesPagination meta = branchV0Resources.getMeta();
        assertNotNull(meta);
    }

}