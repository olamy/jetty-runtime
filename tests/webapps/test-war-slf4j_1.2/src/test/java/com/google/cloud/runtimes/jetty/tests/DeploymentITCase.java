package com.google.cloud.runtimes.jetty.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.cloud.runtime.jetty.testing.AppDeployment;
import com.google.cloud.runtime.jetty.testing.HttpURLUtil;
import com.google.cloud.runtime.jetty.testing.RemoteLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DeploymentITCase
{
    @Test
    public void testGet() throws IOException
    {
        // Trigger events
        HttpURLConnection http = HttpURLUtil.openTo(AppDeployment.SERVER_URI.resolve("/logging"));
        Assert.assertThat(http.getResponseCode(), is(200));
    
        // Fetch logging events on server
        List<RemoteLog.Entry> logs = RemoteLog.getLogs(AppDeployment.MODULE_ID, AppDeployment.VERSION_ID);
    
        List<String> expectedEntries = new ArrayList<>();
        
        // Results from /logging
        expectedEntries.add("[DEBUG] LoggingServlet - LoggingServlet(slf4j-1.2) initialized");
        expectedEntries.add("[INFO ] LoggingServlet - LoggingServlet(slf4j-1.2) GET requested");
        expectedEntries.add("[WARN ] LoggingServlet - LoggingServlet(slf4j-1.2) Slightly warn, with a chance of log events");
        expectedEntries.add("[FATAL] LoggingServlet - LoggingServlet(slf4j-1.2) Whoops (intentionally) causing a Throwable");
        
        RemoteLog.assertHasEntries(logs, expectedEntries);
    
        RemoteLog.Entry entry = RemoteLog.findEntry(logs, "[FATAL] LoggingServlet - LoggingServlet(slf4j-1.2) Whoops (intentionally) causing a Throwable");
        assertThat("Multi-Line Log", entry.getTextPayload(), containsString("java.io.FileNotFoundException: A file cannot be found"));
        assertThat("Multi-Line Log", entry.getTextPayload(), containsString("at com.google.cloud.runtime.jetty.tests.webapp.LoggingServlet.doGet(LoggingServlet.java"));
    }
}