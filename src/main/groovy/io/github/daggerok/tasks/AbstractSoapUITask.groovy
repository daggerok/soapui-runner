/**
 * The MIT License
 *
 * Copyright 2017 Maksim Kostromin
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * https://tldrlegal.com/license/mit-license
 */
package io.github.daggerok.tasks

import com.eviware.soapui.tools.AbstractSoapUITestRunner
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import static io.github.daggerok.utils.ExecutionUtils.tryWithLogMessage

@Slf4j
@CompileStatic
abstract class AbstractSoapUITask extends DefaultTask {

  @Optional @Input boolean failOnError = true

  @Optional @Input String outputFolder = "$project.buildDir/soapui"
  @Optional @Input String projectFile = 'soapui-test-project.xml'

  @Optional @Input String[] systemProperties
  @Optional @Input String[] globalProperties
  @Optional @Input String[] projectProperties

  @Optional @Input String settingsFile
  @Optional @Input String endpoint
  @Optional @Input String domain
  @Optional @Input String host
  @Optional @Input String username
  @Optional @Input String password
  @Optional @Input String wssPasswordType
  @Optional @Input String projectPassword
  @Optional @Input String soapUISettingsPassword

  @Optional @Input boolean enableUI = false

  AbstractSoapUITask(final String id) {
    description = "SoapUI $id task"
    group = 'SoapUI runner'
  }

  @TaskAction
  void run() {
    action()
  }

  abstract void action()

  void setupParent(final AbstractSoapUITestRunner runner) {
    setupRequired runner
    setupProps runner
  }

  private void setupRequired(final AbstractSoapUITestRunner runner) {

    runner.setProjectFile projectFile
    runner.setOutputFolder outputFolder

    log.info "base options configured."
  }

  private void setupProps(final AbstractSoapUITestRunner runner) {

    tryWithLogMessage('parsing system properties failed.') {
      String[] props = System.properties.collect { "$it.key=$it.value" }
      runner.setSystemProperties(systemProperties ?: props)
    }

    if (globalProperties) runner.globalProperties = globalProperties
    if (projectProperties) runner.projectProperties = projectProperties

    if (settingsFile) runner.settingsFile = settingsFile
    if (endpoint) runner.endpoint = endpoint
    if (domain) runner.domain = domain
    if (host) runner.host = host
    if (username) runner.username = username
    if (password) runner.password = password
    if (wssPasswordType) runner.wssPasswordType = wssPasswordType
    if (projectPassword) runner.projectPassword = projectPassword
    if (soapUISettingsPassword) runner.soapUISettingsPassword = soapUISettingsPassword

    runner.setEnableUI enableUI
  }

  // setters

  /**
   * @param failOnError sets if execution should fails on any errors occurs.
   *
   * @default: true
   */
  void setFailOnError(final boolean failOnError) {
    this.failOnError = failOnError
  }

  void setOutputFolder(final String outputFolder) {
    this.outputFolder = outputFolder
  }

  /**
   * Sets the SoapUI project file containing the tests to run
   *
   * @param projectFile the SoapUI project file containing the tests to run
   */
  void setProjectFile(final String projectFile) {
    this.projectFile = projectFile
  }

  /**
   * Sets the SoapUI settings file containing the tests to run
   *
   * @param settingsFile the SoapUI settings file to use
   */
  void setSettingsFile(final String settingsFile) {
    this.settingsFile = settingsFile
  }

  void setSystemProperties(final String[] systemProperties) {
    this.systemProperties = systemProperties
  }

  void setGlobalProperties(final String[] globalProperties) {
    this.globalProperties = globalProperties
  }

  void setProjectProperties(final String[] projectProperties) {
    this.projectProperties = projectProperties
  }

  /**
   * Sets the endpoint to use for all test requests
   *
   * @param endpoint the endpoint to use for all test requests
   */
  void setEndpoint(final String endpoint) {
    this.endpoint = endpoint
  }

  /**
   * Sets the domain to use for any authentications
   *
   * @param domain the domain to use for any authentications
   */
  void setDomain(final String domain) {
    this.domain = domain
  }

  /**
   * Sets the host to use by all test-requests, the existing endpoint port and
   * path will be used
   *
   * @param host the host to use by all requests
   */
  void setHost(final String host) {
    this.host = host
  }

  /**
   * Sets the username to use for any authentications
   *
   * @param username the username to use for any authentications
   */
  void setUsername(final String username) {
    this.username = username
  }

  /**
   * Sets the password to use for any authentications
   *
   * @param password the password to use for any authentications
   */
  void setPassword(final String password) {
    this.password = password
  }

  /**
   * Sets the WSS password-type to use for any authentications. Setting this
   * will result in the addition of WS-Security UsernamePassword tokens to any
   * outgoing request containing the specified username and password.
   *
   * @param wssPasswordType the wss-password type to use, either 'Text' or 'Digest'
   */
  void setWssPasswordType(final String wssPasswordType) {
    this.wssPasswordType = wssPasswordType
  }

  void setProjectPassword(final String projectPassword) {
    this.projectPassword = projectPassword
  }

  void setSoapUISettingsPassword(final String soapUISettingsPassword) {
    this.soapUISettingsPassword = soapUISettingsPassword
  }

  void setEnableUI(final boolean enableUI) {
    this.enableUI = enableUI
  }
}
