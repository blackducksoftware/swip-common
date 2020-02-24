package com.synopsys.integration.polaris.common.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.util.Collections;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import com.google.gson.Gson;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.executable.ExecutableOutput;
import com.synopsys.integration.executable.ExecutableRunnerException;
import com.synopsys.integration.executable.ProcessBuilderRunner;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.LogLevel;
import com.synopsys.integration.log.PrintStreamIntLogger;
import com.synopsys.integration.polaris.common.cli.PolarisCliExecutable;
import com.synopsys.integration.polaris.common.cli.PolarisCliResponseUtility;
import com.synopsys.integration.polaris.common.cli.PolarisDownloadUtility;
import com.synopsys.integration.polaris.common.cli.model.CoverityToolInfo;
import com.synopsys.integration.polaris.common.cli.model.PolarisCliResponseModel;
import com.synopsys.integration.polaris.common.configuration.PolarisServerConfig;
import com.synopsys.integration.polaris.common.configuration.PolarisServerConfigBuilder;
import com.synopsys.integration.polaris.common.exception.PolarisIntegrationException;
import com.synopsys.integration.polaris.common.rest.AccessTokenPolarisHttpClient;

@Ignore
public class JobServiceTestIT {
    private PolarisCliResponseModel polarisCliResponseModel;
    private JobService jobService;

    @BeforeEach
    public void createJobAndJobService() throws ExecutableRunnerException, PolarisIntegrationException {
        final PolarisServerConfigBuilder polarisServerConfigBuilder = PolarisServerConfig.newBuilder();
        polarisServerConfigBuilder.setUrl(System.getenv("POLARIS_URL"));
        polarisServerConfigBuilder.setAccessToken(System.getenv("POLARIS_ACCESS_TOKEN"));
        polarisServerConfigBuilder.setGson(new Gson());

        assumeTrue(StringUtils.isNotBlank(polarisServerConfigBuilder.getUrl()));
        assumeTrue(StringUtils.isNotBlank(polarisServerConfigBuilder.getAccessToken()));

        final PolarisServerConfig polarisServerConfig = polarisServerConfigBuilder.build();
        final IntLogger logger = new PrintStreamIntLogger(System.out, LogLevel.INFO);

        final AccessTokenPolarisHttpClient accessTokenPolarisHttpClient = polarisServerConfig.createPolarisHttpClient(logger);
        final PolarisDownloadUtility polarisDownloadUtility = PolarisDownloadUtility.fromPolaris(logger, polarisServerConfig.createPolarisHttpClient(logger), null);
        final Optional<String> potentialPolarisCLiExecutablePath = polarisDownloadUtility.getOrDownloadPolarisCliExecutable();

        assumeTrue(potentialPolarisCLiExecutablePath.isPresent(), "The Polaris CLI could not be downloaded");

        final String polarisCliExecutablePath = potentialPolarisCLiExecutablePath.get();

        final ProcessBuilderRunner processBuilderRunner = new ProcessBuilderRunner();
        final File emptyProjectDirectory = new File("test_directory");
        assumeTrue(emptyProjectDirectory.mkdirs(), "Test directory could not be created");

        final PolarisCliExecutable polarisCliExecutable = new PolarisCliExecutable(new File(polarisCliExecutablePath), emptyProjectDirectory, System.getenv(), Collections.singletonList("analyze"));

        final ExecutableOutput executableOutput = processBuilderRunner.execute(polarisCliExecutable);
        assumeTrue(executableOutput.getReturnCode() == 0, "'polaris analyze' returned a nonzero exit code");

        final PolarisCliResponseUtility polarisCliResponseUtility = PolarisCliResponseUtility.defaultUtility(logger);
        polarisCliResponseModel = polarisCliResponseUtility.getPolarisCliResponseModelFromDefaultLocation(emptyProjectDirectory.getAbsolutePath());

        final PolarisServicesFactory polarisServicesFactory = polarisServerConfig.createPolarisServicesFactory(logger);
        jobService = polarisServicesFactory.createJobService();
    }

    @Test
    public void testWaitForJobToCompleteByUrl() throws IntegrationException, InterruptedException {
        final Optional<String> potentialJobStatusUrl = Optional.ofNullable(polarisCliResponseModel)
                                                           .map(PolarisCliResponseModel::getCoverityToolInfo)
                                                           .map(CoverityToolInfo::getJobStatusUrl);

        assumeTrue(potentialJobStatusUrl.isPresent(), "Coverity jobStatusUrl is missing from the cli-scan.json-- this test needs to be updated");
        final String jobStatusUrl = potentialJobStatusUrl.get();

        assertTrue(jobService.waitForJobToCompleteByUrl(jobStatusUrl));
    }

    @Test
    public void testWaitForJobToCompleteById() throws IntegrationException, InterruptedException {
        final Optional<String> potentialJobStatusUrl = Optional.ofNullable(polarisCliResponseModel)
                                                           .map(PolarisCliResponseModel::getCoverityToolInfo)
                                                           .map(CoverityToolInfo::getJobId);

        assumeTrue(potentialJobStatusUrl.isPresent(), "Coverity jobId is missing from the cli-scan.json-- this test needs to be updated");
        final String jobStatusUrl = potentialJobStatusUrl.get();

        assertTrue(jobService.waitForJobToCompleteByUrl(jobStatusUrl));
    }

}
