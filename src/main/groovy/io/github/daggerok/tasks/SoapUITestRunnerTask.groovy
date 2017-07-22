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
import groovy.util.logging.Slf4j
import io.github.daggerok.tasks.runners.SoapUITestRunner
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import static io.github.daggerok.utils.ExecutionUtils.*

@Slf4j
@CompileStatic
class SoapUITestRunnerTask extends AbstractSoapUITask {

  public static final String NAME = 'testrunner'

  @Optional @Input String testSuite
  @Optional @Input String testCase

  @Optional @Input boolean printReport = true
  @Optional @Input boolean printAlertSiteReport = true
  @Optional @Input boolean exportAll = true
  @Optional @Input boolean ignoreErrors = false
  @Optional @Input boolean junitReport = true
  @Optional @Input boolean junitReportWithProperties = true
  @Optional @Input boolean saveAfterRun = false

  @Optional @Input int maxErrors = 5

  SoapUITestRunnerTask() {
    super(NAME)
  }

  /**
   * testrunner task.
   */
  @Override
  void action() {

    tryWithMessage(usage(NAME)) {
      validate project.file(projectFile), NAME, failOnError
    }

    final def testRunner = new SoapUITestRunner("running $NAME of $project.name: ${today()}")

    setupParent testRunner
    setupProps testRunner

    log.info "start $NAME execution..."

    tryOrThrowIf(failOnError) {
      testRunner.run()
    }
  }

  /**
   * If testSuite or testCase was provided, whole project tests will not be executed,
   * but instead, only specified filters for suite or case.
   *
   * @param runner SoapUI test case runner to be configured.
   */
  void setupProps(final SoapUITestCaseRunner runner) {

    if (testSuite) runner.setTestSuite testSuite
    if (testCase) runner.setTestCase testCase
    if (maxErrors) runner.setMaxErrors maxErrors

    runner.setPrintReport printReport
    runner.setPrintAlertSiteReport printAlertSiteReport
    runner.setExportAll exportAll
    runner.setIgnoreErrors ignoreErrors
    runner.setJUnitReport junitReport
    // // not available until SoapUI 5.2.1
    // runner.setJUnitReportWithProperties junitReportWithProperties
    runner.setSaveAfterRun saveAfterRun

    log.info "$NAME props configured."
  }

  // setters

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

  void setMaxErrors(final int maxErrors) {
    this.maxErrors = maxErrors
  }

  /**
   * Controls if a short test summary should be printed after the test runs
   *
   * @param printReport a flag controlling if a summary should be printed
   */
  void setPrintReport(final boolean printReport) {
    this.printReport = printReport
  }

  void setPrintAlertSiteReport(final boolean printAlertSiteReport) {
    this.printAlertSiteReport = printAlertSiteReport
  }

  /**
   * Add console appender to groovy log
   */
  void setExportAll(final boolean exportAll) {
    this.exportAll = exportAll
  }

  void setIgnoreErrors(final boolean ignoreErrors) {
    this.ignoreErrors = ignoreErrors
  }

  void setJunitReport(final boolean junitReport) {
    this.junitReport = junitReport
  }

  void setJunitReportWithProperties(final boolean junitReportWithProperties) {
    this.junitReportWithProperties = junitReportWithProperties
  }

  void setSaveAfterRun(final boolean saveAfterRun) {
    this.saveAfterRun = saveAfterRun
  }
}
