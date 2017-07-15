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
import io.github.daggerok.tasks.testcaserunner.SoapUITestCaseRunnerGroovy
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.text.DateFormat

@Slf4j
@CompileStatic
@InheritConstructors
class SoapUITestRunnerGroovyTask extends DefaultTask {

  static final String usage = """
Please, make sure you properly configured testrunner task.
Minimal required configuration:

testrunner {
  projects = [
    'path/to/soapui-test-project.xml'
  ]
}"""

  @Optional
  private String projectFile = 'soapui-test-project.xml'

  @Optional
  private String outputFolder = "$project.buildDir/soapui"

  @Optional
  private boolean failOnStart = false

  @TaskAction
  void run() {

    if (getProjectFile()) {

      SoapUITestCaseRunner testCaseRunner = new SoapUITestCaseRunnerGroovy(
          "$project.name (${DateFormat.getDateTimeInstance().format(new Date())})")

      testCaseRunner.setProjectFile(getProjectFile())
      testCaseRunner.setOutputFolder(getOutputFolder())

      setupDefaultRunnerConfigs(testCaseRunner)

      testCaseRunner.getLog().info 'start tests execution...'

      try {

        testCaseRunner.run()

      } catch (final Exception e) {

        testCaseRunner.getLog().error "test execution error: ${e?.localizedMessage}"
      }

    } else {

      if (failOnStart) throw new GradleException(usage)
      log.error usage
    }
  }

  private void setupDefaultRunnerConfigs(final SoapUITestCaseRunner testCaseRunner) {
    testCaseRunner.setPrintReport(true)
    testCaseRunner.setExportAll(false)
    testCaseRunner.setSaveAfterRun(false)
    testCaseRunner.setEnableUI(false)
    testCaseRunner.setJUnitReport(true)
    testCaseRunner.setJUnitReportWithProperties(true)
    testCaseRunner.setIgnoreError(false)
    testCaseRunner.setIgnoreErrors(false)
    testCaseRunner.setPrintAlertSiteReport(true)
  }

  void setProjectFile(final String projectFile) {
    this.projectFile = projectFile
  }

  String getProjectFile() {
    return projectFile
  }

  String getOutputFolder() {
    return outputFolder
  }

  void setOutputFolder(final String outputFolder) {
    this.outputFolder = outputFolder
  }

  boolean getFailOnStart() {
    return failOnStart
  }

  void setFailOnStart(final boolean failOnStart) {
    this.failOnStart = failOnStart
  }
}
