package io.jenkins.blueocean.agent;

import hudson.Extension;
import io.jenkins.blueocean.rest.OrganizationRoute;
import io.jenkins.blueocean.rest.hal.Link;
import io.jenkins.blueocean.rest.model.BlueOrganization;
import io.jenkins.blueocean.rest.model.Resource;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * @author kzantow
 */
@Extension
@ExportedBean
public class MyAwesomeData extends Resource implements OrganizationRoute {
    @Override
    public String getUrlName() {
        return "awesome";
    }

    @Override
    public Link getLink() {
        // This should always have an organization as a parent, as it's an OrganizationRoute
        Ancestor ancestor = Stapler.getCurrentRequest().findAncestor(BlueOrganization.class);
        BlueOrganization organization = (BlueOrganization)ancestor.getObject();
        return organization.getLink().rel(getUrlName());
    }
    
    @Exported
    public String getMessage() {
        return "you are awesome";
    }
}
