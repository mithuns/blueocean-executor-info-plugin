package com.mycompany.sample.plugin;

import hudson.Extension;
import io.jenkins.blueocean.rest.OrganizationRoute;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * @author kzantow
 */
@Extension
@ExportedBean
public class MyAwesomeData implements OrganizationRoute {
    @Override
    public String getUrlName() {
        return "awesome";
    }
    
    public String getData() {
        return "you are awesome";
    }
}
