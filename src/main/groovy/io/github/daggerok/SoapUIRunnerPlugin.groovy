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

import groovy.transform.CompileStatic
import io.github.daggerok.tasks.SoapUILoadTestRunnerTask
import io.github.daggerok.tasks.SoapUITestRunnerTask
import org.gradle.api.Plugin
import org.gradle.api.Project

import static io.github.daggerok.utils.ExecutionUtils.createExtFolder

@CompileStatic
class SoapUIRunnerPlugin implements Plugin<Project> {

  @Override
  void apply(final Project project) {
    project.getTasks().create('testrunner', SoapUITestRunnerTask)
    project.getTasks().create('loadtestrunner', SoapUILoadTestRunnerTask)
    createExtFolder(project)
  }
}