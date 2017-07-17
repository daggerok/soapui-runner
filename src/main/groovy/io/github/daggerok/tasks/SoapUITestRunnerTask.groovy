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

import com.eviware.soapui.tools.SoapUITestCaseRunner
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.github.daggerok.tasks.runners.SoapUITestCaseTestRunner
import org.gradle.api.GradleException
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.text.DateFormat

import static io.github.daggerok.utils.Utils.tryOrThrowIf
import static io.github.daggerok.utils.Utils.tryWithMessage

@Slf4j
@CompileStatic
@InheritConstructors
@SuppressWarnings("unused")
class SoapUITestRunnerTask extends AbstractSoapUITask {

  static final String USAGE = '''
Please, make sure you properly configured testrunner task.
Minimal required configuration is existed SoapUI test project file. 
Default path is: "${projectDir}/soapui-test-project.xml"

testrunner {
  projectFile = 'soapui-test-project.xml'
}'''

  @Optional boolean printReport = true
  @Optional boolean exportAll = true
  @Optional boolean enableUI = false
  @Optional boolean junitReport = true
  @Optional boolean junitReportWithProperties = true
  @Optional boolean ignoreError = false
  @Optional boolean ignoreErrors = false
  @Optional boolean printAlertSiteReport = true
  @Optional boolean saveAfterRun = false

  @Optional boolean failOnError = true

  @Optional String testSuite = null
  @Optional String testCase = null

  @TaskAction
  void run() {

    tryWithMessage(USAGE) {
      validate project.file(projectFile)
    }

    def today = DateFormat.getDateTimeInstance().format(new Date())
    def testCaseRunner = new SoapUITestCaseTestRunner("running $project.name: $today")

    setupRequired testCaseRunner
    setupProperties testCaseRunner
    setupOptionals testCaseRunner

    setupFilters testCaseRunner
    setupFlags testCaseRunner

    log.info 'start tests execution...'

    tryOrThrowIf(failOnError) {
      testCaseRunner.run()
    }
  }

  def validate(final File file) {

    if (file.exists() && file.canRead()) return

    def message = "file $file doesn't exists or not accessible.\n$USAGE"
    log.error message
    if (failOnError) throw new GradleException(message)
  }

  /**
   * If testSuite or testCase was provided, whole project tests will not be executed,
   * but instead, only specified filters for suite or case.
   *
   * @param runner SoapUI test case runner to be configured.
   */
  def setupFilters(final SoapUITestCaseRunner runner) {

    if (testSuite) runner.setTestSuite testSuite
    if (testCase) runner.setTestCase testCase

    log.info "filters configured."
  }

  /**
   * Setup other flags.
   *
   * @param runner SoapUI test case runner to be configured.
   */
  private void setupFlags(final SoapUITestCaseRunner runner) {

    runner.setPrintReport printReport
    runner.setExportAll exportAll
    runner.setSaveAfterRun saveAfterRun
    runner.setEnableUI enableUI
    runner.setJUnitReport junitReport
    runner.setJUnitReportWithProperties junitReportWithProperties
    runner.setIgnoreError ignoreError
    runner.setIgnoreErrors ignoreErrors
    runner.setPrintAlertSiteReport printAlertSiteReport

    log.info "flags configured."
  }

  void setPrintReport(final boolean printReport) {
    this.printReport = printReport
  }

  void setExportAll(final boolean exportAll) {
    this.exportAll = exportAll
  }

  void setEnableUI(final boolean enableUI) {
    this.enableUI = enableUI
  }

  void setJunitReport(final boolean junitReport) {
    this.junitReport = junitReport
  }

  void setJunitReportWithProperties(final boolean junitReportWithProperties) {
    this.junitReportWithProperties = junitReportWithProperties
  }

  void setIgnoreError(final boolean ignoreError) {
    this.ignoreError = ignoreError
  }

  void setIgnoreErrors(final boolean ignoreErrors) {
    this.ignoreErrors = ignoreErrors
  }

  void setPrintAlertSiteReport(final boolean printAlertSiteReport) {
    this.printAlertSiteReport = printAlertSiteReport
  }

  void setSaveAfterRun(final boolean saveAfterRun) {
    this.saveAfterRun = saveAfterRun
  }

  /**
   * @param failOnError sets if execution should fails on any errors occurs.
   *
   * @default: true
   */
  void setFailOnError(final boolean failOnError) {
    this.failOnError = failOnError
  }

  /**
   * Sets the TestSuite to run. If not set all TestSuites in the specified
   * project file are run
   *
   * @param testSuite the testSuite to run.
   */
  void setTestSuite(final String testSuite) {
    this.testSuite = testSuite
  }

  /**
   * Sets the testcase to run
   *
   * @param testCase the testcase to run
   */
  void setTestCase(final String testCase) {
    this.testCase = testCase
  }

  /*
   * deprecations
   */

  @Optional
  @Deprecated
  private boolean failOnStart = false

  /**
   * @param failOnStart sets true if execution should fail on any configuration errors...
   * @default: false
   * @deprecated use failOnError instead.
   */
  @Deprecated
  void setFailOnStart(final boolean failOnStart) {
    this.failOnStart = failOnStart
  }
}
