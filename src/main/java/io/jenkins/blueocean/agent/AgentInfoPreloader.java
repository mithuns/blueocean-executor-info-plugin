package io.jenkins.blueocean.agent;

import hudson.Extension;
import io.jenkins.blueocean.commons.PageStatePreloader;

/**
 * Provides configuration data, basic usage:
 * import { blueocean } from '@jenkins-cd/blueocean-core-js/dist/js/scopes';
 * data available at: blueocean.agent
 */
@Extension
public class AgentInfoPreloader extends PageStatePreloader {
    @Override
    public String getStatePropertyPath() {
        return "agent";
    }
    @Override
    public String getStateJson() {
        return "'some-config-data'";
    }
}
