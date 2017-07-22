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
package io.github.daggerok

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static io.github.daggerok.utils.TestUtils.getGradleBuildHead
import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class SoapUIRunnerPluginFunctionalTest extends Specification {

  final static String head = getGradleBuildHead('4.6.2')

  @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()

  File buildFile

  def setup() {
    buildFile = testProjectDir.newFile('build.gradle')
  }

  def 'testrunner task SUCCESS without execution (no suapui test project file was found).'() {

    given:
    buildFile << """$head
    testrunner {
      projectFile "soapui-test-project.xml"
      outputFolder "soapui"
      failOnError false
    }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('testrunner')
        .build()
    then:
    result.task(':testrunner').outcome == SUCCESS
    result.output.contains('Please, make sure you properly configured testrunner task.')
    !new File(testProjectDir.root, 'build/soapui').exists()
  }

  def 'testrunner task FAILED with failOnStart = true (no suapui test project file was found).'() {

    given:
    buildFile << """$head
    testrunner {
      failOnError = true
    }
    """

    when:
    def result = GradleRunner.create()
                             .withProjectDir(testProjectDir.root)
                             .withArguments('testrunner')
                             .buildAndFail()
    then:
    result.task(':testrunner').outcome == FAILED
    result.output.contains('Please, make sure you properly configured testrunner task.')
    !new File(testProjectDir.root, 'build/soapui').exists()
  }

  def 'task testrunner and loadtestrunner are exists on applied plugin.'() {

    given:
    buildFile << """$head
    // testrunner {}
    // loadtestrunner {}
    """

    when:
    def result = GradleRunner.create()
                             .withProjectDir(testProjectDir.root)
                             .withArguments('tasks', '--all')
                             .build()
    then:
    result.task(':tasks').outcome == SUCCESS
    result.output.contains('SoapUI runner tasks')
    result.output.contains('testrunner - SoapUI testrunner task')
    result.output.contains('loadtestrunner - SoapUI loadtestrunner task')
  }
}
