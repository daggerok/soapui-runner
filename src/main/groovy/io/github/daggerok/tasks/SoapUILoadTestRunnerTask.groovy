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

import com.eviware.soapui.tools.SoapUILoadTestRunner
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.github.daggerok.tasks.runners.SoapUILoadRunner
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import static io.github.daggerok.utils.ExecutionUtils.*

@Slf4j
@CompileStatic
class SoapUILoadTestRunnerTask extends AbstractSoapUITask {

  static final String name = 'loadtestrunner'

  @Optional @Input String testSuite
  @Optional @Input String testCase
  @Optional @Input String loadTest

  @Optional @Input boolean printReport = true
  @Optional @Input boolean saveAfterRun = false

  @Optional @Input int limit = -1
  @Optional @Input long threadCount = -1

  SoapUILoadTestRunnerTask() {
    super(name)
  }

  /**
   * loadtestrunner task.
   */
  @Override
  void action() {

    tryWithMessage(usage(name)) {
      validate project.file(projectFile), name, failOnError
    }

    final def testRunner = new SoapUILoadRunner("running $name of $project.name: ${today()}")

    setupParent testRunner
    setupProps testRunner

    log.info "start $name execution..."

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
  void setupProps(final SoapUILoadTestRunner runner) {

    if (testSuite) runner.setTestSuite testSuite
    if (testCase) runner.setTestCase testCase
    if (loadTest) runner.setLoadTest loadTest

    runner.setPrintReport printReport
    runner.setSaveAfterRun saveAfterRun

    if (limit > 0) runner.setLimit limit
    if (threadCount > 0) runner.setThreadCount threadCount

    log.info "$name props configured."
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

  void setLoadTest(final String loadTest) {
    this.loadTest = loadTest
  }

  /**
   * Controls if a short test summary should be printed after the test runs
   *
   * @param printReport a flag controlling if a summary should be printed
   */
  void setPrintReport(final boolean printReport) {
    this.printReport = printReport
  }

  void setSaveAfterRun(final boolean saveAfterRun) {
    this.saveAfterRun = saveAfterRun
  }

  void setLimit(final int limit) {
    this.limit = limit
  }

  void setThreadCount(final long threadCount) {
    this.threadCount = threadCount
  }
}
