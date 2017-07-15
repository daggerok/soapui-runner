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
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.github.daggerok.tasks.SoapUITestRunnerGroovyTask
import org.gradle.api.Plugin
import org.gradle.api.Project

@Slf4j
@CompileStatic
@InheritConstructors
class SoapUITestRunnerGroovyPlugin implements Plugin<Project> {

  @Override
  void apply(final Project project) {
    createMainTask(project)
    createExtFolder(project)
  }

  private void createMainTask(final Project project) {
    project.getTasks().create("testrunner", SoapUITestRunnerGroovyTask)
  }

  /**
   * SoapUI testrunner looking ext folder for external scripts and libraries.
   *
   * <br/>
   * I will create that folder if it's not exists.
   * Otherwise if it's exists and it's not a directory it might cause some unexpected issues...
   *
   * @param project applied to
   */
  void createExtFolder(final Project project) {

    final String extDirName = 'ext'
    final File extDir = project.file(extDirName)

    try {

      if (extDir.exists()) {
        if (extDir.isDirectory()) return
        log.error "$extDirName is reserved name for SoapUI libraries folder."
        return
      }

      project.mkdir(extDirName)

    } catch (Exception e) {

      log.error "error on ext folder: ${e?.localizedMessage}"
    }
  }
}
