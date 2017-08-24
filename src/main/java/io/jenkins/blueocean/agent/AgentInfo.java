package io.jenkins.blueocean.agent;

import hudson.Extension;
import hudson.model.Computer;
import hudson.model.Executor;
import hudson.model.Job;
import hudson.model.Label;
import hudson.model.Queue;
import hudson.model.Run;
import hudson.model.queue.SubTask;
import io.jenkins.blueocean.rest.OrganizationRoute;
import io.jenkins.blueocean.rest.hal.Link;
import io.jenkins.blueocean.rest.model.BlueOrganization;
import io.jenkins.blueocean.rest.model.Resource;
import java.util.ArrayList;
import java.util.List;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * @author kzantow
 */
@Extension
@ExportedBean
public class AgentInfo extends Resource implements OrganizationRoute {
    @Override
    public String getUrlName() {
        return "agents";
    }

    @Override
    public Link getLink() {
        // This should always have an organization as a parent, as it's an OrganizationRoute
        Ancestor ancestor = Stapler.getCurrentRequest().findAncestor(BlueOrganization.class);
        BlueOrganization organization = (BlueOrganization)ancestor.getObject();
        return organization.getLink().rel(getUrlName());
    }
    
    @Exported(inline=true)
    public ComputerInfo[] getAgentInfo() throws Exception {
        List<ComputerInfo> info = new ArrayList<>();
                
        Jenkins j = Jenkins.getInstance();
        
        for (Computer c : j.getComputers()) {
            System.out.println("Computer: " + c.getDisplayName());
            EXECUTORS: for (Executor e : c.getExecutors()) {
                System.out.println("Executor: " + e.getDisplayName());
                Queue.Executable currentExecutable = e.getCurrentExecutable();
                if (currentExecutable != null) {
                    SubTask executing = currentExecutable.getParent();
                    int depth = 0;
                    while (depth < 5 && executing != null) {
                        System.out.println("Task: " + executing.getClass() + " : " + executing.getDisplayName());
                        if (executing instanceof ExecutorStepExecution.PlaceholderTask) {
                            ExecutorStepExecution.PlaceholderTask task = (ExecutorStepExecution.PlaceholderTask)executing;
                            Run r = task.runForDisplay();
                            info.add(new ComputerInfo(getLink(), r, e.getDisplayName()));
                            continue EXECUTORS;
                        }
                        if (executing instanceof Job<?, ?>) {
                            Job<?, ?> task = (Job<?, ?>)executing;
                            System.out.println("Job found...:" + task.getDisplayName());
                            continue EXECUTORS;
                        }
                        if (executing instanceof Run<?, ?>) {
                            Run<?, ?> r = (Run)executing;
                            info.add(new ComputerInfo(getLink(), r, e.getDisplayName()));
                            continue EXECUTORS;
                        }
                        if (executing == executing.getOwnerTask()) {
                            continue EXECUTORS;
                        }
                        executing = executing.getOwnerTask();
                        depth++;
                    }
                }
            }
        }
        
        Queue q = j.getQueue();
        for (Queue.Item i : q.getItems()) {
            System.out.println("Queue task: " + i.task.getClass() + " : " + i.task.getDisplayName());
            if (i.task instanceof ExecutorStepExecution.PlaceholderTask) {
                ExecutorStepExecution.PlaceholderTask task = (ExecutorStepExecution.PlaceholderTask)i.task;
                Label label = i.getAssignedLabel();
                Run r = task.runForDisplay();
                info.add(new ComputerInfo(getLink(), r, label.getDisplayName()));
            }
        }
        
        return info.toArray(new ComputerInfo[info.size()]);
    }
    
    @ExportedBean
    public static class ComputerInfo extends Resource {
        final Link parent;
        final Run<?, ?> run;
        final String agentName;

        public ComputerInfo(Link parent, Run<?, ?> run, String agentName) {
            this.parent = parent;
            this.run = run;
            this.agentName = agentName;
        }

        @Exported
        public String getAgentName() {
            return agentName;
        }
        
        @Exported
        public String getPipelineId() {
            return run.getParent().getFullName();
        }

        @Exported
        public String getRunId() {
            return run.getId();
        }

        @Override
        public Link getLink() {
            return parent.rel("agents");
        }
    }
}
