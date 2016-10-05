package com.google.cloud.runtimes.jetty.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.google.cloud.runtime.jetty.testing.AppDeployment;
import com.google.cloud.runtime.jetty.testing.HttpUrlUtil;
import com.google.cloud.runtime.jetty.testing.RemoteLog;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeploymentITCase {
  @BeforeClass
  public static void isServerUp() {
    HttpUrlUtil.waitForServerUp(AppDeployment.SERVER_URI, 5, TimeUnit.MINUTES);
  }

  @Test
  public void testGet() throws IOException {
    // Trigger events
    String dump = issueGet("/dump/");
    String reqAttr = issueGet("/attr/request/");
    String ctxAttr = issueGet("/attr/context/");
    String counting = issueGet("/counting/");

    // Fetch logging events on server
    List<RemoteLog.Entry> logs =
        RemoteLog.getLogs(AppDeployment.SERVICE_ID, AppDeployment.VERSION_ID);

    assertHasLogEntry("/dump/", logs, dump);
    assertHasLogEntry("/attr/request/", logs, reqAttr);
    assertHasLogEntry("/attr/context", logs, ctxAttr);
    assertHasLogEntry("/counting/", logs, counting);
  }

  private void assertHasLogEntry(String msg, List<RemoteLog.Entry> logs, String expected)
      throws IOException {
    try (StringReader reader = new StringReader(expected);
        BufferedReader buf = new BufferedReader(reader)) {
      // Find first line
      String first = buf.readLine();
      RemoteLog.Entry entry = RemoteLog.findEntry(logs, first);
      assertThat("Entry with first line [" + first + "]", entry, notNullValue());
      String line;
      while ((line = buf.readLine()) != null) {
        assertThat("Multi-Line entry", entry.getTextPayload(), containsString(line));
      }
    }
  }

  private String issueGet(String urlPath) throws IOException {
    HttpURLConnection http = HttpUrlUtil.openTo(AppDeployment.SERVER_URI.resolve(urlPath));
    Assert.assertThat(http.getResponseCode(), is(200));
    return HttpUrlUtil.getResponseBody(http);
  }
}
