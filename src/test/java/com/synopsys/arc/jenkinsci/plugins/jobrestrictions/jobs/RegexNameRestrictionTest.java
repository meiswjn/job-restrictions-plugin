package com.synopsys.arc.jenkinsci.plugins.jobrestrictions.jobs;

import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.restrictions.job.RegexNameRestriction;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.model.Run;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class RegexNameRestrictionTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void regexIsValidatedCorrectly() throws Exception {

    }

    @Test
    public void nonMatchingJobIsRejected() throws Exception {
        RegexNameRestriction regexRestriction = new RegexNameRestriction("^TestFreestylePipeline$", false);
        UpstreamCauseRestriction causeRestriction = new UpstreamCauseRestriction(regexRestriction);
        FreeStyleProject project = JobRestrictionTestHelper.createJob(j, FreeStyleProject.class, "RestrictedJob", causeRestriction);
        FreeStyleProject upstreamProject = j.createFreeStyleProject("UpstreamJob");
        FreeStyleBuild upstreamRun = upstreamProject.scheduleBuild2(0, new Cause.UserIdCause()).get();
        FreeStyleBuild build = project.scheduleBuild2(0, new Cause.UpstreamCause((Run)upstreamRun)).get();
        assertThat("RegEx restriction should have prohibited the launch.", build.getResult(), equalTo(Result.FAILURE));
    }
}
